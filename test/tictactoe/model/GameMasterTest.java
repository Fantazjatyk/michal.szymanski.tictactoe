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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tictactoe.play.GameMaster;
import tictactoe.play.TestParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class GameMasterTest {

    Board board;
    Player player1;
    Player player2;
    Player player3;

    @Before
    public void setUp() {
        player1 = new TestParticipant();
        player2 = new TestParticipant();
        player3 = new TestParticipant();
        board = new Board(3);
    }

    @Test
    public void testIsDone() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player1, new IntPoint(1, 1)));
        board.doMove(new Move(player3, new IntPoint(1, 0)));
        board.doMove(new Move(player2, new IntPoint(0, 2)));
        board.doMove(new Move(player1, new IntPoint(2, 2)));
        board.doMove(new Move(player3, new IntPoint(0, 1)));

        assertTrue(GameMaster.isDone(board));
    }

    @Test
    public void testIsDone_Remis() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player1, new IntPoint(0, 1)));
        board.doMove(new Move(player1, new IntPoint(0, 2)));

        board.doMove(new Move(player3, new IntPoint(1, 0)));
        board.doMove(new Move(player3, new IntPoint(1, 1)));
        board.doMove(new Move(player3, new IntPoint(1, 2)));

        assertTrue(GameMaster.isDone(board));
    }

    @Test
    public void testGetWinner() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player1, new IntPoint(1, 1)));
        board.doMove(new Move(player3, new IntPoint(1, 0)));
        board.doMove(new Move(player2, new IntPoint(0, 2)));
        board.doMove(new Move(player1, new IntPoint(2, 2)));
        board.doMove(new Move(player3, new IntPoint(0, 1)));

        List<Player> winner = GameMaster.getWinner(board);
        assertTrue(winner.size() == 1);
        assertEquals(player1, winner.get(0));

    }

    @Test
    public void testGetWinCombination() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player1, new IntPoint(1, 1)));
        board.doMove(new Move(player1, new IntPoint(2, 2)));
        board.doMove(new Move(player2, new IntPoint(0, 2)));

        board.doMove(new Move(player3, new IntPoint(0, 1)));
        board.doMove(new Move(player3, new IntPoint(1, 0)));

        List<BoardField[]> combination = GameMaster.getWinCombinations(board.getSelector().getAllPossibleWinningLines());
        assertEquals(1, combination.size());
    }

    @Test
    public void testAreLineOwnedOnlyByOnePlayer() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player1, new IntPoint(0, 1)));
        board.doMove(new Move(player1, new IntPoint(0, 2)));

        board.doMove(new Move(player3, new IntPoint(1, 0)));
        board.doMove(new Move(player3, new IntPoint(1, 1)));
        board.doMove(new Move(player3, new IntPoint(1, 2)));

        boolean result = GameMaster.areLineOwnedOnlyByOnePlayer(board.getSelector().getColumn(0));
        assertTrue(result);
    }

    @Test
    public void testGetWinner_Remis() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player1, new IntPoint(0, 1)));
        board.doMove(new Move(player1, new IntPoint(0, 2)));

        board.doMove(new Move(player3, new IntPoint(1, 0)));
        board.doMove(new Move(player3, new IntPoint(1, 1)));
        board.doMove(new Move(player3, new IntPoint(1, 2)));

        List<Player> winner = GameMaster.getWinner(board);
        assertEquals(2, winner.size());
    }

    @Test
    public void testIsValidMove() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player2, new IntPoint(1, 1)));
        board.doMove(new Move(player1, new IntPoint(2, 0)));
        Move move = new Move(player2, new IntPoint(0, 0));
        assertFalse(GameMaster.isValidMove(move, board));
    }

    @Test
    public void testIsValidMove_Optimistic() {
        board.doMove(new Move(player1, new IntPoint(0, 0)));
        board.doMove(new Move(player2, new IntPoint(1, 1)));
        board.doMove(new Move(player1, new IntPoint(2, 0)));
        Move move = new Move(player2, new IntPoint(2, 2));
        assertTrue(GameMaster.isValidMove(move, board));
    }

}
