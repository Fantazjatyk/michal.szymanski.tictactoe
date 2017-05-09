/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core.model;

import java.util.UUID;
import pl.michal.szymanski.ticktacktoe.transport.Participant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Player<T extends Participant> {

    private String id;
    private String username;

    private T connector;

    public Player(T e, String username) {
        this.connector = e;
        this.id = UUID.randomUUID().toString();
        this.username = username;
    }

    public T connector() {
        return this.connector;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        boolean test = obj instanceof Player && ((Player) obj).getId().equals(this.getId());
        return test;
    }

}
