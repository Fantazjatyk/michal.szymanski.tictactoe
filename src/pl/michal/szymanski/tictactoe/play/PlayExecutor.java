/*
 * The MIT License
 *
 * Copyright 2017 Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.michal.szymanski.tictactoe.play;

import com.google.common.base.Stopwatch;
import java.util.List;
import java.util.Optional;
import pl.michal.szymanski.tictactoe.play.Play;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.michal.szymanski.tictactoe.control.GameTimeoutNotify;
import pl.michal.szymanski.tictactoe.control.TimerNotifier;
import pl.michal.szymanski.tictactoe.exceptions.NotAllPlayersPresentException;
import pl.michal.szymanski.tictactoe.model.Move;
import pl.michal.szymanski.tictactoe.model.Player;
import pl.michal.szymanski.tictactoe.model.Point;
import pl.michal.szymanski.tictactoe.model.Turn;
import pl.michal.szymanski.tictactoe.play.GameMaster;
import pl.michal.szymanski.tictactoe.transport.LockProxyResponse;
import pl.michal.szymanski.tictactoe.transport.GameTimeoutHandler;
import pl.michal.szymanski.tictactoe.transport.Participant;
import pl.michal.szymanski.tictactoe.transport.PlayerDisconnectedHandler;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayExecutor<T extends Participant> implements GameTimeoutHandler, PlayerDisconnectedHandler {

    private Play<T> play;
    private TimerNotifier gameTimeoutNotifier;
    private Stopwatch watch;
    private boolean isTerminated = false;
    private boolean isTimedOut = false;
    private boolean isEnded = false;
    private ExecutorStatus status = ExecutorStatus.Started;
    private PlayStartEndCallbacks startEnd = new PlayStartEndCallbacks();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public PlayExecutor(Play<T> play) {
        this.play = play;
    }

    public PlayStartEndCallbacks.ReducedVisiblity setCallbacks() {
        return this.startEnd.get();
    }

    public Play<T> getPlay() {
        return play;
    }

    public boolean isRunning() {
        return !isTerminated || !isTimedOut || !isEnded;
    }

    public final void execute() {
        if (!play.getInfo().getPlayers().isPair()) {
            throw new NotAllPlayersPresentException();
        }
        logger.log(Level.SEVERE, "GAME START");
        this.status = ExecutorStatus.Running;
        watch = Stopwatch.createStarted();
        play.onStart();
        startEnd.onStart();
        this.gameTimeoutNotifier = TimerNotifier.createStarted(play.getSettings().getters().getTimeout(), new GameTimeoutNotify());
        this.gameTimeoutNotifier.addObserver(this);
        gameLoop();
        if (!this.isTerminated) {
            findAndSetWinner();
        }
        end();
    }

    private final void gameLoop() {
        while (!isDone() && !isTerminated) {
            Player player = play.getHistory().getTurns().isEmpty() ? play.getInfo().getPlayers().getRandomPlayer() : getNextPlayer();
            doTurn(player);
            sendBoard();
        }
    }

    protected void doTurn(Player<T> player) {
        LockProxyResponse rs = new LockProxyResponse();

        ReentrantLock lock = new ReentrantLock();
        rs.setLock(lock);

        player.getConnector().get().isConnected(rs);

        if (!rs.getReal().isPresent()) {
            synchronized (lock) {
                try {
                    lock.wait(play.getSettings().getters().getTurnLimit());
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, ex.getMessage());
                }
            }
        }

        if (rs.getReal().isPresent()) {
            play.getHistory().getTurns().addLast(new Turn(player, play.getInfo().getBoard()));
            getMove(player);
        } else {
            handleDisconnected(player);
        }

    }

    public void handleDisconnected(Player p) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "one or two players disconnected. Terminating play...");
        Optional<Player<T>> winner = play.getInfo().getPlayers().filter((el) -> el.getId() != p.getId());
        play.getInfo().setWinner((winner));
        this.status = ExecutorStatus.Walkover;
        stop();
    }

    private void findAndSetWinner() {
        List<Player> winners = GameMaster.getWinner(play.getInfo().getBoard());
        play.getInfo().setWinner(winners.size() == 1 ? Optional.of(winners.get(0)) : Optional.empty());

    }

    public void stop() {
        logger.log(Level.SEVERE, "GAME TERMINATED");
        this.isTerminated = true;
    }

    public ExecutorStatus getStatus() {
        return this.status;
    }

    public void end() {

        play.onFinish();
        play.getInfo().setTotalTime((int) watch.elapsed(TimeUnit.MILLISECONDS));
        watch.stop();
        startEnd.onEnd();
        isEnded = true;
        status = evaluateStatus();
        logger.log(Level.SEVERE, "GAME OVER");
    }

    public ExecutorStatus evaluateStatus() {
        ExecutorStatus status = null;
        if (this.status == ExecutorStatus.Walkover) {
            return ExecutorStatus.Walkover;
        } else if (this.play.getInfo().getWinner().isPresent()) {
            status = ExecutorStatus.Winner;
        } else {
            status = ExecutorStatus.Remis;
        }
        return status;
    }

    private void getMove(Player<T> player) {
        Move move = null;

        LockProxyResponse<Point> response = new LockProxyResponse();
        Lock lock = new ReentrantLock();
        response.setLock(lock);

        player.connector().get().getMoveField(response);

        if (!response.getReal().isPresent()) {
            synchronized (lock) {
                try {
                    lock.wait(play.getSettings().getters().getTurnLimit());
                } catch (InterruptedException ex) {

                }
            }
        }
        if (response.getReal().isPresent()) {
            move = new Move(player, response.getReal().get());
            if (GameMaster.isValidMove(move, play.getInfo().getBoard())) {
                play.getInfo().getBoard().doMove(move);
                play.getHistory().getMoves().addLast(move);
            }
        } else {
            player.getConnector().get().onTurnTimeout();
        }

    }

    protected Player<T> getNextPlayer() {
        Turn lastTurn = play.getHistory().getTurns().getLast();
        return lastTurn.getQuarterback().equals(play.getInfo().getPlayers().firstPlayer().get())
                ? play.getInfo().getPlayers().secondPlayer().get() : play.getInfo().getPlayers().firstPlayer().get();
    }

    protected void sendBoard() {
        play.getInfo().getWatchers().stream().forEach((el) -> el.receiveBoard(play.getInfo().getBoard()));
    }

    public final boolean isDone() {
        return GameMaster.isDone(play.getInfo().getBoard()) || this.isTimedOut;
    }

    @Override
    public void onGameTimeout() {
        logger.log(Level.SEVERE, "GAME TIMEOUT");
        this.isTimedOut = true;
        this.isTerminated = true;
    }

}
