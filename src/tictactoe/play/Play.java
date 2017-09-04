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

import tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Play {

    private PlaySettings settings = new PlaySettings();
    private PlayHistory history = new PlayHistory();
    private PlayInfo info = new PlayInfo();

    public PlayInfo getInfo() {
        return info;
    }

    public PlayHistory getHistory() {
        return history;
    }

    public PlaySettings.PlaySettingsSetters settings() {
        return this.settings.setters();
    }

    public void onStart() {
        info.getPlayers().assignBoardFieldsMarks();
    }

    public void onFinish() {
        this.info.getPlayers().getFirstPlayer().get().onGameEnd(info, settings.getters());
        this.info.getPlayers().getSecondPlayer().get().onGameEnd(info, settings.getters());
    }

    public PlaySettings getSettings() {
        return this.settings;
    }

    public void join(Player player) {
        if (!this.getInfo().getPlayers().getFirstPlayer().isPresent()) {
            this.getInfo().getPlayers().firstPlayer(player);
        } else if (!this.getInfo().getPlayers().getSecondPlayer().isPresent()) {
            this.getInfo().getPlayers().secondPlayer(player);
        }

    }

}
