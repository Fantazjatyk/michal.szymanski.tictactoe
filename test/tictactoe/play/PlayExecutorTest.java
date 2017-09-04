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
package tictactoe.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tictactoe.exceptions.PlayerDisconnectedException;
import tictactoe.model.IntPoint;
import tictactoe.model.Player;
import tictactoe.play.GameRunner.GameRunnerStatus;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayExecutorTest {

    TestParticipant p1;
    TestParticipant p2;
    Game play;
    BasicGameRunner exe;

    @Before
    public void setUp() {
        p1 = new TestParticipant();
        p2 = new TestParticipant();

        p1.setUsername("A");
        p2.setUsername("B");

        Stack<IntPoint> a1Moves = new Stack();
        a1Moves.push(new IntPoint(0, 0));
        a1Moves.push(new IntPoint(1, 1));
        a1Moves.push(new IntPoint(2, 2));
        Stack<IntPoint> a2Moves = new Stack();
        a2Moves.push(new IntPoint(0, 1));
        a2Moves.push(new IntPoint(0, 2));
        a2Moves.push(new IntPoint(1, 2));

        p1.setProgrammedMoves(a1Moves);
        p2.setProgrammedMoves(a2Moves);

        play = new Game();
        play.configure().moveTimeLimit(250, TimeUnit.MILLISECONDS).gameTimeLimit(1, TimeUnit.SECONDS);
        exe = new BasicGameRunner(play);
    }

    @Test(timeout = 1000)
    public void testIfGameLastSpecifiedTime() throws PlayerDisconnectedException {
        play.configure().gameTimeLimit(500, TimeUnit.MILLISECONDS);
        play.configure().moveTimeLimit(100, TimeUnit.MILLISECONDS);
        p1.dontRespondOnGetField();
        p1.setWaitTime(100);
        p1.setWaitTime(100);
        p2.dontRespondOnGetField();
        play.join(p1);
        play.join(p2);
        new BasicGameRunner(play).start();
    }

    @Test(timeout = 2000)
    public void testIfAllParticipantsReceivedEXACTLYGetFieldRequests() throws Exception, Exception {
        play.configure().moveTimeLimit(100, TimeUnit.MILLISECONDS).gameTimeLimit(200, TimeUnit.MILLISECONDS);

        p1 = Mockito.spy(TestParticipant.class);
        p1.setUsername("A");
        p2 = Mockito.spy(TestParticipant.class);
        p2.setUsername("B");
        p1.dontRespondOnGetField();
        p2.dontRespondOnGetField();
        play.join(p1);
        play.join(p2);
        new BasicGameRunner(play).start();

        Mockito.verify(p1, Mockito.times(1)).getMoveField(Mockito.any());
        Mockito.verify(p2, Mockito.times(1)).getMoveField(Mockito.any());

    }

    @Test(timeout = 5000)
    public void testIfAllParticipantsGetFieldsRequestResponsesWereMarkedAtBoard() throws PlayerDisconnectedException {
        play.configure().moveTimeLimit(260, TimeUnit.MILLISECONDS).gameTimeLimit(1, TimeUnit.SECONDS);
        p1.setWaitTime(250);
        p2.setWaitTime(250);
        play.join(p1);
        play.join(p2);
        new BasicGameRunner(play).start();

        assertEquals(4, play.getInfo().getBoard().getSelector().getAllFields().stream().filter(el -> el.getOwner().isPresent()).collect(Collectors.toList()).size());
    }

    @Test(timeout = 5000)
    public void testGameEndWhenPlayerHit3PointsInLine() throws PlayerDisconnectedException {
        play.configure().moveTimeLimit(250, TimeUnit.MILLISECONDS).gameTimeLimit(2, TimeUnit.SECONDS);
        Stack<IntPoint> a1Moves = new Stack();
        a1Moves.push(new IntPoint(0, 0));
        a1Moves.push(new IntPoint(1, 1));
        a1Moves.push(new IntPoint(2, 2));
        Stack<IntPoint> a2Moves = new Stack();
        a2Moves.push(new IntPoint(0, 1));
        a2Moves.push(new IntPoint(0, 2));
        a2Moves.push(new IntPoint(1, 2));
        a2Moves.push(new IntPoint(2, 1));

        p1.setProgrammedMoves(a1Moves);
        p2.setProgrammedMoves(a2Moves);

        play.join(p1);
        play.join(p2);
        new BasicGameRunner(play).start();

        int totalMoves = play.getInfo().getBoard().getSelector().getAllFields().stream().filter(el -> el.getOwner().isPresent()).collect(Collectors.toList()).size();
        assertTrue(totalMoves == 5 || totalMoves == 6);
    }

    @Test(timeout = 5000)
    public void testIsWinnerPresent() throws PlayerDisconnectedException {
        play.configure().moveTimeLimit(250, TimeUnit.MILLISECONDS).gameTimeLimit(2, TimeUnit.SECONDS);
        Stack<IntPoint> a1Moves = new Stack();
        a1Moves.push(new IntPoint(0, 0));
        a1Moves.push(new IntPoint(1, 1));
        a1Moves.push(new IntPoint(2, 2));
        Stack<IntPoint> a2Moves = new Stack();
        a2Moves.push(new IntPoint(0, 1));
        a2Moves.push(new IntPoint(0, 2));
        a2Moves.push(new IntPoint(1, 2));
        a2Moves.push(new IntPoint(2, 1));

        p1.setProgrammedMoves(a1Moves);
        p2.setProgrammedMoves(a2Moves);

        play.join(p1);
        play.join(p2);
        new BasicGameRunner(play).start();

        assertTrue(play.getInfo().getWinner().isPresent());
    }

    @Test(timeout = 5000)
    public void testIsWinnerMatchParticipant() throws PlayerDisconnectedException {
        play.configure().moveTimeLimit(250, TimeUnit.MILLISECONDS).gameTimeLimit(2, TimeUnit.SECONDS);
        Stack<IntPoint> a1Moves = new Stack();
        a1Moves.push(new IntPoint(0, 0));
        a1Moves.push(new IntPoint(1, 1));
        a1Moves.push(new IntPoint(2, 2));
        Stack<IntPoint> a2Moves = new Stack();
        a2Moves.push(new IntPoint(0, 1));
        a2Moves.push(new IntPoint(0, 2));
        a2Moves.push(new IntPoint(1, 2));
        a2Moves.push(new IntPoint(2, 1));

        p1.setProgrammedMoves(a1Moves);
        p2.setProgrammedMoves(a2Moves);

        play.join(p1);
        play.join(p2);
        new BasicGameRunner(play).start();

        assertTrue(play.getInfo().getWinner().isPresent());
        Player winner = (Player) play.getInfo().getWinner().get();
        assertEquals(p1, winner);
    }

    @Test(timeout = 5000)
    public void testAllParticipantsWereNotifiedAboutGameEnd() throws PlayerDisconnectedException {
        play.configure().moveTimeLimit(250, TimeUnit.MILLISECONDS).gameTimeLimit(1, TimeUnit.SECONDS);

        p1 = Mockito.spy(TestParticipant.class);
        p1.setUsername("A");
        p2 = Mockito.spy(TestParticipant.class);
        p2.setUsername("B");
        play.join(p1);
        play.join(p2);
        new BasicGameRunner(play).start();

        Mockito.verify(p1, Mockito.times(1)).onGameEnd(Mockito.any(), Mockito.any());
        Mockito.verify(p2, Mockito.times(1)).onGameEnd(Mockito.any(), Mockito.any());
    }

    @Test
    public void testCallbacksProperlyInvoked_TerminatedExecutor() throws PlayerDisconnectedException {
        List onEnd = new ArrayList();
        List onStart = new ArrayList();
        BasicGameRunner exe = new BasicGameRunner(play);
        exe.setCallbacks().addOnEndEvent(() -> onStart.add(1));
        exe.setCallbacks().addOnEndEvent(() -> onStart.add(1));
        exe.setCallbacks().addOnEndEvent(() -> onStart.add(1));

        exe.setCallbacks().addOnEndEvent(() -> onEnd.add(1));
        exe.setCallbacks().addOnEndEvent(() -> onEnd.add(2));
        exe.setCallbacks().addOnEndEvent(() -> onEnd.add(3));

        play.join(p1);
        play.join(p2);
        exe.start();
        exe.interrupt();

        assertEquals(3, onEnd.size());
        assertEquals(3, onStart.size());
    }

    @Test
    public void testExecutorTerminatedProperly() throws InterruptedException, PlayerDisconnectedException {
        play.join(p1);
        play.join(p2);
        play.getSettings().setters().moveTimeLimit(500, TimeUnit.MILLISECONDS);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        executor.schedule(() -> {
            exe.interrupt();
        }, 500, TimeUnit.MILLISECONDS);
        executor.schedule(() -> {
            assertTrue(exe.isRunning());
            assertFalse(exe.isRunning());

        }, 1000, TimeUnit.MILLISECONDS);

        exe.start();

    }

    public IntPoint returnRandomPoint() {
        int randomX = new Random().nextInt(2);
        int randomY = new Random().nextInt(2);
        return new IntPoint(randomX, randomY);
    }

    @Test(timeout = 8000)
    public void testTurnTimeoutEventDelivered() throws PlayerDisconnectedException {
        this.p1.dontRespondOnGetField();

        TestParticipant passive1 = Mockito.spy(TestParticipant.class);
        TestParticipant passive2 = Mockito.spy(TestParticipant.class);

        passive2.setProgrammedMoves(p1.getMoves());
        passive1.dontRespondOnGetField();
        play.join(passive1);
        play.join(passive2);
        play.configure().moveTimeLimit(300, TimeUnit.MILLISECONDS);
        play.configure().gameTimeLimit(600, TimeUnit.MILLISECONDS);
        exe.start();
        Mockito.verify(passive1, Mockito.atLeastOnce()).onTurnTimeout();
        Mockito.verify(passive2, Mockito.times(0)).onTurnTimeout();
    }

    @Test(timeout = 500, expected = PlayerDisconnectedException.class)
    public void testGameEndDuePlayerDisconnected_WinsPresentPlayer() throws Exception {
        TestParticipant passive1 = Mockito.spy(TestParticipant.class);
        TestParticipant passive2 = Mockito.spy(TestParticipant.class);
        passive2.setProgrammedMoves(p1.getMoves());
        passive2.getMoves().remove(0); // prevents situation when passive2 wins - and therefore - games ends naturally.

        passive1.dontRespondOnGetField();
        passive1.dontRespondOnConnected();

        play.configure().moveTimeLimit(100, TimeUnit.MILLISECONDS);
        play.configure().gameTimeLimit(1000, TimeUnit.MILLISECONDS);
        play.join(passive1);
        play.join(passive2);
        exe = new BasicGameRunner(play);
        exe.start();
        Mockito.verify(passive2, Mockito.atLeastOnce()).onGameEnd(Mockito.any(), Mockito.any());
        Mockito.verify(passive1, Mockito.atLeastOnce()).onGameEnd(Mockito.any(), Mockito.any());

        Mockito.verify(passive1, Mockito.never()).onTurnTimeout();
        Mockito.verify(passive1, Mockito.never()).getMoveField(Mockito.any());

        assertEquals(GameRunnerStatus.Interrupted, exe.getStatus());
        assertTrue(play.getInfo().getWinner().isPresent());
        Player winner = (Player) play.getInfo().getWinner().get();
        assertEquals(passive2, winner);
    }

    @Test(timeout = 500, expected = PlayerDisconnectedException.class)
    public void testGameEndDueTwoPlayersDisconnected_WinsSecondTurnPlayer() throws Exception, Exception {
        TestParticipant passive1 = Mockito.spy(TestParticipant.class);
        TestParticipant passive2 = Mockito.spy(TestParticipant.class);

        passive1.dontRespondOnGetField();
        passive2.dontRespondOnConnected();
        passive2.dontRespondOnGetField();
        passive1.dontRespondOnConnected();

        play.configure().moveTimeLimit(100, TimeUnit.MILLISECONDS);
        play.configure().gameTimeLimit(1000, TimeUnit.MILLISECONDS);
        play.join(passive1);
        play.join(passive2);
        exe = new BasicGameRunner(play);
        exe.start();
        Mockito.verify(passive2, Mockito.atLeastOnce()).onGameEnd(Mockito.any(), Mockito.any());
        Mockito.verify(passive1, Mockito.atLeastOnce()).onGameEnd(Mockito.any(), Mockito.any());

        Mockito.verify(passive1, Mockito.never()).onTurnTimeout();
        Mockito.verify(passive1, Mockito.never()).getMoveField(Mockito.any());
        Mockito.verify(passive2, Mockito.never()).onTurnTimeout();
        Mockito.verify(passive2, Mockito.never()).getMoveField(Mockito.any());

        assertEquals(GameRunnerStatus.Interrupted, exe.getStatus());
        assertTrue(play.getInfo().getWinner().isPresent());
    }

}
