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
package pl.michal.szymanski.ticktacktoe.core.model;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.michal.szymanski.ticktacktoe.transport.Connector;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class GameMasterTest {

    Board board;
    Player player1;
    Player player2;
    Player player3;

    public GameMasterTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        player1 = new Player(new Connector() {
            @Override
            public Move getMove() {
                return null;
            }

            @Override
            public void receiveBoard(Board board) {
            }
        });
        player2 = new Player(new Connector() {
            @Override
            public Move getMove() {
                return null;
            }

            @Override
            public void receiveBoard(Board board) {
            }
        });
        player3 = new Player(new Connector() {
            @Override
            public Move getMove() {
                return null;
            }

            @Override
            public void receiveBoard(Board board) {
            }
        });
        board = new Board(3, 3);
    }

    /**
     * Test of isDone method, of class GameMaster.
     */
    @Test
    public void testIsDone() {
    }

    /**
     * Test of getWinner method, of class GameMaster.
     */
    @Test
    public void testGetWinner() {
        board.doMove(new Move(player1, new Point(1, 1)));
        board.doMove(new Move(player1, new Point(2, 2)));
        board.doMove(new Move(player3, new Point(1, 0)));
        board.doMove(new Move(player2, new Point(0, 0)));
        board.doMove(new Move(player1, new Point(2, 1)));
        board.doMove(new Move(player3, new Point(0, 1)));

        Player winner = GameMaster.getWinner(board);
        assertEquals(player1, winner);

    }

}
