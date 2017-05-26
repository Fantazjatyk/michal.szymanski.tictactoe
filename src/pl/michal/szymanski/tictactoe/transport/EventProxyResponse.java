package pl.michal.szymanski.tictactoe.transport;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import pl.michal.szymanski.tictactoe.transport.ProxyResponse;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class EventProxyResponse<T> extends ProxyResponse<T> {

    private Lock lock;

    @Override
    public void setReal(T real) {
        super.setReal(real); //To change body of generated methods, choose Tools | Templates.
        synchronized(lock){
        lock.notify();
        }
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }
}
