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
package pl.michal.szymanski.tictactoe.core;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class BoardSelector {

    private WeakReference<Board> board;

    public BoardSelector(Board board) {
        this.board = new WeakReference(board);
    }

    public List<BoardField[]> getDiagonals() {
        List<BoardField[]> diagonals = new ArrayList();
        diagonals.add(getDiagonal(new Point(0, 0), new Point(board.get().getSizeX() - 1, board.get().getSizeY() - 1)));
        diagonals.add(getDiagonal(new Point(0, board.get().getSizeY() - 1), new Point(board.get().getSizeX() - 1, 0)));
        return diagonals;
    }

    public String[][] getSimplified() {
        String[][] result = new String[this.board.get().getSizeX()][this.board.get().getSizeY()];
        for (int y = 0; y < this.board.get().getSizeY(); y++) {
            BoardField[] row = this.board.get().getSelector().getRow(y);
            for (int x = 0; x < row.length; x++) {
                BoardField field = row[x];
                result[x][y] = field.getOwner().isPresent() ? field.getOwner().get().getBoardFieldType().toString() : "?";
            }
        }
        return result;
    }

    private BoardField[] getDiagonal(Point<Integer> p1, Point<Integer> p2) {

        int a = board.get().getSizeX() - 1 - p1.getX();
        a = a == 0 && p1.getX() == board.get().getSizeX() - 1 ? (board.get().getSizeX() - 1) : (p1.getX() == 0 ? a - 1 : a);

        a = a < 0 ? a + (2 * a) : a;

        boolean incrementX = p2.getX() > p1.getX();
        boolean incrementY = p2.getY() > p1.getY();

        List<BoardField> fields = new ArrayList();

        for (int x = p1.getX(), y = p1.getY(); incrementX && x <= p2.getX() || !incrementX && x >= 0;) {
            fields.add(board.get().getBoard()[x][y]);

            x = incrementX ? x + 1 : x - 1;
            y = incrementY ? y + 1 : y - 1;
        }
        return fields.toArray(new BoardField[0]);
    }

    public List<BoardField[]> getColumns() {
        List<BoardField[]> rows = new ArrayList();

        for (int i = 0; i < board.get().getSizeY(); i++) {
            rows.add(getColumn(i));
        }
        return rows;
    }

    public BoardField[] getColumn(int id) {
        BoardField[] result = new BoardField[board.get().getSizeX()];

        for (int x = 0; x < result.length; x++) {
            result[x] = board.get().getBoard()[x][id];
        }
        return result;
    }

    public List<BoardField> getAllFields() {
        List<BoardField> fields = new ArrayList();
        Stream.of(board.get().getBoard()).forEach(el -> fields.addAll(Stream.of(el).collect(Collectors.toList())));
        return fields;
    }

    public BoardField[] getRow(int id) {
        return board.get().getBoard()[id];
    }

    public List<BoardField[]> getRows() {
        List<BoardField[]> rows = new ArrayList();

        for (int i = 0; i < board.get().getSizeY(); i++) {
            rows.add(getRow(i));
        }
        return rows;
    }

    public List<BoardField> getPlayerFields(Player p) {
        List<BoardField> allFields = getAllFields();
        return allFields.stream().filter(el -> el.getOwner().isPresent() && el.getOwner().get().equals(p)).collect(Collectors.toList());
    }
}
