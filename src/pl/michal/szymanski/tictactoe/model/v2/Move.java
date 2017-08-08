/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.model.v2;

import pl.michal.szymanski.tictactoe.model.*;
import java.lang.ref.WeakReference;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Move {

    private WeakReference<Player> invoker;
    private IntPoint point;
    private boolean wasValid = true;

    public Move(Player p, IntPoint point) {
        this.invoker = new WeakReference(p);
        this.point = point;
    }

    public WeakReference<Player> getInvoker() {
        return invoker;
    }

    public IntPoint getPoint() {
        return point;
    }

    public void invalidate() {
        this.wasValid = false;
    }

}
