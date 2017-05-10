/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.Optional;
import pl.michal.szymanski.ticktacktoe.ai.Difficulty;
import pl.michal.szymanski.ticktacktoe.ai.AIEndpoint;
import pl.michal.szymanski.ticktacktoe.transport.SingleplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class SingleplayerPlay extends PlayFlow<SingleplayerParticipant> {

    private Difficulty difficulty = Difficulty.EASY;

    public void difficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean join(SingleplayerParticipant t, String username) {

        if (!super.getInfo().getPlayers().firstPlayer().isPresent()) {
            super.getInfo().getPlayers().firstPlayer(t, username);
            return true;
        }
        return false;

    }

    public Move getAIMove() {
        Player ai = super.getInfo().getPlayers().secondPlayer().get();
        Point field = ai.connector().getMoveField();
        return new Move(ai, field);
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.getInfo().getPlayers().secondPlayer(new AIEndpoint(difficulty), "Komputer");
    }

}
