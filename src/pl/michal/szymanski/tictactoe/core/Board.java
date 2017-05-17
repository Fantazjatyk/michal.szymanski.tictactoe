/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Board {

    private int sizeX;
    private int sizeY;
    private BoardField[][] board;

    public Board(Board board) {
        this.sizeX = board.sizeX;
        this.sizeY = board.sizeY;
        this.board = board.board;
    }

    public Board(int size) {
        this.sizeX = size;
        this.sizeY = size;
        this.board = createBoard(sizeX, sizeY);
    }

    public BoardField[][] getBoard() {
        return board;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public List<BoardField> getAllFields() {
        List<BoardField> fields = new ArrayList();
        Stream.of(this.board).forEach(el -> fields.addAll(Stream.of(el).collect(Collectors.toList())));
        return fields;
    }

    public List<BoardField[]> getDiagonals() {
        List<BoardField[]> diagonals = new ArrayList();
        diagonals.add(getDiagonal(new Point(0, 0), new Point(this.getSizeX() - 1, this.getSizeY() - 1)));
        diagonals.add(getDiagonal(new Point(0, this.getSizeY() - 1), new Point(this.getSizeX() - 1, 0)));
        return diagonals;
    }

    private BoardField[] getDiagonal(Point<Integer> p1, Point<Integer> p2) {

        int a = this.getSizeX() - 1 - p1.getX();
        a = a == 0 && p1.getX() == this.getSizeX() - 1 ? (this.getSizeX() - 1) : (p1.getX() == 0 ? a - 1 : a);

        a = a < 0 ? a + (2 * a) : a;

        boolean incrementX = p2.getX() > p1.getX();
        boolean incrementY = p2.getY() > p1.getY();

        List<BoardField> fields = new ArrayList();

        for (int x = p1.getX(), y = p1.getY(); incrementX && x <= p2.getX() || !incrementX && x >= 0;) {
            fields.add(board[x][y]);

            x = incrementX ? x + 1 : x - 1;
            y = incrementY ? y + 1 : y - 1;
        }
        return fields.toArray(new BoardField[0]);
    }

    private BoardField[][] createBoard(int sizeX, int sizeY) {
        BoardField[][] board = new BoardField[sizeX][sizeY];

        for (int i = 0; i < this.sizeY; i++) {
            board[i] = fillRow(i, sizeY);
        }
        return board;
    }

    private BoardField[] fillRow(int rowId, int max) {
        BoardField[] row = new BoardField[max];

        for (int i = 0; i < row.length; i++) {
            row[i] = new BoardField(i, rowId);
        }
        return row;
    }

    public BoardField[] getRow(int id) {
        return this.board[id];
    }

    public List<BoardField[]> getRows() {
        List<BoardField[]> rows = new ArrayList();

        for (int i = 0; i < this.getSizeY(); i++) {
            rows.add(getRow(i));
        }
        return rows;
    }

    public List<BoardField[]> getColumns() {
        List<BoardField[]> rows = new ArrayList();

        for (int i = 0; i < this.getSizeY(); i++) {
            rows.add(getColumn(i));
        }
        return rows;
    }

    public BoardField[] getColumn(int id) {
        BoardField[] result = new BoardField[this.getSizeX()];

        for (int x = 0; x < result.length; x++) {
            result[x] = this.board[x][id];
        }
        return result;
    }

    public void doMove(Move move) {
        Player invoker = move.getInvoker().get();
        board[move.getPoint().getY()][move.getPoint().getX()].setOwner(invoker);
    }
}
