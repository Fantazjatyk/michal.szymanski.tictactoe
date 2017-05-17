/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.transport;

import pl.michal.szymanski.tictactoe.core.PlayFlow;
import pl.michal.szymanski.tictactoe.core.Board;
import pl.michal.szymanski.tictactoe.core.PlayInfo;
import pl.michal.szymanski.tictactoe.core.PlaySettings;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface GameWatcher {

    void receiveBoard(Board board);

    void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings);
}
