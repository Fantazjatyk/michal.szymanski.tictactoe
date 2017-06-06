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
package pl.michal.szymanski.tictactoe.play;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.michal.szymanski.tictactoe.model.Player;
import pl.michal.szymanski.tictactoe.transport.LockProxyResponse;
import pl.michal.szymanski.tictactoe.transport.MultiplayerParticipant;
import pl.michal.szymanski.tictactoe.transport.Participant;
import pl.michal.szymanski.tictactoe.transport.PlayerDisconnectedHandler;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class MultiplayerPlayExecutor extends PlayExecutor<MultiplayerParticipant> implements PlayerDisconnectedHandler {

    public MultiplayerPlayExecutor(Play play) {
        super(play);
    }

    @Override
    protected void doTurn(Player<MultiplayerParticipant> player) {
        LockProxyResponse rs = new LockProxyResponse();

        ReentrantLock lock = new ReentrantLock();
        rs.setLock(lock);

        player.getConnector().get().isConnected(rs);

        synchronized (lock) {
            try {
                lock.wait(super.getPlay().getSettings().getters().getTurnLimit());
            } catch (InterruptedException ex) {
                Logger.getLogger(MultiplayerPlayExecutor.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }

        if (rs.getReal().isPresent()) {
            super.doTurn(player);
        }
        else{
            handleDisconnected(player);
        }

    }

    @Override
    public void handleDisconnected(Player p) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "one or two players disconnected. Terminating play...");
        Optional<Player<MultiplayerParticipant>> winner = super.getPlay().getInfo().getPlayers().filter((el)->el.getId() != p.getId());
        super.getPlay().getInfo().setWinner((winner));
        super.stop();
    }

}
