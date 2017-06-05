/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.play;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import pl.michal.szymanski.tictactoe.model.Player;
import pl.michal.szymanski.tictactoe.play.GameMaster;
import pl.michal.szymanski.tictactoe.play.PlayHistory;
import pl.michal.szymanski.tictactoe.play.PlayInfo;
import pl.michal.szymanski.tictactoe.play.PlaySettings;
import pl.michal.szymanski.tictactoe.transport.GameWatcher;
import pl.michal.szymanski.tictactoe.transport.Participant;
import pl.michal.szymanski.tictactoe.transport.MultiplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Play<T extends Participant> {

    private PlaySettings settings = new PlaySettings();
    private PlayHistory history = new PlayHistory();
    private PlayInfo<T> info = new PlayInfo();

    public PlayInfo<T> getInfo() {
        return info;
    }

    public PlayHistory getHistory() {
        return history;
    }

    protected void onStart() {
        info.getWatchers().add((GameWatcher) info.getPlayers().firstPlayer().get().connector().get());
        info.getWatchers().add((GameWatcher) info.getPlayers().secondPlayer().get().connector().get());
        info.getPlayers().assignBoardFieldsMarks();
    }

    public PlaySettings.PlaySettingsSetters settings() {
        return this.settings.setters();
    }

    public PlaySettings getSettings() {
        return this.settings;
    }

    public void join(T c) {
        Player<T> p = new Player();
        p.setConnector(c);
        join(p);
    }

    public void join(T c, String id) {
        Player<T> p = new Player();
        p.setId(id);
        p.setConnector(c);
        join(p);
    }

    private void join(Player player) {
        if (!this.getInfo().getPlayers().firstPlayer().isPresent()) {
            this.getInfo().getPlayers().firstPlayer(player);
        } else if (!this.getInfo().getPlayers().secondPlayer().isPresent()) {
            this.getInfo().getPlayers().secondPlayer(player);
        }

    }

    public void notifyWatchers() {
        info.getWatchers().stream().forEach((el) -> el.onGameEnd(info, settings.getters()));
    }

    public void registerWatcher(GameWatcher watcher) {
        info.getWatchers().add(watcher);
    }

    public void onFinish() {

        this.notifyWatchers();
    }

}
