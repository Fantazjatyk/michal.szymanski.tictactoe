/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.core;

import java.util.Optional;
import java.util.UUID;
import pl.michal.szymanski.tictactoe.transport.Participant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Player<T extends Participant> {

    private String username;
    private BoardFieldType boardFieldType;
    private Optional<T> connector;

    public Player(String username) {
        this.username = username;
    }

    public Player(String username, T connector) {
        this.username = username;
        this.connector = Optional.ofNullable(connector);
    }

    public Player(String username, BoardFieldType boardFieldType) {
        this.username = username;
        this.boardFieldType = boardFieldType;
    }

    public Player(String username, BoardFieldType boardFieldType, T connector) {
        this.username = username;
        this.boardFieldType = boardFieldType;
        this.connector = Optional.ofNullable(connector);
    }

    public void setBoardFieldType(BoardFieldType boardFieldType) {
        this.boardFieldType = boardFieldType;
    }

    public void setConnector(T connector) {
        this.connector = Optional.ofNullable(connector);
    }

    public Optional<T> connector() {
        return this.connector;
    }

    public String getUsername() {
        return username;
    }

    public BoardFieldType getBoardFieldType() {
        return boardFieldType;
    }

    @Override
    public boolean equals(Object obj) {
        boolean test = obj != null && obj instanceof Player && ((Player) obj).getUsername().equals(this.getUsername());
        return test;
    }

}
