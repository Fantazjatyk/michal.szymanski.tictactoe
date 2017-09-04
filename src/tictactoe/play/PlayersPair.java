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

import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import tictactoe.model.BoardFieldType;
import tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayersPair {

    private Optional<Player> firstPlayer = Optional.empty();
    private Optional<Player> secondPlayer = Optional.empty();

    public Optional<Player> getFirstPlayer() {
        return firstPlayer;
    }

    public Optional<Player> getSecondPlayer() {
        return secondPlayer;
    }

    public boolean isPair() {
        return this.firstPlayer.isPresent() && this.secondPlayer.isPresent();
    }

    public Optional<Player> getPlayer(String username) {
        return firstPlayer.isPresent() && firstPlayer.get().getUsername().equals(username)
                ? firstPlayer : (secondPlayer.isPresent() && secondPlayer.get().getUsername().equals(username) ? secondPlayer : Optional.empty());
    }

    public Optional<Player> getOMarkPlayer() {
        return getMarkedPlayer(BoardFieldType.OMark);
    }

    public Optional<Player> getMarkedPlayer(BoardFieldType type) {
        return firstPlayer.isPresent() && firstPlayer.get().getType().equals(type) ? firstPlayer
                : (secondPlayer.isPresent() && secondPlayer.get().getType().equals(type) ? secondPlayer : Optional.empty());
    }

    public Optional<Player> getXMarkPlayer() {
        return getMarkedPlayer(BoardFieldType.XMark);
    }

    public Player getRandomPlayer() {
        boolean isThatFirst = new Random().nextBoolean();
        return isThatFirst ? getFirstPlayer().get() : getSecondPlayer().get();
    }

    public void assignBoardFieldsMarks() {

        boolean random = new Random().nextBoolean();

        if (random) {
            getFirstPlayer().get().setType(BoardFieldType.XMark);
            getSecondPlayer().get().setType(BoardFieldType.OMark);
        } else {
            getFirstPlayer().get().setType(BoardFieldType.OMark);
            getSecondPlayer().get().setType(BoardFieldType.XMark);
        }

    }

    public Optional<Player> filter(Predicate<Player> pr) {
        Optional<Player> result = Optional.empty();

        if (pr.test(this.firstPlayer.get())) {
            result = this.firstPlayer;
        } else if (pr.test(this.secondPlayer.get())) {
            result = this.secondPlayer;
        }
        return result;
    }

    protected void firstPlayer(Player player) {
        this.firstPlayer = Optional.ofNullable(player);
    }

    protected void secondPlayer(Player player) {
        this.secondPlayer = Optional.ofNullable(player);
    }

}
