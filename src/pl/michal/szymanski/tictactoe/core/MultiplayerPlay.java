/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.core;

import java.util.Optional;
import pl.michal.szymanski.tictactoe.transport.MultiplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class MultiplayerPlay extends PlayFlow<MultiplayerParticipant> {

    @Override
    public void join(MultiplayerParticipant t, String username) {
        if (!super.getInfo().getPlayers().firstPlayer().isPresent()) {
            super.getInfo().getPlayers().firstPlayer(t, username);
        } else if (!super.getInfo().getPlayers().secondPlayer().isPresent()) {
            super.getInfo().getPlayers().secondPlayer(t, username);
        }

        if (super.getInfo().getPlayers().isPair()  && super.getPlaySettings().getters().getBeginOnAllPlayersJoined()) {
            super.begin();
        }

    }

}
