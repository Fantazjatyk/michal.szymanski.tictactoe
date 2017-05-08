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
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import pl.michal.szymanski.ticktacktoe.core.model.Board;
import pl.michal.szymanski.ticktacktoe.core.model.BoardField;
import pl.michal.szymanski.ticktacktoe.core.model.GameMaster;
import pl.michal.szymanski.ticktacktoe.core.model.Move;
import pl.michal.szymanski.ticktacktoe.core.model.Player;
import pl.michal.szymanski.ticktacktoe.transport.GameWatcher;
import pl.michal.szymanski.ticktacktoe.transport.Participant;
import pl.michal.szymanski.ticktacktoe.transport.MultiplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class Play<T extends Participant> extends PlayBase<T> {

    private Board board = new Board(3);
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
        return GameMaster.isDone(board);
    }

    public void begin() {
        onStart();
        while (!isDone()) {
            Player<T> player = this.moves.isEmpty() ? getRandomPlayer() : getNextPlayer();
            doTurn(player, limits.getTurnLimit());
            sendBoard();
        }
        onFinish();
    }

    public void sendBoard() {
        players.firstPlayer().get().connector().receiveBoard(board);
        players.secondPlayer().get().connector().receiveBoard(board);

        watchers.stream().forEach((el) -> el.receiveBoard(board));
    }

    public void doTurn(Player<T> player, long timeout) {
        Move move = null;
        Stopwatch stopwatch = Stopwatch.createStarted();

        while (stopwatch.elapsed(TimeUnit.SECONDS) < timeout) {
            move = player.connector().getMove();

            if (move != null && GameMaster.isValidMove(move)) {
                board.doMove(move);
                break;
            }
        }

    }

    public void end() {

    }

    private Player<T> getRandomPlayer() {
        boolean isThatFirst = new Random().nextBoolean();
        return isThatFirst ? this.players.firstPlayer().get() : this.players.secondPlayer().get();
    }

    private Player<T> getNextPlayer() {
        Move lastMove = moves.getLast();
        return lastMove.getInvoker().get().equals(players.firstPlayer().get()) ? players.secondPlayer().get() : players.firstPlayer().get();
    }
}
