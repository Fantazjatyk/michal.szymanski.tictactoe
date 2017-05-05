/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core.model;

import java.lang.ref.WeakReference;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Move {

    private WeakReference<Player> invoker;
    private Point<Integer> point;

    public Move(Player p, Point point) {
        this.invoker = new WeakReference(p);
        this.point = point;
    }

    public WeakReference<Player> getInvoker() {
        return invoker;
    }

    public Point<Integer> getPoint() {
        return point;
    }

}
