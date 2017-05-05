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
import pl.michal.szymanski.ticktacktoe.transport.SingleplayerConnector;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class SingleplayerPlay extends Play<SingleplayerConnector> {

    private Difficulty difficulty = Difficulty.EASY;

    public void difficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean join(SingleplayerConnector t) {

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
    protected boolean isDone() {
        return false;
    }

    @Override
    protected void start() {
        super.players().secondPlayer(new AIEndpoint(difficulty));
    }

    @Override
    protected void finish() {
    }

}
