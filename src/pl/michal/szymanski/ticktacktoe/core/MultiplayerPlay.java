/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.Optional;
import pl.michal.szymanski.ticktacktoe.transport.MultiplayerConnector;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class MultiplayerPlay extends Play<MultiplayerConnector> {

    @Override
    public boolean join(MultiplayerConnector t) {
        if (!super.players().firstPlayer().isPresent()) {
            super.players().firstPlayer(t);
            return true;
        } else if (!super.players().secondPlayer().isPresent()) {
            super.players().secondPlayer(t);
            return true;
        }
        return false;
    }

    protected void start() {
    }

    @Override
    protected void finish() {
    }

    @Override
    protected boolean isDone() {
        boolean superTest = super.isDone();
        return superTest;
    }

}
