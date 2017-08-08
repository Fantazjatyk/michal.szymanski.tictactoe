/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.play.v2;

import pl.michal.szymanski.tictactoe.play.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import pl.michal.szymanski.tictactoe.model.v2.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Play {

    private PlaySettings settings = new PlaySettings();
    private PlayHistory history = new PlayHistory();
    private PlayInfo info = new PlayInfo();

    public PlayInfo getInfo() {
        return info;
    }

    public PlayHistory getHistory() {
        return history;
    }

    public PlaySettings.PlaySettingsSetters settings() {
        return this.settings.setters();
    }

    public void onStart() {
        info.getPlayers().assignBoardFieldsMarks();
    }

    public void onFinish(){
        this.info.getPlayers().getFirstPlayer().get().onGameEnd(info, settings.getters());
        this.info.getPlayers().getSecondPlayer().get().onGameEnd(info, settings.getters());
    }
    public PlaySettings getSettings() {
        return this.settings;
    }

    public void join(Player player) {
        if (!this.getInfo().getPlayers().getFirstPlayer().isPresent()) {
            this.getInfo().getPlayers().firstPlayer(player);
        } else if (!this.getInfo().getPlayers().getSecondPlayer().isPresent()) {
            this.getInfo().getPlayers().secondPlayer(player);
        }

    }

}
