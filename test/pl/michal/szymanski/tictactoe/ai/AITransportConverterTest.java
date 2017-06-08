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
package pl.michal.szymanski.tictactoe.ai;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.michal.szymanski.tictactoe.model.Board;
import pl.michal.szymanski.tictactoe.model.BoardField;
import pl.michal.szymanski.tictactoe.model.Move;
import pl.michal.szymanski.tictactoe.model.Player;
import pl.michal.szymanski.tictactoe.model.Point;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class AITransportConverterTest {

    public AITransportConverterTest() {
    }

    Player me;
    Player notMe;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        me = new Player();
        notMe = new Player();
    }

    /**
     * Test of convertBoardToThreeValuedMatrix method, of class
     * AITransportConverter.
     */
    @Test
    public void testConvertBoardLineToThreeValuedMatrix() {
        Board b = new Board(3);
        b.doMove(new Move(notMe, new Point(0, 0)));
        b.doMove(new Move(me, new Point(1, 0)));
        BoardField[] line = b.getSelector().getRow(0);
        int[] result = AITransportConverter.convertBoardLineToThreeValuedType(line, me.getId());
        assertEquals(line.length, result.length);
        assertEquals(-1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(0, result[2]);
    }

    @Test
    public void testConvertBoardToThreeValuedMatrix() {
        Board b = new Board(3);
        List<Move> moves = new ArrayList();

        moves.add(new Move(notMe, new Point(0, 0)));
        moves.add(new Move(me, new Point(1, 0)));
        moves.add(new Move(me, new Point(2, 0)));
        moves.add(new Move(notMe, new Point(1, 1)));
        moves.add(new Move(notMe, new Point(2, 1)));
        moves.add(new Move(me, new Point(2, 2)));

        moves.forEach(el -> b.doMove(el));
        int[][] result = AITransportConverter.convertBoardToThreeValuedMatrix(b, me.getId());
        assertEquals(3, result.length);
        assertEquals(-1, result[0][0]);
        assertEquals(1, result[0][1]);
        assertEquals(1, result[0][2]);
        assertEquals(0, result[1][0]);
        assertEquals(-1, result[1][1]);
        assertEquals(-1, result[1][2]);
        assertEquals(0, result[2][0]);
        assertEquals(0, result[2][1]);
        assertEquals(1, result[2][2]);
    }

    /**
     * Test of convertBoardFieldToThreeValuedType method, of class
     * AITransportConverter.
     */
    @Test
    public void testConvertBoardFieldToThreeValuedType_Positive() {
        BoardField f = new BoardField(0, 0);
        f.setOwner(me);
        int result = AITransportConverter.convertBoardFieldToThreeValuedType(f, me.getId());
        assertEquals(1, result);
    }

    @Test
    public void testConvertBoardFieldToThreeValuedType_Negative() {
        BoardField f = new BoardField(0, 0);
        f.setOwner(notMe);
        int result = AITransportConverter.convertBoardFieldToThreeValuedType(f, me.getId());
        assertEquals(-1, result);
    }

    @Test
    public void testConvertBoardFieldToThreeValuedType_Unknown() {
        BoardField f = new BoardField(0, 0);
        int result = AITransportConverter.convertBoardFieldToThreeValuedType(f, me.getId());
        assertEquals(0, result);
    }

}
