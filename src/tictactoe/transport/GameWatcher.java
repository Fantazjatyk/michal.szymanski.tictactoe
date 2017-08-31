/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.transport;

import tictactoe.model.Board;
import tictactoe.play.PlayInfo;
import tictactoe.play.PlaySettings;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface GameWatcher {

    void receiveBoard(Board board);

    void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings);
}
