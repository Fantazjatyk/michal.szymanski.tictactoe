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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tictactoe.model.Board;
import tictactoe.model.BoardField;
import tictactoe.model.IntPoint;
import tictactoe.model.Move;
import tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
abstract class GameMaster {

    private GameMaster() {

    }

    public static boolean isDone(Board board) {
        return !getWinners(board).isEmpty();
    }

    public static List<Player> getWinners(Board board) {
        List<BoardField[]> possibleLines = board.getSelector().getAllPossibleWinningLines();
        Set<Player> players = findAllPlayersOnBoard(board);

        if (players.isEmpty()) {
            return new ArrayList();
        }

        List<Player> winners = lookForWinners(possibleLines, players);
        if (winners.isEmpty() && possibleLines.stream().allMatch(line -> Stream.of(line).allMatch(field -> field.getOwner().isPresent()))) {
            winners.addAll(players);
        }
        return winners;
    }

    private static List<Player> lookForWinners(List<BoardField[]> lines, Set<Player> players) {
        return players.stream().filter(player
                -> lines
                        .stream()
                        .anyMatch(line -> Stream.of(line)
                        .allMatch(field -> field.getOwner().isPresent() && field.getOwner().get().equals(player)))
        ).collect(Collectors.toList());
    }

    public static List<BoardField[]> getWinCombinations(List<BoardField[]> lines) {
        return lines.stream()
                .filter(el -> areLineOwnedOnlyByOnePlayer(el)).collect(Collectors.toList());
    }

    public static boolean areLineOwnedOnlyByOnePlayer(BoardField[] line) {

        Player p = null;
        int matches = 0;
        for (BoardField f : line) {
            if (f.getOwner().isPresent()) {
                if (p == null) {
                    p = f.getOwner().get();
                    matches++;
                } else if (!(f.getOwner().get().equals(p))) {
                    return false;
                } else {
                    matches++;
                }
            }
        }
        return matches == line.length;

    }

    private static Set<Player> findAllPlayersOnBoard(Board board) {
        return board.getSelector().getAllFields().stream().map(el -> el.getOwner().orElse(null)).filter(el -> el != null).collect(Collectors.toSet());
    }

    public static boolean isValidMove(Move move, Board board) {
        IntPoint field = move.getPoint();
        return !board.getBoard()[field.getY()][(int) field.getX()].getOwner().isPresent();
    }

}
