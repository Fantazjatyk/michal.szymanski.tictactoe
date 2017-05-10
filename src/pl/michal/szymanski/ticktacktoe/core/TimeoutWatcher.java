/*
 * The MIT License
 *
 * Copyright 2017 Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.michal.szymanski.ticktacktoe.core;

import com.google.common.base.Stopwatch;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import pl.michal.szymanski.ticktacktoe.exceptions.TurnTimeoutException;
import pl.michal.szymanski.ticktacktoe.exceptions.TurnTimeoutExceptionHandler;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class TimeoutWatcher extends Thread {

    private long timeout;
    private List<WeakReference<TurnTimeoutExceptionHandler>> observers = new ArrayList();

    public void addObserver(TurnTimeoutExceptionHandler ob) {
        if (!observers.stream().anyMatch(el -> el.get().equals(ob))) {
            this.observers.add(new WeakReference(ob));
        }
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void run() {
        Stopwatch watch = Stopwatch.createStarted();
        while (!this.isInterrupted() && watch.elapsed(TimeUnit.MILLISECONDS) < timeout) {

        }
        observers.forEach(el -> el.get().handle(new TurnTimeoutException()));
    }
}
