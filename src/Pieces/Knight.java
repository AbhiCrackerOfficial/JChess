package Pieces;

import java.util.List;

import Utils.Move;

public class Knight {
    public static void getKnightMoves(int r, int c, List<Move> moves, String[][] board, boolean whiteToMove) {
        int[][] directions = { { -2, -1 }, { -2, 1 }, { 2, -1 }, { 2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 } };
        String enemyColor = whiteToMove ? "w" : "b";

        for (int[] direction : directions) {
            int dRow = direction[0];
            int dCol = direction[1];
            int endRow = r + dRow;
            int endCol = c + dCol;

            if (endRow >= 0 && endRow < 8 && endCol >= 0 && endCol < 8) {
                String endPiece = board[endRow][endCol];
                if (endPiece.charAt(0) != enemyColor.charAt(0)) {
                    moves.add(new Move(r, c, endRow, endCol, board, false));
                }
            }
        }
    }

}
