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
package pl.michal.szymanski.tictactoe.play;

import java.util.EmptyStackException;
import java.util.Stack;
import pl.michal.szymanski.tictactoe.model.Board;
import pl.michal.szymanski.tictactoe.model.Point;
import pl.michal.szymanski.tictactoe.transport.MultiplayerParticipant;
import pl.michal.szymanski.tictactoe.transport.ProxyResponse;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class TestParticipant implements MultiplayerParticipant {

    private Stack<Point<Integer>> moves = new Stack();

    public void setProgrammedMoves(Stack<Point<Integer>> programmedMoves) {
        this.moves = programmedMoves;
    }

    public Stack<Point<Integer>> getMoves() {
        return moves;
    }

    @Override
    public void getMoveField(ProxyResponse<Point> proxy) {
        try {
            proxy.setReal(moves.pop());
        } catch (EmptyStackException e) {

        }
    }

    @Override
    public void isConnected(ProxyResponse<Boolean> response) {
    }

    @Override
    public String getDisplayName() {
        return this.getClass().getName();
    }

    @Override
    public void receiveBoard(Board board) {
    }

    @Override
    public void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings) {
    }

    @Override
    public void onTurnTimeout() {
    }

}
