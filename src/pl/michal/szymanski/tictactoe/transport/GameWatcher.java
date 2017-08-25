/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.transport;

import pl.michal.szymanski.tictactoe.model.Board;
import pl.michal.szymanski.tictactoe.play.PlayInfo;
import pl.michal.szymanski.tictactoe.play.PlaySettings;
import pl.michal.szymanski.tictactoe.transport.*;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface GameWatcher {

    void receiveBoard(Board board);

    void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings);
}
