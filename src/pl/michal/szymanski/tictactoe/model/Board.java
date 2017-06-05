/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.model;


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
        this.board = BoardGenerator.createBoard(sizeX, sizeY);
    }

    public BoardSelector getSelector() {
        return new BoardSelector(this);
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

    public void clear(){
        this.board = BoardGenerator.createBoard(sizeX, sizeY);
    }

    public void doMove(Move move) {
        Player invoker = move.getInvoker().get();
        board[move.getPoint().getY()][move.getPoint().getX()].setOwner(invoker);
    }
}
