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
package tictactoe.transport;

import java.util.Optional;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class EventProxyResponse<T> extends ProxyResponse<T> {

    private Optional<Runnable> beforeCallback = Optional.empty();
    private Optional<Runnable> afterCallback = Optional.empty();

    public void setBeforeCallback(Runnable beforeCallback) {
        this.beforeCallback = Optional.of(beforeCallback);
    }

    public void setAfterCallback(Runnable afterCallback) {
        this.afterCallback = Optional.of(afterCallback);
    }

    @Override
    public void setReal(T real) {
        this.beforeCallback.ifPresent(el -> el.run());
        super.setReal(real); //To change body of generated methods, choose Tools | Templates.
        this.afterCallback.ifPresent(el -> el.run());
    }

}
