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

import java.util.List;
import java.util.Optional;
import tictactoe.model.GameResult;
import tictactoe.model.GameResult.GameResultStatus;
import tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
class GameResultSimpleFactory {

    public static GameResult createGameResult(Game game, BasicGameRunner runner) {

        List<Player> winners = GameMaster.getWinners(runner.getBoard());
        GameResultStatus status = evaluateStatus(winners, game);
        GameResult.GameResultBuilder b = new GameResult.GameResultBuilder();
        b.setStatus(status);

        if (winners.size() > 1) {
            b.setStatus(GameResult.GameResultStatus.Remis);
        } else {
            b.setWinner(Optional.of(winners.get(0)));
        }

        return b.build();
    }

    private static GameResultStatus evaluateStatus(List<Player> winners, Game game) {
        GameResult.GameResultStatus status = null;
        if (game.getStatus() == GameRunner.GameRunnerStatus.Done) {
            if (winners.size() == 1) {
                if (game.getPlayers().filter(el -> el.getId() != winners.get(0).getId() && el.isIsDisqualified()).isPresent()) {
                    status = GameResult.GameResultStatus.Walkover;
                } else {
                    status = GameResult.GameResultStatus.Winner;
                }

            } else {
                status = GameResult.GameResultStatus.Remis;
            }
        }
        return status;
    }
}
