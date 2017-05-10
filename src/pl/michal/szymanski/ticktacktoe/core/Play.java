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
import pl.michal.szymanski.ticktacktoe.transport.WatchdogHandler;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class Play<T extends Participant> extends PlayBase<T> implements GameTimeoutHandler, TurnTimeoutHandler {

    private Board board = new Board(3);
    private PlayersPair<T> players = new PlayersPair();
    private LinkedBlockingDeque<Move> moves = new LinkedBlockingDeque(9);
    private LinkedBlockingDeque<GameWatcher> watchers = new LinkedBlockingDeque(100);
    private PlaySettings limits = new PlaySettings();
    private LinkedBlockingDeque<Turn> turns = new LinkedBlockingDeque();
    private Optional<Player> winner = Optional.empty();
    private TimerNotifier gameTimeoutNotifier;
    private boolean isTimedOut = false;
    private TimerNotifier turnTimeoutNotifier;
    private Thread moveLoader;

    protected Board getBoard() {
        return this.board;
    }

    protected PlayersPair<T> players() {
        return this.players;
    }

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

    public void watch(GameWatcher watcher) {
        this.watchers.add(watcher);
    }

    private boolean isDone() {
        return GameMaster.isDone(board) || this.isTimedOut;
    }

    public PlaySettingsSetters settings() {
        return this.limits.setters();
    }

    public void begin() {
        onStart();
        while (!isDone()) {
            Player<T> player = this.turns.isEmpty() ? getRandomPlayer() : getNextPlayer();
            doTurn(player);
            sendBoard();
        }
        onFinish();
    }

    public Optional<Player> getWinner() {
        return winner;
    }

    private void sendBoard() {
        watchers.stream().forEach((el) -> el.receiveBoard(board));
    }

    private void doTurn(Player<T> player) {
        this.turnTimeoutNotifier.start(this.limits.getters().getTurnLimit());
        this.turns.addLast(new Turn(player, board));

        moveLoader = getConnectorMoveLoader(player);
        moveLoader.setDaemon(true);
        synchronized (moveLoader) {
            moveLoader.start();
            try {
                moveLoader.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Play.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.turnTimeoutNotifier.stop();
    }

    private Thread getConnectorMoveLoader(Player<T> player) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Move move = null;
                while (move == null || !this.isInterrupted()) {
                    Point field = player.connector().getMoveField();
                    move = new Move(player, field);

                    if (move != null && GameMaster.isValidMove(move, board)) {
                        board.doMove(move);
                        moves.addLast(move);
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
        this.watchers.add((GameWatcher) this.players.firstPlayer().get().connector());
        this.watchers.add((GameWatcher) this.players.secondPlayer().get().connector());
        assignBoardFieldsMarks();
        this.gameTimeoutNotifier = TimerNotifier.createStarted(this.limits.getters().getTimeout(), new GameTimeoutNotify());
        this.gameTimeoutNotifier.addObserver(this);

        this.turnTimeoutNotifier = TimerNotifier.create(this.limits.getters().getTurnLimit(), new TurnTimeoutNotify());
        this.turnTimeoutNotifier.addObserver(this.players.secondPlayer().get().connector());
        this.turnTimeoutNotifier.addObserver(this.players.firstPlayer().get().connector());
        this.turnTimeoutNotifier.addObserver(this);
    }

    @Override
    protected void onFinish() {
        List<Player> winners = GameMaster.getWinner(board);
        winner = winners.size() == 1 ? Optional.of(winners.get(0)) : Optional.empty();
        watchers.stream().forEach((el) -> el.onGameEnd(this));
    }

    private Player<T> getRandomPlayer() {
        boolean isThatFirst = new Random().nextBoolean();
        return isThatFirst ? this.players.firstPlayer().get() : this.players.secondPlayer().get();
    }

    private void assignBoardFieldsMarks() {

        boolean random = new Random().nextBoolean();

        if (random) {
            players.firstPlayer().get().setBoardFieldType(BoardFieldType.XMark);
            players.secondPlayer().get().setBoardFieldType(BoardFieldType.OMark);
        } else {
            players.firstPlayer().get().setBoardFieldType(BoardFieldType.OMark);
            players.secondPlayer().get().setBoardFieldType(BoardFieldType.XMark);
        }

    }

    private Player<T> getNextPlayer() {
        Turn lastTurn = turns.getLast();
        return lastTurn.getQuarterback().equals(players.firstPlayer().get()) ? players.secondPlayer().get() : players.firstPlayer().get();
    }
}
