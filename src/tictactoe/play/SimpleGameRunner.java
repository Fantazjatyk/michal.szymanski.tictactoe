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
package tictactoe.play;

import com.google.common.base.Stopwatch;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.control.GameTimeoutNotify;
import tictactoe.control.TimerNotifier;
import tictactoe.exceptions.NotAllPlayersPresentException;
import tictactoe.model.IntPoint;
import tictactoe.model.Move;
import tictactoe.model.Player;
import tictactoe.model.Turn;
import tictactoe.transport.GameTimeoutHandler;
import tictactoe.transport.LockProxyResponse;
import tictactoe.transport.PlayerDisconnectedHandler;
import tictactoe.transport.ProxyResponse;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class SimpleGameRunner implements GameTimeoutHandler, PlayerDisconnectedHandler, GameRunner {

    private Game play;
    private TimerNotifier gameTimeoutNotifier;
    private Stopwatch watch;
    private boolean isTerminated = false;
    private boolean isTimedOut = false;
    private boolean isEnded = false;
    private GameRunnerStatus status = GameRunnerStatus.Started;
    private PlayStartEndCallbacks startEnd = new PlayStartEndCallbacks();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public SimpleGameRunner(Game play) {
        this.play = play;
    }

    public PlayStartEndCallbacks.ReducedVisiblity setCallbacks() {
        return this.startEnd.get();
    }

    private final void gameLoop() {
        sendBoard();
        while (!isDone() && !isTerminated) {
            Player player = play.getHistory().getTurns().isEmpty() ? play.getInfo().getPlayers().getRandomPlayer() : getNextPlayer();
            doTurn(player);
            sendBoard();
        }
    }

    protected void doTurn(Player player) {
        LockProxyResponse<Boolean> rs = new LockProxyResponse();

        ReentrantLock lock = new ReentrantLock();
        rs.setLock(lock);

        try {
            player.isConnected((ProxyResponse<Boolean>) rs);
        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, ex.getMessage());
        }

        if (!rs.getReal().isPresent()) {
            synchronized (lock) {
                try {
                    lock.wait(play.getSettings().getters().getTurnTimeLimitInMilis());
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
        Optional<Player> winner = play.getInfo().getPlayers().filter((el) -> el.getId() != p.getId());
        play.getInfo().setWinner((winner));
        this.status = GameRunnerStatus.Walkover;
        interrupt();
    }

    public void resign(Player p) {
        Optional<Player> winner = play.getInfo().getPlayers().filter((el) -> el.getId() != p.getId());
        play.getInfo().setWinner((winner));
        this.status = GameRunnerStatus.Walkover;
        interrupt();
    }

    private void findAndSetWinner() {
        List<Player> winners = GameMaster.getWinner(play.getInfo().getBoard());
        play.getInfo().setWinner(winners.size() == 1 ? Optional.of(winners.get(0)) : Optional.empty());
    }

    public GameRunnerStatus getStatus() {
        return this.status;
    }

    private GameRunnerStatus evaluateStatus() {
        GameRunnerStatus status = null;
        if (this.status == GameRunnerStatus.Walkover) {
            return GameRunnerStatus.Walkover;
        } else if (this.play.getInfo().getWinner().isPresent()) {
            status = GameRunnerStatus.Winner;
        } else {
            status = GameRunnerStatus.Remis;
        }
        return status;
    }

    private void getMove(Player player) {
        Move move = null;

        LockProxyResponse<IntPoint> response = new LockProxyResponse();
        Lock lock = new ReentrantLock();
        response.setLock(lock);

        try {
            player.getMoveField(response);
        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, ex.getMessage());
        }

        if (!response.getReal().isPresent()) {
            synchronized (lock) {
                try {
                    lock.wait(play.getSettings().getters().getTurnTimeLimitInMilis());
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
            try {
                player.onTurnTimeout();
            } catch (Exception ex) {
                this.logger.log(Level.SEVERE, ex.getMessage());
            }
        }

    }

    private Player getNextPlayer() {
        Turn lastTurn = play.getHistory().getTurns().getLast();
        return lastTurn.getQuarterback().equals(play.getInfo().getPlayers().getFirstPlayer().get())
                ? play.getInfo().getPlayers().getSecondPlayer().get() : play.getInfo().getPlayers().getFirstPlayer().get();
    }

    private void sendBoard() {
        this.play.getInfo().getPlayers().getFirstPlayer().get().receiveBoard(this.play.getInfo().getBoard());
        this.play.getInfo().getPlayers().getSecondPlayer().get().receiveBoard(this.play.getInfo().getBoard());
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

    @Override
    public void start() {
        if (!play.getInfo().getPlayers().isPair()) {
            throw new NotAllPlayersPresentException();
        }
        logger.log(Level.SEVERE, MessageFormat.format("GAME START.\nSettings: gameTimeLimit: {0} miliseconds, turnTimeLimit: {1} miliseconds",
                play.getSettings().getters().getGameTimeLimitInMilis(), play.getSettings().getters().getTurnTimeLimitInMilis()));
        this.status = GameRunnerStatus.Running;
        watch = Stopwatch.createStarted();
        play.onStart();
        startEnd.onStart();
        this.gameTimeoutNotifier = TimerNotifier.createStarted(play.getSettings().getters().getGameTimeLimitInMilis(), new GameTimeoutNotify());
        this.gameTimeoutNotifier.addObserver(this);
        gameLoop();
        if (!this.isTerminated) {
            findAndSetWinner();
        }
        end();
    }

    @Override
    public void interrupt() {
        logger.log(Level.SEVERE, "GAME TERMINATED");
        this.isTerminated = true;

    }

    @Override
    public boolean isRunning() {
        return !isTerminated || !isTimedOut || !isEnded;
    }

    private void end() {
        play.onFinish();
        play.getInfo().setTotalTime((int) watch.elapsed(TimeUnit.MILLISECONDS));
        watch.stop();
        startEnd.onEnd();
        isEnded = true;
        status = evaluateStatus();
        logger.log(Level.SEVERE, "GAME OVER");
    }

}
