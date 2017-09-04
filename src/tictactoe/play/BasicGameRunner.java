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
class BasicGameRunner extends AbstractGameRunner implements GameTimeoutHandler {

    private TimerNotifier gameTimeoutNotifier;
    private boolean isTerminated = false;
    private boolean isTimedOut = false;
    private boolean isEnded = false;
    private GameRunnerStatus status = GameRunnerStatus.Unknown;
    private PlayStartEndCallbacks startEnd = new PlayStartEndCallbacks();
    private Stopwatch watch;

    public BasicGameRunner(PlayersPair players) {
        super(players);
    }

    protected Stopwatch getStopwatch() {
        return this.watch;
    }

    public final PlayStartEndCallbacks.ReducedVisiblity setCallbacks() {
        return this.startEnd.configure();
    }

    private final void gameLoop() throws PlayerDisconnectedException {
        sendBoard();
        while (!isDone() && !isTerminated) {
            Player player = super.getHistory().getTurns().isEmpty() ? super.getPlayers().getRandomPlayer() : super.getNextPlayer();
            doTurn(player);
            sendBoard();
        }
    }

    @Override
    public GameRunnerStatus getStatus() {
        return this.status;
    }

    protected void getMove(Player player) {
        Move move = null;

        LockProxyResponse<IntPoint> response = new LockProxyResponse();
        Lock lock = new ReentrantLock();
        response.setLock(lock);

        try {
            player.getMoveField(response);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }

        if (!response.getReal().isPresent()) {
            synchronized (lock) {
                try {
                    lock.wait(getSettings().getters().getTurnTimeLimitInMilis());
                } catch (InterruptedException ex) {

                }
            }
        }
        if (response.getReal().isPresent()) {
            move = new Move(player, response.getReal().get());
            if (GameMaster.isValidMove(move, getBoard())) {
                getBoard().doMove(move);
                getHistory().getMoves().addLast(move);
            }
        } else {
            try {
                player.onTurnTimeout();
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
            }
        }

    }

    protected void doTurn(Player player) throws PlayerDisconnectedException {
        LockProxyResponse<Boolean> rs = new LockProxyResponse();

        ReentrantLock lock = new ReentrantLock();
        rs.setLock(lock);

        try {
            player.isConnected((ProxyResponse<Boolean>) rs);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }

        if (!rs.getReal().isPresent()) {
            synchronized (lock) {
                try {
                    lock.wait(getSettings().getters().getTurnTimeLimitInMilis());
                } catch (InterruptedException ex) {
                    Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
                }
            }
        }

        if (rs.getReal().isPresent()) {
            getHistory().getTurns().addLast(new Turn(player, getBoard()));
            getMove(player);
        } else {
            throw new PlayerDisconnectedException(player);
        }

    }

    private final void sendBoard() {
        super.getPlayers().getFirstPlayer().get().receiveBoard(super.getBoard());
        super.getPlayers().getSecondPlayer().get().receiveBoard(super.getBoard());
    }

    private final void end() {
        watch.stop();
        startEnd.onEnd();
        isEnded = true;
        status = evaluateStatus();
        Logger.getGlobal().log(Level.SEVERE, "GAME OVER");
    }

    private final GameRunnerStatus evaluateStatus() {
        return (status != GameRunnerStatus.Running) ? status : GameRunnerStatus.Done;
    }

    @Override
    public void onGameTimeout() {
        Logger.getGlobal().log(Level.SEVERE, "GAME TIMEOUT");
        this.isTimedOut = true;
        this.isTerminated = true;
    }

    @Override
    public final void start() throws PlayerDisconnectedException {
        if (!super.getPlayers().isPair()) {
            throw new NotAllPlayersPresentException();
        }
        super.getPlayers().assignBoardFieldsMarks();
        Logger.getGlobal().log(Level.SEVERE, MessageFormat.format("GAME START.\nSettings: gameTimeLimit: {0} miliseconds, turnTimeLimit: {1} miliseconds",
                super.getSettings().getters().getGameTimeLimitInMilis(), super.getSettings().getters().getTurnTimeLimitInMilis()));
        this.status = GameRunnerStatus.Running;
        watch = Stopwatch.createStarted();
        startEnd.onStart();
        this.gameTimeoutNotifier = TimerNotifier.createStarted(super.getSettings().getters().getGameTimeLimitInMilis(), new GameTimeoutNotify());
        this.gameTimeoutNotifier.addObserver(this);
        gameLoop();
        end();
    }

    @Override
    public void interrupt() {
        Logger.getGlobal().log(Level.SEVERE, "GAME TERMINATED");
        this.isTerminated = true;

    }

    @Override
    public boolean isRunning() {
        return !isTerminated || !isTimedOut || !isEnded;
    }

    @Override
    public final boolean isDone() {
        return GameMaster.isDone(super.getBoard()) || this.isTimedOut;
    }

}
