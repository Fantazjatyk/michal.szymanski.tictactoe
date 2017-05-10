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
package pl.michal.szymanski.ticktacktoe.core;

import java.util.concurrent.LinkedBlockingDeque;
import pl.michal.szymanski.ticktacktoe.transport.GameWatcher;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayHistory {

    private LinkedBlockingDeque<Move> moves = new LinkedBlockingDeque(9);
    private LinkedBlockingDeque<GameWatcher> watchers = new LinkedBlockingDeque(100);
    private LinkedBlockingDeque<Turn> turns = new LinkedBlockingDeque();

    public LinkedBlockingDeque<Move> getMoves() {
        return moves;
    }

    public void setMoves(LinkedBlockingDeque<Move> moves) {
        this.moves = moves;
    }

    public LinkedBlockingDeque<GameWatcher> getWatchers() {
        return watchers;
    }

    public void setWatchers(LinkedBlockingDeque<GameWatcher> watchers) {
        this.watchers = watchers;
    }

    public LinkedBlockingDeque<Turn> getTurns() {
        return turns;
    }

    public void setTurns(LinkedBlockingDeque<Turn> turns) {
        this.turns = turns;
    }

}
