/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayLimits {

    private long timeout;
    private long turnLimit;

    public long getTurnLimit() {
        return turnLimit;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout, TimeUnit unit) {
        this.timeout = unit.convert(timeout, TimeUnit.SECONDS);
    }

    public void setMoveLimit(long timeout, TimeUnit unit) {
        this.turnLimit = unit.convert(timeout, TimeUnit.SECONDS);
    }

}
