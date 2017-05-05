/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core.model;

import java.util.UUID;
import pl.michal.szymanski.ticktacktoe.transport.Connector;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Player<T extends Connector> {

    private String id;
    private T connector;

    public Player(T e) {
        this.connector = e;
        this.id = UUID.randomUUID().toString();
    }

    public T connector() {
        return this.connector;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean test = obj instanceof Player && ((Player) obj).getId().equals(this.getId());
        return test;
    }

}
