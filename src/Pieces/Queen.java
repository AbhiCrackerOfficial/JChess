package Pieces;

import java.util.List;

import Utils.Move;

public class Queen {
    public static void getQueenMoves(int r, int c, List<Move> moves, String[][] board, boolean whiteToMove) {
        Rook.getRookMoves(r, c, moves, board, whiteToMove);
        Bishop.getBishopMoves(r, c, moves, board, whiteToMove);
    }
}
