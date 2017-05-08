/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.Optional;
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

    protected void firstPlayer(T firstPlayer) {
        this.firstPlayer = Optional.ofNullable(new Player(firstPlayer));
    }

    protected void secondPlayer(T secondPlayer) {
        this.secondPlayer = Optional.ofNullable(new Player(secondPlayer));
    }
}
