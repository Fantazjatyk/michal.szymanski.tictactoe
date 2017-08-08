/*
 * The MIT License
 *
 * Copyright 2017 Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.michal.szymanski.tictactoe.ai.v2;

import pl.michal.szymanski.tictactoe.ai.*;
import java.util.UUID;
import pl.michal.szymanski.ai.tictactoe.ContextAwareAI;
import pl.michal.szymanski.ai.tictactoe.model.BoardReader;
import pl.michal.szymanski.tictactoe.model.v2.Board;
import pl.michal.szymanski.tictactoe.model.v2.BoardField;
import pl.michal.szymanski.tictactoe.model.v2.GameResult;
import pl.michal.szymanski.tictactoe.model.v2.IntPoint;
import pl.michal.szymanski.tictactoe.model.v2.Player;
import pl.michal.szymanski.tictactoe.play.v2.PlayInfo;
import pl.michal.szymanski.tictactoe.play.v2.PlaySettings;

import pl.michal.szymanski.tictactoe.transport.v2.Participant;
import pl.michal.szymanski.tictactoe.transport.v2.ProxyResponse;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class AIAdapter extends Player {

    private ContextAwareAI ai;
    private final String name = "AI";
    private final String id = UUID.randomUUID().toString();

    public AIAdapter(ContextAwareAI ai) {
        this.ai = ai;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public void getMoveField(ProxyResponse<IntPoint> proxy) {
        java.awt.Point p2d = ai.generateMove();
        IntPoint p = new IntPoint((int) p2d.getX(), (int) p2d.getY());
        proxy.setReal(p);
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public void isConnected(ProxyResponse<Boolean> response) {
        response.setReal(Boolean.TRUE);
    }

    public ContextAwareAI getAI() {
        return this.ai;
    }

    @Override
    public void receiveBoard(Board board) {
        BoardReader<BoardField> r = new BoardReader();
        r.configure((a) -> (a.getOwner().isPresent() && !a.getOwner().get().getId().equals(this.id)),
                (a) -> !(a.getOwner().isPresent()),
                (a) -> (a.getOwner().isPresent() && a.getOwner().get().getId().equals(this.id))
        );
        ai.pushNextBoardState(r.read(board.getBoard()));
    }

    @Override
    public void receiveGameResult(GameResult r) {
    }

    @Override
    public void onTurnTimeout() {
    }

    @Override
    public void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings) {
    }

}
