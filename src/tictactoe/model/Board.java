/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.model;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Board {

    private int sizeX;
    private int sizeY;
    private BoardField[][] matrix;

    public Board(Board board) {
        this.sizeX = board.sizeX;
        this.sizeY = board.sizeY;
        this.matrix = board.matrix;
    }

    public Board(int size) {
        this.sizeX = size;
        this.sizeY = size;
        this.matrix = BoardGenerator.createBoard(sizeX, sizeY);
    }

    public BoardSelector getSelector() {
        return new BoardSelector(this);
    }

    public BoardField[][] getBoard() {
        return matrix;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void clear() {
        this.matrix = BoardGenerator.createBoard(sizeX, sizeY);
    }

    public void doMove(Move move) {
        Player invoker = move.getInvoker().get();
        BoardField bf = matrix[move.getPoint().getY()][move.getPoint().getX()];
        bf.setOwner(invoker);
    }
}
