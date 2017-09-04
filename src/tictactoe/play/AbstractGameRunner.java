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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.control.GameTimeoutNotify;
import tictactoe.control.TimerNotifier;
import tictactoe.exceptions.NotAllPlayersPresentException;
import tictactoe.exceptions.PlayerDisconnectedException;
import tictactoe.model.Board;
import tictactoe.model.IntPoint;
import tictactoe.model.Move;
import tictactoe.model.Player;
import tictactoe.model.Turn;
import tictactoe.play.GameRunner.GameRunnerStatus;
import tictactoe.transport.GameTimeoutHandler;
import tictactoe.transport.LockProxyResponse;
import tictactoe.transport.ProxyResponse;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
abstract class AbstractGameRunner implements GameTimeoutHandler, GameRunnerCallbackable {

    private TimerNotifier gameTimeoutNotifier;
    private Stopwatch watch;
    private PlaySettings settings;
    private PlayHistory history = new PlayHistory();
    private PlayersPair players;
    private boolean isTerminated = false;
    private boolean isTimedOut = false;
    private boolean isEnded = false;
    private Board board = new Board(3);
    private GameRunnerStatus status = GameRunnerStatus.Unknown;
    private PlayStartEndCallbacks startEnd = new PlayStartEndCallbacks();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public AbstractGameRunner(PlayersPair players) {
        this.board = board;
        this.players = players;
    }

    public PlayStartEndCallbacks.ReducedVisiblity setCallbacks() {
        return this.startEnd.get();
    }

    private final void gameLoop() throws PlayerDisconnectedException {
        sendBoard();
        while (!isDone() && !isTerminated) {
            Player player = history.getTurns().isEmpty() ? players.getRandomPlayer() : getNextPlayer();
            doTurn(player);
            sendBoard();
        }
    }

    private void doTurn(Player player) throws PlayerDisconnectedException {
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
                    lock.wait(settings.getters().getTurnTimeLimitInMilis());
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, ex.getMessage());
                }
            }
        }

        if (rs.getReal().isPresent()) {
            history.getTurns().addLast(new Turn(player, board));
            getMove(player);
        } else {
            throw new PlayerDisconnectedException(player);
        }

    }

    Board getBoard() {
        return this.board;
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
                    lock.wait(settings.getters().getTurnTimeLimitInMilis());
                } catch (InterruptedException ex) {

                }
            }
        }
        if (response.getReal().isPresent()) {
            move = new Move(player, response.getReal().get());
            if (GameMaster.isValidMove(move, board)) {
                board.doMove(move);
                history.getMoves().addLast(move);
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
        Turn lastTurn = history.getTurns().getLast();
        return lastTurn.getQuarterback().equals(players.getFirstPlayer().get())
                ? players.getSecondPlayer().get() : players.getFirstPlayer().get();
    }

    private void sendBoard() {
        this.players.getFirstPlayer().get().receiveBoard(board);
        this.players.getSecondPlayer().get().receiveBoard(board);
    }

    private void end() {
        watch.stop();
        startEnd.onEnd();
        isEnded = true;
        status = evaluateStatus();
        logger.log(Level.SEVERE, "GAME OVER");
    }

    private GameRunnerStatus evaluateStatus() {
        return (status != GameRunnerStatus.Running) ? status : GameRunnerStatus.Done;
    }

    @Override
    public void onGameTimeout() {
        logger.log(Level.SEVERE, "GAME TIMEOUT");
        this.isTimedOut = true;
        this.isTerminated = true;
    }

    @Override
    public void start() throws PlayerDisconnectedException {
        if (!players.isPair()) {
            throw new NotAllPlayersPresentException();
        }
        players.assignBoardFieldsMarks();
        logger.log(Level.SEVERE, MessageFormat.format("GAME START.\nSettings: gameTimeLimit: {0} miliseconds, turnTimeLimit: {1} miliseconds",
                settings.getters().getGameTimeLimitInMilis(), settings.getters().getTurnTimeLimitInMilis()));
        this.status = GameRunnerStatus.Running;
        watch = Stopwatch.createStarted();
        startEnd.onStart();
        this.gameTimeoutNotifier = TimerNotifier.createStarted(settings.getters().getGameTimeLimitInMilis(), new GameTimeoutNotify());
        this.gameTimeoutNotifier.addObserver(this);
        gameLoop();
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

    @Override
    public GameRunnerStatus getStatus() {
        return this.status;
    }

    @Override
    public final boolean isDone() {
        return GameMaster.isDone(board) || this.isTimedOut;
    }

    @Override
    public void setOnEndCallback(Runnable r) {
    }

    @Override
    public void setOnStartCallback(Runnable r) {
    }

}
