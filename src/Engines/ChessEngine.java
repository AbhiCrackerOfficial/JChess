package Engines;

import java.util.*;
import javax.sound.sampled.*;
import java.io.File;
import Utils.*;
import Pieces.*;

public class ChessEngine {
    public String[][] board = {
            { "bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR" },
            { "bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp" },
            { "--", "--", "--", "--", "--", "--", "--", "--" },
            { "--", "--", "--", "--", "--", "--", "--", "--" },
            { "--", "--", "--", "--", "--", "--", "--", "--" },
            { "--", "--", "--", "--", "--", "--", "--", "--" },
            { "wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp" },
            { "wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR" }
    };

    public boolean whiteToMove = true;
    public List<Move> moveLog = new ArrayList<>();
    private Pair<Integer, Integer> whiteKingLocation = new Pair<>(7, 4);
    private Pair<Integer, Integer> blackKingLocation = new Pair<>(0, 4);
    public boolean staleMate = false;
    public boolean checkMate = false;

    public void makeMove(Move move, Boolean sound) {
        if (move == null)
            return;
        board[move.startRow][move.startCol] = "--";
        board[move.endRow][move.endCol] = move.pieceMoved;
        moveLog.add(move);
        whiteToMove = !whiteToMove;

        if (sound) {
            if (move.pieceCaptured != "--")
                new Thread(() -> {
                    playSound("Resources/Sounds/capture.wav");
                }).start();
            else
                new Thread(() -> {
                    playSound("Resources/Sounds/move.wav");
                }).start();
        }

        if (move.pieceMoved.equals("wK")) {
            whiteKingLocation.setFirst(move.endRow);
            whiteKingLocation.setSecond(move.endCol);
        } else if (move.pieceMoved.equals("bK")) {
            blackKingLocation.setFirst(move.endRow);
            blackKingLocation.setSecond(move.endCol);
        }

        if (move.isPawnPromotion) {
            board[move.endRow][move.endCol] = move.pieceMoved.charAt(0) + "Q";
        }

    }

    public void undoMove() {
        if (!moveLog.isEmpty()) {
            Move move = moveLog.remove(moveLog.size() - 1);
            board[move.startRow][move.startCol] = move.pieceMoved;
            board[move.endRow][move.endCol] = move.pieceCaptured;
            whiteToMove = !whiteToMove;

            if (move.pieceMoved.equals("wK")) {
                whiteKingLocation.setFirst(move.startRow);
                whiteKingLocation.setSecond(move.startCol);
            } else if (move.pieceMoved.equals("bK")) {
                blackKingLocation.setFirst(move.startRow);
                blackKingLocation.setSecond(move.startCol);
            }
        }
    }

    public List<Move> getValidMoves() {
        List<Move> moves = getAllPossibleMoves();

        for (int i = moves.size() - 1; i >= 0; i--) {
            makeMove(moves.get(i), false);
            whiteToMove = !whiteToMove;

            if (inCheck()) {
                moves.remove(i);
            }

            whiteToMove = !whiteToMove;
            undoMove();
        }

        if (moves.isEmpty()) {
            if (inCheck()) {
                checkMate = true;
            } else {
                staleMate = true;
            }
        } else {
            checkMate = false;
            staleMate = false;
        }

        return moves;
    }

    public boolean inCheck() {
        int kingRow, kingCol;
        if (whiteToMove) {
            kingRow = whiteKingLocation.getFirst();
            kingCol = whiteKingLocation.getSecond();
        } else {
            kingRow = blackKingLocation.getFirst();
            kingCol = blackKingLocation.getSecond();
        }
        return squareUnderAttack(kingRow, kingCol);
    }

    public boolean squareUnderAttack(int r, int c) {
        whiteToMove = !whiteToMove;
        List<Move> oppMoves = getAllPossibleMoves();
        whiteToMove = !whiteToMove;
        for (Move move : oppMoves) {
            if (move.endRow == r && move.endCol == c) {
                return true;
            }
        }
        return false;
    }

    public List<Move> getAllPossibleMoves() {
        List<Move> moves = new ArrayList<>();

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                String piece = board[r][c];
                char turn = piece.charAt(0);

                if ((turn == 'w' && whiteToMove) || (turn == 'b' && !whiteToMove)) {
                    switch (piece.charAt(1)) {
                        case 'p':
                            Pawn.getPawnMoves(r, c, moves, board, whiteToMove);
                            break;
                        case 'R':
                            Rook.getRookMoves(r, c, moves, board, whiteToMove);
                            break;
                        case 'N':
                            Knight.getKnightMoves(r, c, moves, board, whiteToMove);
                            break;
                        case 'B':
                            Bishop.getBishopMoves(r, c, moves, board, whiteToMove);
                            break;
                        case 'Q':
                            Queen.getQueenMoves(r, c, moves, board, whiteToMove);
                            break;
                        case 'K':
                            King.getKingMoves(r, c, moves, board, whiteToMove);
                            break;
                    }
                }
            }
        }

        return moves;
    }

    public void playSound(String soundFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP)
                        clip.close();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
