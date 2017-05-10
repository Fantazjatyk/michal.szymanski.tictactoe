/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.Optional;
import pl.michal.szymanski.ticktacktoe.transport.MultiplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class MultiplayerPlay extends Play<MultiplayerParticipant> {

    @Override
    public boolean join(MultiplayerParticipant t, String username) {
        if (!super.players().firstPlayer().isPresent()) {
            super.players().firstPlayer(t, username);
            return true;
        } else if (!super.players().secondPlayer().isPresent()) {
            super.players().secondPlayer(t, username);
            return true;
        }
        return false;
    }

}
