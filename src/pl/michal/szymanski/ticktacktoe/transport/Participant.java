/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.transport;

import pl.michal.szymanski.ticktacktoe.core.Board;
import pl.michal.szymanski.ticktacktoe.core.Move;
import pl.michal.szymanski.ticktacktoe.core.Point;
import pl.michal.szymanski.ticktacktoe.transport.ProxyResponse.ProxyResponseSetter;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface Participant extends GameWatcher, TurnTimeoutHandler {

    void getMoveField(ProxyResponse<Point>.ProxyResponseSetter<Point> proxy);

}
