/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core.model;

import pl.michal.szymanski.ticktacktoe.transport.Endpoint;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class GameMaster {

    public boolean isDone(Board board) {
        return false;
    }

    public Endpoint getWinner(Board board) {
        return null;
    }
}
