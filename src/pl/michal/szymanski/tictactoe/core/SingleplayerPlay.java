/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.core;

import java.util.Optional;
import pl.michal.szymanski.tictactoe.ai.Difficulty;
import pl.michal.szymanski.tictactoe.ai.AIEndpoint;
import pl.michal.szymanski.tictactoe.transport.ProxyResponse;
import pl.michal.szymanski.tictactoe.transport.SingleplayerParticipant;

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
    public void join(SingleplayerParticipant t, String username) {

        if (!super.getInfo().getPlayers().firstPlayer().isPresent()) {
            super.getInfo().getPlayers().firstPlayer(new Player(username, t));
        }

        if (super.getInfo().getPlayers().isPair() && super.getPlaySettings().getters().getBeginOnAllPlayersJoined() && super.getInfo().getPlayers().areEachHaveConnector()) {
            super.begin();
        }

    }

    public Move getAIMove() {
        Player<SingleplayerParticipant> ai = super.getInfo().getPlayers().secondPlayer().get();
        ProxyResponse<Point> response = new ProxyResponse();
        ai.connector().get().getMoveField(response.getSetters());
        Point field = response.getGetters().getReal().get();
        return new Move(ai, field);
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.getInfo().getPlayers().secondPlayer(new Player("Computer", new AIEndpoint(difficulty)));
    }

    @Override
    public void join(String username) {
        if (!super.getInfo().getPlayers().firstPlayer().isPresent()) {
            super.getInfo().getPlayers().firstPlayer(new Player(username));

        }

        if (super.getInfo().getPlayers().isPair() && super.getPlaySettings().getters().getBeginOnAllPlayersJoined() && super.getInfo().getPlayers().areEachHaveConnector()) {
            super.begin();
        }
    }

}
