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
package tictactoe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tictactoe.play.TestParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class BoardTest {

    Board board;

    @Before
    public void setUp() {
        board = new Board(3);
    }

    public void testCreateBoard() {
        assertEquals(9, board.getSizeX() * board.getSizeY());
    }

    @Test
    public void testDoMove() {
        Player a = new TestParticipant();
        Player b = new TestParticipant();
        a.setType(BoardFieldType.XMark);
        b.setType(BoardFieldType.OMark);

        board.doMove(new Move(a, new IntPoint(0, 0)));
        assertTrue(board.getBoard()[0][0].getOwner().isPresent());

        board.clear();

        board.doMove(new Move(a, new IntPoint(1, 1)));
        assertTrue(board.getBoard()[1][1].getOwner().isPresent());

        board.clear();

        board.doMove(new Move(a, new IntPoint(2, 2)));
        assertTrue(board.getBoard()[2][2].getOwner().isPresent());

        board.clear();

        board.doMove(new Move(a, new IntPoint(0, 2)));
        assertTrue(board.getBoard()[2][0].getOwner().isPresent());

        board.clear();
        board.doMove(new Move(a, new IntPoint(2, 0)));
        assertTrue(board.getBoard()[0][2].getOwner().isPresent());

        board.clear();

        board.doMove(new Move(a, new IntPoint(0, 1)));
        assertTrue(board.getBoard()[1][0].getOwner().isPresent());

    }

}
