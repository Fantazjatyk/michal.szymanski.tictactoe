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

import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tictactoe.play.TestParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class BoardSelectorTest {

    Board board;

    @Before
    public void setUp() {
        board = new Board(3);
    }

    @Test
    public void testGetRow() {
        BoardField[] row = board.getSelector().getRow(1);
        assertEquals(0, row[0].getX());
        assertEquals(1, row[0].getY());

        assertEquals(1, row[1].getX());
        assertEquals(1, row[1].getY());

        assertEquals(2, row[2].getX());
        assertEquals(1, row[2].getY());
    }

    @Test
    public void testGetColumn() {
        BoardField[] column = board.getSelector().getColumn(1);
        assertEquals(1, column[0].getX());
        assertEquals(0, column[0].getY());

        assertEquals(1, column[1].getX());
        assertEquals(1, column[1].getY());

    }

    @Test
    public void testGetSimplified() {
        Player a = new TestParticipant();
        Player b = new TestParticipant();
        a.setType(BoardFieldType.XMark);
        b.setType(BoardFieldType.OMark);

        board.doMove(new Move(a, new IntPoint(0, 0)));
        board.doMove(new Move(a, new IntPoint(1, 0)));
        board.doMove(new Move(a, new IntPoint(2, 0)));

        board.doMove(new Move(b, new IntPoint(2, 2)));
        board.doMove(new Move(b, new IntPoint(1, 1)));
        board.doMove(new Move(b, new IntPoint(0, 2)));
        board.doMove(new Move(b, new IntPoint(0, 1)));

        String[][] result = board.getSelector().getSimplified();
        assertEquals(board.getSizeY(), result.length);
        assertEquals("X", result[0][0]);
        assertEquals("X", result[0][1]);
        assertEquals("X", result[0][2]);

        assertEquals("O", result[2][2]);
        assertEquals("O", result[1][1]);
        assertEquals("O", result[2][0]);
        assertEquals("O", result[1][0]);

        assertEquals("?", result[1][2]);
        assertEquals("?", result[2][1]);
    }

    @Test
    public void testGetSimplified_StepByStep() {
        Player a = new TestParticipant();
        Player b = new TestParticipant();
        a.setType(BoardFieldType.XMark);
        b.setType(BoardFieldType.OMark);
        assertTrue(testStepDoMove(new IntPoint(0, 0), b));
        assertTrue(testStepDoMove(new IntPoint(1, 0), b));
        assertTrue(testStepDoMove(new IntPoint(2, 0), b));
        assertTrue(testStepDoMove(new IntPoint(0, 1), b));
        assertTrue(testStepDoMove(new IntPoint(1, 1), b));
        assertTrue(testStepDoMove(new IntPoint(2, 1), b));
        assertTrue(testStepDoMove(new IntPoint(0, 2), b));
        assertTrue(testStepDoMove(new IntPoint(1, 2), b));
        assertTrue(testStepDoMove(new IntPoint(2, 2), b));
    }

    public boolean testStepDoMove(IntPoint point, Player p) {
        board.clear();
        board.doMove(new Move(p, point));
        String[][] result = board.getSelector().getSimplified();
        return p.getType().toString().equals(result[point.getY()][point.getX()]);
    }

    @Test
    public void getAllFields_isReallyAll() {
        Player a = new TestParticipant();
        Player b = new TestParticipant();
        a.setType(BoardFieldType.XMark);
        b.setType(BoardFieldType.OMark);

        board.doMove(new Move(a, new IntPoint(0, 0)));
        board.doMove(new Move(a, new IntPoint(1, 0)));
        board.doMove(new Move(a, new IntPoint(2, 0)));

        board.doMove(new Move(b, new IntPoint(2, 2)));
        board.doMove(new Move(b, new IntPoint(1, 1)));

        board.doMove(new Move(b, new IntPoint(0, 1)));
        board.doMove(new Move(b, new IntPoint(2, 1)));

        List fields = board.getSelector().getAllFields().stream().filter(el -> el.getOwner().isPresent()).collect(Collectors.toList());
        assertEquals(7, fields.size());
    }

    @Test
    public void getAllFields_areThereAnyMistakes() {
        Player a = new TestParticipant();
        Player b = new TestParticipant();
        a.setType(BoardFieldType.XMark);
        b.setType(BoardFieldType.OMark);

        board.doMove(new Move(a, new IntPoint(0, 0)));
        board.doMove(new Move(a, new IntPoint(1, 0)));
        board.doMove(new Move(a, new IntPoint(2, 0)));

        board.doMove(new Move(b, new IntPoint(2, 2)));
        board.doMove(new Move(b, new IntPoint(0, 2)));
        board.doMove(new Move(b, new IntPoint(1, 1)));

        board.doMove(new Move(b, new IntPoint(0, 1)));
        board.doMove(new Move(b, new IntPoint(2, 1)));

        List<BoardField> fields = board.getSelector().getAllFields().stream().filter(el -> el.getOwner().isPresent() && el.getOwner().get().equals(b)).collect(Collectors.toList());
        assertEquals(5, fields.size());

        assertFalse(fields.stream().anyMatch(el -> el.getX() == 0 && el.getY() == 0));
        assertFalse(fields.stream().anyMatch(el -> el.getX() == 1 && el.getY() == 0));
        assertFalse(fields.stream().anyMatch(el -> el.getX() == 2 && el.getY() == 0));

        assertTrue(fields.stream().anyMatch(el -> el.getX() == 0 && el.getY() == 2));
        assertTrue(fields.stream().anyMatch(el -> el.getX() == 2 && el.getY() == 2));
        assertTrue(fields.stream().anyMatch(el -> el.getX() == 1 && el.getY() == 1));
        assertTrue(fields.stream().anyMatch(el -> el.getX() == 0 && el.getY() == 1));
        assertTrue(fields.stream().anyMatch(el -> el.getX() == 2 && el.getY() == 1));
    }

    @Test
    public void getAllPlayersFields() {
        Player a = new TestParticipant();
        Player b = new TestParticipant();

        board.doMove(new Move(a, new IntPoint(0, 0)));
        board.doMove(new Move(a, new IntPoint(1, 0)));

        board.doMove(new Move(b, new IntPoint(2, 2)));
        board.doMove(new Move(b, new IntPoint(1, 1)));
        board.doMove(new Move(b, new IntPoint(2, 0)));

        List<BoardField> result = board.getSelector().getPlayerFields(b);
        assertEquals(3, result.size());
        assertEquals(new IntPoint(2, 2), new IntPoint(result.get(2).getX(), result.get(2).getY()));
        assertEquals(new IntPoint(1, 1), new IntPoint(result.get(1).getX(), result.get(1).getY()));
        assertEquals(new IntPoint(2, 0), new IntPoint(result.get(0).getX(), result.get(0).getY()));
    }

    @Test
    public void testGetColumns() {
        List<BoardField[]> columns = board.getSelector().getColumns();
        BoardField[] column1 = columns.get(2);

        assertEquals(3, columns.size());
        assertEquals(2, column1[0].getX());
        assertEquals(0, column1[0].getY());
    }

    /**
     * Test of getColumns method, of class Board.
     */
    @Test
    public void testGetRows() {
        List<BoardField[]> rows = board.getSelector().getRows();
        BoardField[] column1 = rows.get(1);

        assertEquals(3, rows.size());
        assertEquals(0, column1[0].getX());
        assertEquals(1, column1[0].getY());

    }

    @Test
    public void getDiagonals() {
        List<BoardField[]> diagonals = board.getSelector().getDiagonals();
        assertEquals(2, diagonals.size());

        BoardField[] leftRightDiagonal = diagonals.get(0); // always starts with diagonal from 0,0 to max, max
        BoardField f1 = leftRightDiagonal[0];
        BoardField f2 = leftRightDiagonal[1];
        BoardField f3 = leftRightDiagonal[2];

        assertEquals(0, f1.getX());
        assertEquals(0, f1.getY());

        assertEquals(1, f2.getX());
        assertEquals(1, f2.getY());

        assertEquals(2, f3.getX());
        assertEquals(2, f3.getY());

    }

    @Test
    public void testGetAll() {
        List fields = board.getSelector().getAllFields();
        assertEquals(9, fields.size());
    }
}
