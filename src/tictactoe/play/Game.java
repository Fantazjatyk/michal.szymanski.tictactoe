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

import tictactoe.model.GameResult;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.exceptions.PlayerDisconnectedException;
import tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Game implements GameRunner {

    private GameSettings settings = new GameSettings();
    private String id;
    private PlayersPair players = new PlayersPair();
    private BasicGameRunner runner;
    private Optional<GameResult> result = Optional.empty();

    public Game(BasicGameRunner runner) {
        this.runner = runner;
    }

    public Game() {
        this.id = UUID.randomUUID().toString() + new Random().nextInt(1000);
        this.runner = new BasicGameRunner(players);
    }

    public String getId() {
        return id;
    }

    public final GameSettings.PlaySettingsSetters configure() {
        return this.settings.setters();
    }

    public final Optional<GameResult> getResult() {
        if (!this.result.isPresent()) {
            this.result = Optional.of(GameResultSimpleFactory.createGameResult(this, runner));
        }
        return this.result;
    }

    public void join(Player player) {
        if (!this.players.getFirstPlayer().isPresent()) {
            this.players.firstPlayer(player);
        } else if (!this.players.getSecondPlayer().isPresent()) {
            this.players.secondPlayer(player);
        }

    }

    public void kickPlayer(Player p) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Throwing player " + p.getId() + ". Terminating play...");
        p.disgualify();
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
            this.kickPlayer(p);
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

    protected final BasicGameRunner getRunner() {
        return this.runner;
    }

    protected final PlayersPair getPlayers() {
        return this.players;
    }

    protected final GameSettings getSettings() {
        return this.settings;
    }

}
