/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.core;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import pl.michal.szymanski.tictactoe.transport.GameWatcher;
import pl.michal.szymanski.tictactoe.transport.Participant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class Play<T extends Participant> {

    private PlaySettings settings = new PlaySettings();
    private PlayHistory history = new PlayHistory();
    private PlayInfo<T> info = new PlayInfo();

    public PlayInfo<T> getInfo() {
        return info;
    }

    protected PlayHistory getHistory() {
        return history;
    }

    protected PlaySettings getPlaySettings() {
        return this.settings;
    }

    protected void onStart() {
        history.getWatchers().add((GameWatcher) info.getPlayers().firstPlayer().get().connector());
        history.getWatchers().add((GameWatcher) info.getPlayers().secondPlayer().get().connector());
        assignBoardFieldsMarks();
    }

    protected boolean isDone() {
        return GameMaster.isDone(info.getBoard());
    }

    protected abstract void doTurn(Player<T> player);

    public abstract void join(T t, String username);

    public PlaySettings.PlaySettingsSetters settings() {
        return this.settings.setters();
    }

    protected Player<T> getNextPlayer() {
        Turn lastTurn = history.getTurns().getLast();
        return lastTurn.getQuarterback().equals(info.getPlayers().firstPlayer().get()) ? info.getPlayers().secondPlayer().get() : info.getPlayers().firstPlayer().get();
    }

    protected Player<T> getRandomPlayer() {
        boolean isThatFirst = new Random().nextBoolean();
        return isThatFirst ? info.getPlayers().firstPlayer().get() : info.getPlayers().secondPlayer().get();
    }

    public void watch(GameWatcher watcher) {
        history.getWatchers().add(watcher);
    }

    protected void assignBoardFieldsMarks() {

        boolean random = new Random().nextBoolean();

        if (random) {
            info.getPlayers().firstPlayer().get().setBoardFieldType(BoardFieldType.XMark);
            info.getPlayers().secondPlayer().get().setBoardFieldType(BoardFieldType.OMark);
        } else {
            info.getPlayers().firstPlayer().get().setBoardFieldType(BoardFieldType.OMark);
            info.getPlayers().secondPlayer().get().setBoardFieldType(BoardFieldType.XMark);
        }

    }

    protected void sendBoard() {
        history.getWatchers().stream().forEach((el) -> el.receiveBoard(info.getBoard()));
    }

    public void begin() {
        onStart();
        while (!isDone()) {
            Player<T> player = history.getTurns().isEmpty() ? getRandomPlayer() : getNextPlayer();
            doTurn(player);
            sendBoard();
        }
        onFinish();
    }

    protected void onFinish() {
        List<Player> winners = GameMaster.getWinner(info.getBoard());
        info.setWinner(winners.size() == 1 ? Optional.of(winners.get(0)) : Optional.empty());
    }

}
