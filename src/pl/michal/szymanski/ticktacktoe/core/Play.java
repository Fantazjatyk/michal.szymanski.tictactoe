/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import com.google.common.base.Stopwatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import pl.michal.szymanski.ticktacktoe.core.model.Board;
import pl.michal.szymanski.ticktacktoe.core.model.GameMaster;
import pl.michal.szymanski.ticktacktoe.core.model.Move;
import pl.michal.szymanski.ticktacktoe.transport.Endpoint;
import pl.michal.szymanski.ticktacktoe.transport.GameWatcher;
import pl.michal.szymanski.ticktacktoe.transport.MultiplayerEndpoint;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class Play<T extends Endpoint> extends PlayBase<T> {

    private Board board = new Board();
    private GameMaster gameMaster = new GameMaster();
    private PlayersPair<T> players = new PlayersPair();
    private LinkedBlockingDeque<Move> moves = new LinkedBlockingDeque(9);
    private LinkedBlockingDeque<GameWatcher> watchers = new LinkedBlockingDeque(100);
    private PlayLimits limits = new PlayLimits();

    protected Board getBoard() {
        return this.board;
    }

    protected PlayersPair<T> players() {
        return this.players;
    }

    public void watch(GameWatcher watcher) {
        this.watchers.add(watcher);
    }

    protected boolean isDone() {
        return gameMaster.isDone(board);
    }

    public void begin() {
        start();
        while (!isDone()) {
            T endpoint = getNextEndpoint();
            doTurn(endpoint, limits.getTurnLimit());
            sendBoard();
        }
        finish();
    }

    public void sendBoard() {
        players.firstPlayer().get().endpoint().receiveBoard(board);
        players.secondPlayer().get().endpoint().receiveBoard(board);

        watchers.stream().forEach((el) -> el.receiveBoard(board));
    }

    public void doTurn(T endpoint, long timeout) {
        Move move = null;
        Stopwatch stopwatch = Stopwatch.createStarted();

        while (stopwatch.elapsed(TimeUnit.SECONDS) < timeout) {
            move = endpoint.getMove();

            if (move != null && board.isValid(move)) {
                board.doMove(move);
                break;
            }
        }

    }

    private T getNextEndpoint() {
        Move lastMove = moves.getLast();

        return null;
    }
}
