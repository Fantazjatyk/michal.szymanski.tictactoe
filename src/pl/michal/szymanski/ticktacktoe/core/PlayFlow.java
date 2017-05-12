/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import pl.michal.szymanski.ticktacktoe.control.TimerNotifier;
import com.google.common.base.Stopwatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.michal.szymanski.ticktacktoe.control.GameTimeoutNotify;
import pl.michal.szymanski.ticktacktoe.core.PlaySettings.PlaySettingsSetters;
import pl.michal.szymanski.ticktacktoe.transport.GameWatcher;
import pl.michal.szymanski.ticktacktoe.transport.Participant;
import pl.michal.szymanski.ticktacktoe.transport.MultiplayerParticipant;
import pl.michal.szymanski.ticktacktoe.transport.TurnTimeoutHandler;
import pl.michal.szymanski.ticktacktoe.control.TurnTimeoutNotify;
import pl.michal.szymanski.ticktacktoe.transport.GameTimeoutHandler;
import pl.michal.szymanski.ticktacktoe.transport.ProxyResponse;
import pl.michal.szymanski.ticktacktoe.transport.WatchdogHandler;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class PlayFlow<T extends Participant> extends Play<T> implements GameTimeoutHandler, TurnTimeoutHandler {

    private TimerNotifier gameTimeoutNotifier;
    private boolean isTimedOut = false;
    private TimerNotifier turnTimeoutNotifier;
    private Thread moveLoader;

    @Override
    public void onGameTimeout() {
        this.isTimedOut = true;
        onTurnTimeout();
        onFinish();
    }

    @Override
    public void onTurnTimeout() {
        if (moveLoader != null) {
            moveLoader.interrupt();
        }
    }

    @Override
    protected boolean isDone() {
        return super.isDone() || this.isTimedOut;
    }

    @Override
    protected void doTurn(Player<T> player) {
        this.turnTimeoutNotifier.start(super.getPlaySettings().getters().getTurnLimit());
        super.getHistory().getTurns().addLast(new Turn(player, super.getInfo().getBoard()));

        moveLoader = getConnectorMoveLoader(player);
        moveLoader.setDaemon(true);
        synchronized (moveLoader) {
            moveLoader.start();
            try {
                moveLoader.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(PlayFlow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.turnTimeoutNotifier.stop();
    }

    private Thread getConnectorMoveLoader(Player<T> player) {
        Board board = super.getInfo().getBoard();
        LinkedBlockingDeque<Move> moves = super.getHistory().getMoves();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Move move = null;
                while (move == null && !this.isInterrupted()) {
                    ProxyResponse<Point> response = new ProxyResponse();
                    player.connector().getMoveField(response.getSetters());

                    if (response.getGetters().getReal().isPresent()) {
                        move = new Move(player, response.getGetters().getReal().get());

                        if (GameMaster.isValidMove(move, board)) {
                            board.doMove(move);
                            moves.addLast(move);
                        }
                    }
                }

            }

            @Override
            public void interrupt() {
                super.interrupt(); //To change body of generated methods, choose Tools | Templates.
                synchronized (this) {
                    this.notifyAll();
                }
            }

        };

        return thread;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.gameTimeoutNotifier = TimerNotifier.createStarted(super.getPlaySettings().getters().getTimeout(), new GameTimeoutNotify());
        this.gameTimeoutNotifier.addObserver(this);

        this.turnTimeoutNotifier = TimerNotifier.create(super.getPlaySettings().getters().getTurnLimit(), new TurnTimeoutNotify());
        this.turnTimeoutNotifier.addObserver(super.getInfo().getPlayers().secondPlayer().get().connector());
        this.turnTimeoutNotifier.addObserver(super.getInfo().getPlayers().firstPlayer().get().connector());
        this.turnTimeoutNotifier.addObserver(this);
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        super.getHistory().getWatchers().stream().forEach((el) -> el.onGameEnd(super.getInfo(), super.getPlaySettings().getters()));
    }

}
