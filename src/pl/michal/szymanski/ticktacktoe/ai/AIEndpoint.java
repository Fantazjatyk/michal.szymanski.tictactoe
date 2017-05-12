/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.ai;

import java.util.ArrayList;
import java.util.List;
import pl.michal.szymanski.ticktacktoe.ai.Difficulty;
import pl.michal.szymanski.ticktacktoe.core.PlayFlow;
import pl.michal.szymanski.ticktacktoe.core.Board;
import pl.michal.szymanski.ticktacktoe.core.Move;
import pl.michal.szymanski.ticktacktoe.core.PlayInfo;
import pl.michal.szymanski.ticktacktoe.core.PlaySettings;
import pl.michal.szymanski.ticktacktoe.core.Point;
import pl.michal.szymanski.ticktacktoe.transport.ProxyResponse;
import pl.michal.szymanski.ticktacktoe.transport.ProxyResponseSetter;
import pl.michal.szymanski.ticktacktoe.transport.SingleplayerParticipant;
import pl.michal.szymanski.ticktacktoe.transport.SingleplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class AIEndpoint implements SingleplayerParticipant {

    private Difficulty difficulty;
    private List uncheckedSolutions = new ArrayList();
    private AIStrategy strategy;

    public AIEndpoint(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void receiveBoard(Board board) {
        this.uncheckedSolutions.addAll(board.getDiagonals());
        this.uncheckedSolutions.addAll(board.getColumns());
        this.uncheckedSolutions.addAll(board.getRows());
    }

    private int numberOfOnlyMyFields() {
        return 0;
    }

    private void decideWhichStrategy() {

    }

    @Override
    public void onTurnTimeout() {
    }

    @Override
    public void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings) {
    }

    @Override
    public void getMoveField(ProxyResponseSetter<Point> proxy) {
    }

}
