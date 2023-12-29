package Engines;

import java.util.List;
import java.util.Random;

import Utils.*;

public class AiEngine {

    private static final int CHECKMATE = 1000;
    private static final int STALEMATE = 0;

    // private static final char[] PIECE_ORDER = { 'K', 'Q', 'R', 'B', 'N', 'P' };

    private static int pieceScore(char piece) {
        switch (Character.toUpperCase(piece)) {
            case 'K':
                return 0;
            case 'Q':
                return 9;
            case 'R':
                return 5;
            case 'B':
            case 'N':
                return 3;
            case 'P':
                return 1;
            default:
                return 0;
        }
    }

    public static Move randomMove(List<Move> validMoves) {
        if (validMoves == null || validMoves.isEmpty()) {
            return null; // or handle the case where there are no valid moves
        }

        Random random = new Random();
        int randomIndex = random.nextInt(validMoves.size());
        return validMoves.get(randomIndex);
    }

    public static Move bestMove(ChessEngine gs, List<Move> validMoves) {
        int turnMultiplier = gs.whiteToMove ? 1 : -1;
        int opponentMinMaxScore = CHECKMATE;
        Move bestPlayerMove = null;
        randomizeList(validMoves);

        for (Move playerMove : validMoves) {
            gs.makeMove(playerMove, false);
            List<Move> opponentsMoves = gs.getValidMoves();
            int opponentMaxScore = -CHECKMATE;

            for (Move opponentsMove : opponentsMoves) {
                gs.makeMove(opponentsMove, false);
                int score;
                if (gs.checkMate) {
                    score = -turnMultiplier * CHECKMATE;
                } else if (gs.staleMate) {
                    score = STALEMATE;
                } else {
                    score = -turnMultiplier * scoreMaterial(gs.board);
                }

                if (score > opponentMaxScore) {
                    opponentMaxScore = score;
                }

                gs.undoMove();
            }

            if (opponentMaxScore < opponentMinMaxScore) {
                opponentMinMaxScore = opponentMaxScore;
                bestPlayerMove = playerMove;
            }

            gs.undoMove();
        }

        return bestPlayerMove;
    }

    private static void randomizeList(List<Move> moves) {
        Random random = new Random();
        for (int i = moves.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Move temp = moves.get(i);
            moves.set(i, moves.get(j));
            moves.set(j, temp);
        }
    }

    private static int scoreMaterial(String[][] board) {
        int score = 0;
        for (String[] row : board) {
            for (String square : row) {
                if (square.charAt(0) == 'w') {
                    score += pieceScore(square.charAt(1));
                } else if (square.charAt(0) == 'b') {
                    score -= pieceScore(square.charAt(1));
                }
            }
        }
        return score;
    }
}
