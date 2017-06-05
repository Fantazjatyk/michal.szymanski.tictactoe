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

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import pl.michal.szymanski.tictactoe.model.Board;
import pl.michal.szymanski.tictactoe.model.Player;
import pl.michal.szymanski.tictactoe.transport.GameWatcher;
import pl.michal.szymanski.tictactoe.transport.Participant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayInfo<T extends Participant> {

    private String id;
    private int totalTimeInMilis = 0;
    private Optional<Player<T>> winner = Optional.empty();
    private PlayersPair<T> players = new PlayersPair();
    private Board board = new Board(3);
    private LinkedBlockingDeque<GameWatcher> watchers = new LinkedBlockingDeque(100);

    public Board getBoard() {
        return this.board;
    }

    public PlayInfo() {
        this.id = UUID.randomUUID().toString() + new Random().nextInt(1000);
    }

    public LinkedBlockingDeque<GameWatcher> getWatchers() {
        return watchers;
    }

    public Optional<Player<T>> getWinner() {
        return winner;
    }

    public PlayersPair<T> getPlayers() {
        return players;
    }

    public void setWinner(Optional<Player<T>> winner) {
        this.winner = winner;
    }

    void setTotalTime(int totalTime) {
        this.totalTimeInMilis = totalTime;
    }

    public int getTotalTimeInMilis() {
        return totalTimeInMilis;
    }

    public String getId() {
        return id;
    }

}
