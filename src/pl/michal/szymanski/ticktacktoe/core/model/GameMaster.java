/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import pl.michal.szymanski.ticktacktoe.transport.Participant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class GameMaster {

    public static boolean isDone(Board board) {
        return getWinner(board).isPresent();
    }

    public static Optional<Player> getWinner(Board board) {
        List<BoardField[]> possibleLines = new ArrayList();
        possibleLines.addAll(board.getDiagonals());
        possibleLines.addAll(board.getRows());

        Set<Player> players = findAllPlayersOnBoard(board);
        if (players.isEmpty()) {
            return Optional.empty();
        }
        List<Player> winners = players.stream().filter(player
                -> possibleLines
                        .stream()
                        .anyMatch(line -> Stream.of(line)
                        .allMatch(field -> field.getOwner().isPresent() && field.getOwner().get().equals(player)))
        ).collect(Collectors.toList());

        return winners.size() == 1 ? Optional.of(winners.get(0)) : Optional.empty();
    }

    private static Set<Player> findAllPlayersOnBoard(Board board) {
        return board.getAllFields().stream().map(el -> el.getOwner().orElse(null)).filter(el -> el != null).collect(Collectors.toSet());
    }

    public static boolean isValidMove(Move move) {
        return true;
    }

}
