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

import pl.michal.szymanski.ai.tictactoe.ContextAwareAI;
import pl.michal.szymanski.ai.tictactoe.behavior.EasyAIBehavior;
import pl.michal.szymanski.ai.tictactoe.behavior.MediumAIBehavior;
import tictactoe.ai.AIAdapter;
import tictactoe.ai.AILevel;
import tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class SingleplayerPlay extends Play {

    private AIAdapter aiAdapter;

    public SingleplayerPlay() {
        this.aiAdapter = new AIAdapter(new ContextAwareAI());
        super.join(aiAdapter);
    }

    public void setAILevel(AILevel level) {

        switch (level) {
            case Easy:
                aiAdapter.getAI().setBehavior(new EasyAIBehavior());
                this.aiAdapter.setUsername("Easy AI");
                break;
            case Medium:
                aiAdapter.getAI().setBehavior(new MediumAIBehavior());
                this.aiAdapter.setUsername("Medium AI");
                break;
            default:
                throw new UnsupportedOperationException();

        }
    }

    public Player getAiPlayer() {
        return (Player) super.getInfo().getPlayers().getPlayer(aiAdapter.getUsername()).get();
    }

}
