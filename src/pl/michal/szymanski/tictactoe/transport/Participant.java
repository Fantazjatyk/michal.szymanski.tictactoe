/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.transport;

import pl.michal.szymanski.tictactoe.core.Board;
import pl.michal.szymanski.tictactoe.core.Move;
import pl.michal.szymanski.tictactoe.core.Point;


/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface Participant extends GameWatcher, TurnTimeoutHandler {

    void getMoveField(ProxyResponseSetter<Point> proxy);

}
