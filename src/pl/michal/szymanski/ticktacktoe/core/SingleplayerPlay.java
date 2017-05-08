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
    public boolean join(SingleplayerParticipant t) {

        if (!super.players().firstPlayer().isPresent()) {
            super.players().firstPlayer(t);
            return true;
        }
        return false;

    }

    public Move getAIMove() {
        return super.players().secondPlayer().get().connector().getMove();
    }

    @Override
    protected void onStart() {
        super.players().secondPlayer(new AIEndpoint(difficulty));
    }

    @Override
    protected void onFinish() {
    }

}
