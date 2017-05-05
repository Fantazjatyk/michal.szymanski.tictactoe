/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.ai;

import pl.michal.szymanski.ticktacktoe.ai.Difficulty;
import pl.michal.szymanski.ticktacktoe.core.model.Board;
import pl.michal.szymanski.ticktacktoe.core.model.Move;
import pl.michal.szymanski.ticktacktoe.transport.SingleplayerConnector;
import pl.michal.szymanski.ticktacktoe.transport.SingleplayerConnector;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class AIEndpoint implements SingleplayerConnector {

    private Difficulty difficulty;

    public AIEndpoint(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public Move getMove() {
        return null;
    }

    @Override
    public void receiveBoard(Board board) {
    }

}
