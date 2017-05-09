/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class PlayBase<T> {

    private String id;

    protected abstract void onStart();

    protected abstract void onFinish();

    public abstract boolean join(T t, String username);
}
