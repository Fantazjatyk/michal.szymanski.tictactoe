/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.transport.v2;

import pl.michal.szymanski.tictactoe.transport.*;
import pl.michal.szymanski.tictactoe.model.IntPoint;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface Participant extends GameWatcher, TurnTimeoutHandler {

    void getMoveField(ProxyResponse<IntPoint> proxy) throws Exception;

    String getUsername();

    void isConnected(ProxyResponse<Boolean> response) throws Exception;
}
