/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core.model;

import java.util.stream.Stream;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Board {

    protected int sizeX;
    protected int sizeY;
    private BoardField[][] board;

    public Board(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.board = createBoard(sizeX, sizeY);
    }

    public BoardField[][] getBoard() {
        return board;
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

    public boolean isValid(Move move) {
        return true;
    }

    public void doMove(Move move) {
        Player invoker = move.getInvoker().get();
        board[move.getPoint().getX()][move.getPoint().getY()].setOwner(invoker);

    }
}
