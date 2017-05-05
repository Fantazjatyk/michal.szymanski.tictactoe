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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import pl.michal.szymanski.ticktacktoe.transport.Connector;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class GameMaster {

    public static boolean isDone(Board board) {
        return false;
    }

    public static Player getWinner(Board board) {
        Map<Player, List<BoardField>> map = mapBoard(board);
        List result = map.entrySet().stream().sorted((a, b)
                -> ((Integer) b.getValue().size()).compareTo(((Integer) a.getValue().size()))
        ).map((el) -> el.getKey()).collect(Collectors.toList());

        return result.isEmpty() || isRemis(map, result) ? null : (Player) result.get(0);

    }

    private static boolean isRemis(Map<Player, List<BoardField>> map, List<Player> result) {
        boolean test = !(result.size() <= 1) && map.get(result.get(0)).size() == map.get(result.get(1)).size();
        return test;
    }

    private static Map<Player, List<BoardField>> mapBoard(Board board) {
        Map<Player, List<BoardField>> map = new HashMap();
        Stream.of(board.getBoard()).flatMap(x -> Arrays.stream(x)).filter((el) -> el.getOwner().isPresent())
                .forEach(el -> {
                    Player owner = el.getOwner().get();
                    if (map.containsKey(owner)) {
                        map.get(owner).add(el);
                    } else {
                        List list = new ArrayList();
                        list.add(el);
                        map.put(owner, list);
                    }
                });

        return map;
    }

}
