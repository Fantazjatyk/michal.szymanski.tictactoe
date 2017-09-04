/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    PlaySettings getSettings() {
        return this.settings;
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
