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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayersPairTest {

    private PlayersPair pair;
    private Player p1;
    private Player p2;

    @Before
    public void setUp() {
        pair = new PlayersPair();
        p1 = new TestParticipant();
        p2 = new TestParticipant();
        pair.firstPlayer(p1);
        pair.secondPlayer(p2);
    }

    @Test
    public void testOnePlayer() {
        pair = new PlayersPair();
        pair.firstPlayer(p1);

        assertTrue(pair.getFirstPlayer().isPresent());
        assertFalse(pair.getSecondPlayer().isPresent());
        assertFalse(pair.isPair());
    }

    @Test
    public void testTwoPlayers() {
        assertTrue(pair.getFirstPlayer().isPresent() && pair.getSecondPlayer().isPresent());
        assertTrue(pair.isPair());
    }

    @Test
    public void testAssignBoardFieldsMarks() {
        pair.assignBoardFieldsMarks();

        Optional<Player> p1 = pair.getOMarkPlayer();
        Optional<Player> p2 = pair.getXMarkPlayer();
        assertTrue(p1.isPresent() && p2.isPresent());
        assertNotEquals(p1.get(), p2.get());

    }

    @Test
    public void testFiler() {
        this.p1 = new TestParticipant("abcd");

        this.pair = new PlayersPair();
        pair.firstPlayer(p1);
        pair.secondPlayer(p2);
        Optional o = pair.filter(el -> ((Player) el).getId().equals("abcd"));
        assertTrue(o.isPresent());
        assertEquals("abcd", ((Player) o.get()).getId());
    }

}
