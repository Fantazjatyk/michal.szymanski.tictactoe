/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.core;

import pl.michal.szymanski.tictactoe.control.TimerNotifier;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.michal.szymanski.tictactoe.control.GameTimeoutNotify;
import pl.michal.szymanski.tictactoe.core.PlaySettings.PlaySettingsSetters;
import pl.michal.szymanski.tictactoe.transport.GameWatcher;
import pl.michal.szymanski.tictactoe.transport.Participant;
import pl.michal.szymanski.tictactoe.transport.MultiplayerParticipant;
import pl.michal.szymanski.tictactoe.transport.TurnTimeoutHandler;
import pl.michal.szymanski.tictactoe.control.TurnTimeoutNotify;
import pl.michal.szymanski.tictactoe.transport.EventProxyResponse;
import pl.michal.szymanski.tictactoe.transport.GameTimeoutHandler;
import pl.michal.szymanski.tictactoe.transport.ProxyResponse;
import pl.michal.szymanski.tictactoe.transport.WatchdogHandler;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class PlayFlow<T extends Participant> extends Play<T> implements GameTimeoutHandler, TurnTimeoutHandler {

    private TimerNotifier gameTimeoutNotifier;
    private boolean isTimedOut = false;

    @Override
    public void onGameTimeout() {
        this.isTimedOut = true;
        onTurnTimeout();
        onFinish();
    }

    @Override
    public void onTurnTimeout() {

    }

    @Override
    protected boolean isDone() {
        return super.isDone() || this.isTimedOut;
    }

    @Override
    protected void doTurn(Player<T> player) {
        super.getHistory().getTurns().addLast(new Turn(player, super.getInfo().getBoard()));
        getMove(player);
    }

    private void getMove(Player<T> player) {
        Move move = null;
        EventProxyResponse<Point> response = new EventProxyResponse();
        player.connector().get().getMoveField(response.getSetters());
        Lock lock = new ReentrantLock();
        response.setLock(lock);

        synchronized (lock) {
            try {
                lock.wait(this.getPlaySettings().getters().getTurnLimit());
            } catch (InterruptedException ex) {
                Logger.getLogger(PlayFlow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (response.getGetters().getReal().isPresent()) {
            move = new Move(player, response.getGetters().getReal().get());
            if (GameMaster.isValidMove(move, super.getInfo().getBoard())) {
                super.getInfo().getBoard().doMove(move);
                super.getHistory().getMoves().addLast(move);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.gameTimeoutNotifier = TimerNotifier.createStarted(super.getPlaySettings().getters().getTimeout(), new GameTimeoutNotify());
        this.gameTimeoutNotifier.addObserver(this);
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        super.getHistory().getWatchers().stream().forEach((el) -> el.onGameEnd(super.getInfo(), super.getPlaySettings().getters()));
    }

}
