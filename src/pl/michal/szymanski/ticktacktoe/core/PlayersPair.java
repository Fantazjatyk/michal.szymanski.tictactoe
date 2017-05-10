/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import pl.michal.szymanski.ticktacktoe.core.model.BoardFieldType;
import pl.michal.szymanski.ticktacktoe.core.model.Player;
import pl.michal.szymanski.ticktacktoe.transport.Participant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayersPair<T extends Participant> {

    private Optional<Player<T>> firstPlayer = Optional.empty();
    private Optional<Player<T>> secondPlayer = Optional.empty();

    protected Optional<Player<T>> firstPlayer() {
        return firstPlayer;
    }

    protected Optional<Player<T>> secondPlayer() {
        return secondPlayer;
    }

    public Optional<Player<T>> getXMarkPlayer() {
        return getMarkedPlayer(BoardFieldType.XMark);
    }

    public Optional<Player<T>> getOMarkPlayer() {
        return getMarkedPlayer(BoardFieldType.OMark);
    }

    public Optional<Player<T>> getPlayer(String username) {
        return firstPlayer.isPresent() && firstPlayer.get().getUsername().equals(username)
                ? firstPlayer : (secondPlayer.isPresent() && secondPlayer.get().getUsername().equals(username) ? secondPlayer : Optional.empty());
    }

    public Optional<Player<T>> getMarkedPlayer(BoardFieldType type) {
        return firstPlayer.isPresent() && firstPlayer.get().getBoardFieldType().equals(type) ? firstPlayer
                : (secondPlayer.isPresent() && secondPlayer.get().getBoardFieldType().equals(type) ? secondPlayer : Optional.empty());
    }

    protected void firstPlayer(T firstPlayer, String username) {
        this.firstPlayer = Optional.ofNullable(new Player(firstPlayer, username));
    }

    protected void secondPlayer(T secondPlayer, String username) {
        this.secondPlayer = Optional.ofNullable(new Player(secondPlayer, username));
    }
}
