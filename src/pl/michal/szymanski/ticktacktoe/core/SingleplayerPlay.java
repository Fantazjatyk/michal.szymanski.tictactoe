/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.Optional;
import pl.michal.szymanski.ticktacktoe.ai.Difficulty;
import pl.michal.szymanski.ticktacktoe.core.model.Board;
import pl.michal.szymanski.ticktacktoe.core.model.Move;
import pl.michal.szymanski.ticktacktoe.ai.AIEndpoint;
import pl.michal.szymanski.ticktacktoe.core.model.Player;
import pl.michal.szymanski.ticktacktoe.core.model.Point;
import pl.michal.szymanski.ticktacktoe.transport.SingleplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class SingleplayerPlay extends Play<SingleplayerParticipant> {

    private Difficulty difficulty = Difficulty.EASY;

    public void difficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean join(SingleplayerParticipant t, String username) {

        if (!super.players().firstPlayer().isPresent()) {
            super.players().firstPlayer(t, username);
            return true;
        }
        return false;

    }

    public Move getAIMove() {
        Player ai = super.players().secondPlayer().get();
        Point field = ai.connector().getMoveField();
        return new Move(ai, field);
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.players().secondPlayer(new AIEndpoint(difficulty), "Komputer");
    }
}
