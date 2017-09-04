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

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.exceptions.PlayerDisconnectedException;
import tictactoe.model.Player;
import tictactoe.play.GameResult.GameResultBuilder;
import tictactoe.play.GameResult.GameResultStatus;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Game implements GameRunner {

    private PlaySettings settings = new PlaySettings();
    private PlayHistory history = new PlayHistory();
    private PlayInfo info = new PlayInfo();
    private GameRunner runner;

    public Game(GameRunner runner) {
        this.runner = runner;
    }

    public Game() {
        this.runner = new SimpleGameRunner(this);
    }

    public PlayInfo getInfo() {
        return info;
    }

    public PlayHistory getHistory() {
        return history;
    }

    public PlaySettings.PlaySettingsSetters settings() {
        return this.settings.setters();
    }

    void onStart() {
        info.getPlayers().assignBoardFieldsMarks();
    }

    void onFinish() {
        this.info.getPlayers().getFirstPlayer().get().onGameEnd(info, settings.getters());
        this.info.getPlayers().getSecondPlayer().get().onGameEnd(info, settings.getters());
    }

    public GameResult getResult() {
        GameResultStatus status = null;
        if (getStatus() == GameRunnerStatus.Done && getInfo().getWinner().isPresent()) {
            status = GameResult.GameResultStatus.Winner;
        } else if (getStatus() == GameRunnerStatus.Interrupted && getInfo().getWinner().isPresent()) {
            status = GameResult.GameResultStatus.Walkover;
        }
        GameResultBuilder b = new GameResult.GameResultBuilder();
        b.setStatus(status);
        b.setWinner(getInfo().getWinner());
        return b.build();
    }

    public void join(Player player) {
        if (!this.getInfo().getPlayers().getFirstPlayer().isPresent()) {
            this.getInfo().getPlayers().firstPlayer(player);
        } else if (!this.getInfo().getPlayers().getSecondPlayer().isPresent()) {
            this.getInfo().getPlayers().secondPlayer(player);
        }

    }

    public void throwPlayer(Player p) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Throwing player " + p.getId() + ". Terminating play...");
        Optional<Player> winner = getInfo().getPlayers().filter((el) -> el.getId() != p.getId());
        getInfo().setWinner((winner));
        interrupt();
    }

    @Override
    public boolean isRunning() {
        return runner.isRunning();
    }

    @Override
    public void start() {
        try {
            this.runner.start();
        } catch (PlayerDisconnectedException ex) {
            Player p = ex.getPlayer();
            this.throwPlayer(p);
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {
        this.runner.interrupt();
    }

    @Override
    public boolean isDone() {
        return this.runner.isDone();
    }

    @Override
    public GameRunnerStatus getStatus() {
        return this.runner.getStatus();
    }

}
