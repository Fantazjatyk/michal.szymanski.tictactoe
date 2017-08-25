/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.play;

import pl.michal.szymanski.tictactoe.play.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;
import pl.michal.szymanski.tictactoe.model.BoardFieldType;
import pl.michal.szymanski.tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayersPair {

    private Optional<Player> firstPlayer = Optional.empty();
    private Optional<Player> secondPlayer = Optional.empty();

    public Optional<Player> getFirstPlayer() {
        return firstPlayer;
    }

    public Optional<Player> getSecondPlayer() {
        return secondPlayer;
    }

    public boolean isPair() {
        return this.firstPlayer.isPresent() && this.secondPlayer.isPresent();
    }

    public Optional<Player> getPlayer(String username) {
        return firstPlayer.isPresent() && firstPlayer.get().getUsername().equals(username)
                ? firstPlayer : (secondPlayer.isPresent() && secondPlayer.get().getUsername().equals(username) ? secondPlayer : Optional.empty());
    }

    public Optional<Player> getOMarkPlayer() {
        return getMarkedPlayer(BoardFieldType.OMark);
    }

    public Optional<Player> getMarkedPlayer(BoardFieldType type) {
        return firstPlayer.isPresent() && firstPlayer.get().getType().equals(type) ? firstPlayer
                : (secondPlayer.isPresent() && secondPlayer.get().getType().equals(type) ? secondPlayer : Optional.empty());
    }

    public Optional<Player> getXMarkPlayer() {
        return getMarkedPlayer(BoardFieldType.XMark);
    }

    public Player getRandomPlayer() {
        boolean isThatFirst = new Random().nextBoolean();
        return isThatFirst ? getFirstPlayer().get() : getSecondPlayer().get();
    }

    public void assignBoardFieldsMarks() {

        boolean random = new Random().nextBoolean();

        if (random) {
            getFirstPlayer().get().setType(BoardFieldType.XMark);
            getSecondPlayer().get().setType(BoardFieldType.OMark);
        } else {
            getFirstPlayer().get().setType(BoardFieldType.OMark);
            getSecondPlayer().get().setType(BoardFieldType.XMark);
        }

    }

    public Optional<Player> filter(Predicate<Player> pr) {
        Optional<Player> result = Optional.empty();

        if (pr.test(this.firstPlayer.get())) {
            result = this.firstPlayer;
        } else if (pr.test(this.secondPlayer.get())) {
            result = this.secondPlayer;
        }
        return result;
    }

    protected void firstPlayer(Player player) {
        this.firstPlayer = Optional.ofNullable(player);
    }

    protected void secondPlayer(Player player) {
        this.secondPlayer = Optional.ofNullable(player);
    }

}
