/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.model;


import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import pl.michal.szymanski.tictactoe.transport.Participant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Player<T extends Participant> {

    private String id;
    private BoardFieldType boardFieldType;
    private Optional<T> connector;

    public Player() {
        this.id = UUID.randomUUID().toString();
    }

    public Player(Optional<T> connector) {
        this.connector = connector;
    }

    public Player(BoardFieldType boardFieldType) {
        this.boardFieldType = boardFieldType;
    }

    public Player(BoardFieldType boardFieldType, Optional<T> connector) {
        this.boardFieldType = boardFieldType;
        this.connector = connector;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Optional<T> getConnector() {
        return connector;
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

    public BoardFieldType getBoardFieldType() {
        return boardFieldType;
    }

    @Override
    public boolean equals(Object obj) {
        boolean test = obj != null && obj instanceof Player && ((Player) obj).id.equals(this.id);
        return test;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

}
