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

import tictactoe.model.Board;
import tictactoe.model.Player;
import tictactoe.model.Turn;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
abstract class AbstractGameRunner implements GameRunner {

    private GameSettings settings;
    private GameHistory history = new GameHistory();
    private PlayersPair players;
    private Board board = new Board(3);

    public AbstractGameRunner(PlayersPair players) {
        this.players = players;
    }

    GameSettings getSettings() {
        return this.settings;
    }

    GameHistory getHistory() {
        return this.history;
    }

    PlayersPair getPlayers() {
        return this.players;
    }

    Board getBoard() {
        return this.board;
    }

    protected final Player getNextPlayer() {
        Turn lastTurn = getHistory().getTurns().getLast();
        return lastTurn.getQuarterback().equals(getPlayers().getFirstPlayer().get())
                ? getPlayers().getSecondPlayer().get() : getPlayers().getFirstPlayer().get();
    }

}
