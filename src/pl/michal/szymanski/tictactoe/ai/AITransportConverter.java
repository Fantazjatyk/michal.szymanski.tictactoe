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
package pl.michal.szymanski.tictactoe.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.michal.szymanski.tictactoe.model.Board;
import pl.michal.szymanski.tictactoe.model.BoardField;
import pl.michal.szymanski.tictactoe.model.Player;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface AITransportConverter {

    /*
    Three Valued Logic: -1, 0, 1, is equivalent to: not your, unknown, your
     */
    public static int[][] convertBoardToThreeValuedMatrix(Board board, String yourId) {
        int[][] result = new int[board.getSizeY()][board.getSizeX()];
        List<BoardField[]> rows = board.getSelector().getRows();
        List<int[]> list = new ArrayList();
        rows.stream().map(el -> convertBoardLineToThreeValuedType(el, yourId)).forEach(el2 -> list.add(el2));

        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static int[] convertBoardLineToThreeValuedType(BoardField[] line, String yourId) {
        return Stream.of(line).mapToInt(el2 -> convertBoardFieldToThreeValuedType(el2, yourId)).toArray();
    }

    public static int convertBoardFieldToThreeValuedType(BoardField f, String yourId) {
        Optional<Player> fieldOwner = f.getOwner();
        if (!fieldOwner.isPresent()) {
            return 0;
        } else {
            return fieldOwner.get().getId().equals(yourId) ? 1 : -1;
        }
    }
}
