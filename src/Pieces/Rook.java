package Pieces;

import java.util.List;

import Utils.Move;

public class Rook {
    public static void getRookMoves(int r, int c, List<Move> moves, String[][] board, boolean whiteToMove) {
        int[][] directions = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
        String enemyColor = whiteToMove ? "b" : "w";

        for (int[] direction : directions) {
            int dRow = direction[0];
            int dCol = direction[1];
            for (int i = 1; i < 8; i++) {
                int endRow = r + dRow * i;
                int endCol = c + dCol * i;

                if (endRow >= 0 && endRow < 8 && endCol >= 0 && endCol < 8) {
                    String endPiece = board[endRow][endCol];
                    if (endPiece.equals("--")) {
                        moves.add(new Move(r, c, endRow, endCol, board, false));
                    } else {
                        if (endPiece.charAt(0) == enemyColor.charAt(0)) {
                            moves.add(new Move(r, c, endRow, endCol, board, false));
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

}
