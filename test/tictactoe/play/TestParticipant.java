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
package tictactoe.play;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.model.Board;
import tictactoe.model.GameResult;
import tictactoe.model.IntPoint;
import tictactoe.model.Player;
import tictactoe.transport.ProxyResponse;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class TestParticipant extends Player {

    private Stack<IntPoint> moves = new Stack();
    private boolean respondOnGetField = true;
    private boolean respondOnConnected = true;
    private long wait = 0;

    public TestParticipant(String id) {
        super(id);
    }

    public TestParticipant() {
    }

    public void dontRespondOnGetField() {
        this.respondOnGetField = false;
    }

    public void dontRespondOnConnected() {
        this.respondOnConnected = false;
    }

    public void setWaitTime(long milis) {
        wait = milis;
    }

    public void setProgrammedMoves(Stack<IntPoint> programmedMoves) {
        this.moves = programmedMoves;
    }

    public Stack<IntPoint> getMoves() {
        return moves;
    }

    @Override
    public void getMoveField(ProxyResponse<IntPoint> proxy) throws Exception {

        if (wait != 0) {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestParticipant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (respondOnGetField) {
            try {
                proxy.setReal(moves.pop());
            } catch (EmptyStackException e) {

            }
        }
    }

    @Override
    public void isConnected(ProxyResponse<Boolean> response) throws Exception {
        if (this.respondOnConnected) {
            response.setReal(Boolean.TRUE);
        }
    }

    @Override
    public void receiveBoard(Board board) {
    }

    @Override
    public void onTurnTimeout() {
    }

    @Override
    public void receiveGameResult(GameResult r) {
    }

    @Override
    public void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings) {
    }

}
