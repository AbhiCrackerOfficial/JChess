package Pieces;

import java.util.List;

import Utils.Move;

public class Pawn {
    public static void getPawnMoves(int r, int c, List<Move> moves, String[][] board, boolean whiteToMove) {
        if (whiteToMove) {
            if (r - 1 >= 0 && board[r - 1][c].equals("--")) {
                moves.add(new Move(r, c, r - 1, c, board, false));
                if (r == 6 && board[r - 2][c].equals("--")) {
                    moves.add(new Move(r, c, r - 2, c, board, false));
                }
            }
            if (r - 1 >= 0 && c - 1 >= 0 && !board[r - 1][c - 1].equals("--") && board[r - 1][c - 1].charAt(0) == 'b') {
                moves.add(new Move(r, c, r - 1, c - 1, board, false));
            }
            if (r - 1 >= 0 && c + 1 <= 7 && !board[r - 1][c + 1].equals("--") && board[r - 1][c + 1].charAt(0) == 'b') {
                moves.add(new Move(r, c, r - 1, c + 1, board, false));
            }
        } else {
            if (r + 1 <= 7 && board[r + 1][c].equals("--")) {
                moves.add(new Move(r, c, r + 1, c, board, false));
                if (r == 1 && board[r + 2][c].equals("--")) {
                    moves.add(new Move(r, c, r + 2, c, board, false));
                }
            }
            if (r + 1 <= 7 && c - 1 >= 0 && !board[r + 1][c - 1].equals("--") && board[r + 1][c - 1].charAt(0) == 'w') {
                moves.add(new Move(r, c, r + 1, c - 1, board, false));
            }
            if (r + 1 <= 7 && c + 1 <= 7 && !board[r + 1][c + 1].equals("--") && board[r + 1][c + 1].charAt(0) == 'w') {
                moves.add(new Move(r, c, r + 1, c + 1, board, false));
            }
        }
    }

}
