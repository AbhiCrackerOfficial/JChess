package Chess;

import javax.swing.*;
import Engines.*;
import Utils.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.awt.event.*;
import java.util.ArrayList;

public class ChessBoard {
    private JFrame frame;
    DbEngine db = new DbEngine();
    private BoardPanel boardPanel;
    private ChessEngine gameState;
    private static final int SQ_SIZE = 90;
    private Pair<Integer, Integer> selectedTile;
    private List<Pair<Integer, Integer>> playerClicks;
    private boolean GameOver = false, inProgress = true;
    private boolean playerone, playertwo;
    InfoBoard info;
    String whitePlayer, blackPlayer;

    public ChessBoard(boolean p1, boolean p2, InfoBoard info, String whitePlayer, String blackPlayer) {
        frame = new JFrame("Chess Game");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(736, 758);
        frame.setResizable(false);
        gameState = new ChessEngine();
        boardPanel = new BoardPanel();
        selectedTile = null;
        playerClicks = new ArrayList<>();
        // boolean playerone = p1, playertwo = p2;
        this.playerone = p1;
        this.playertwo = p2;
        this.info = info;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'z') {
                    gameState.undoMove();
                    info.UpdateMoves(gameState.moveLog);
                    boardPanel.repaint();
                } else if (e.getKeyChar() == 'r') {
                    inProgress = false;
                    info.dispose();
                    frame.dispose();
                    new InitEngine();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'z') {
                    gameState.undoMove();
                    info.UpdateMoves(gameState.moveLog);
                    boardPanel.repaint();
                } else if (e.getKeyChar() == 'r') {
                    new Thread(() -> {
                        info.dispose();
                        frame.dispose();
                    }).start();
                    new Thread(() -> {
                        new InitEngine();
                    }).start();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        boardPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        startGame();
    }

    private void handleMouseClick(MouseEvent e) {
        boolean humanTurn = (gameState.whiteToMove && playerone) || (!gameState.whiteToMove && playertwo);

        // Ensure it's the player's turn and the game isn't over
        if (!GameOver && humanTurn) {
            int x = e.getY() / (758 / 8);
            int y = e.getX() / (736 / 8);

            // If no tile is selected, select the current tile
            if (selectedTile == null) {
                selectedTile = new Pair<>(x, y);
                playerClicks.add(selectedTile);
            } else {
                // If a tile is already selected, make the move
                playerClicks.add(new Pair<>(x, y));
                Pair<Integer, Integer> startTile = playerClicks.get(0);
                Pair<Integer, Integer> endTile = playerClicks.get(1);
                Move move = new Move(startTile.getFirst(), startTile.getSecond(), endTile.getFirst(),
                        endTile.getSecond(), gameState.board, false);

                if (gameState.getValidMoves().contains(move)) {
                    gameState.makeMove(move, true);
                    info.UpdateMoves(gameState.moveLog);

                    // Print the board state after the move
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            System.out.print(gameState.board[i][j] + " ");
                        }
                        System.out.println(); // Move to the next row
                    }
                    System.out.println("################################################");
                }

                // Reset selected tile and clicks
                selectedTile = null;
                playerClicks.clear();
            }
        }

        // Check for game over conditions and repaint the board
        decide(info, gameState, whitePlayer, blackPlayer);
        boardPanel.repaint();

        // Highlight valid moves after a short delay for better visualization
        new Thread(() -> {
            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            boardPanel.highlightSquares(gameState, gameState.getValidMoves(), selectedTile);
        }).start();
    }

    private void makeAIMove() {
        Move aiMove = AiEngine.bestMove(gameState, gameState.getValidMoves());
        if (aiMove == null)
            aiMove = AiEngine.randomMove(gameState.getValidMoves());
        gameState.makeMove(aiMove, true);
        info.UpdateMoves(gameState.moveLog);

        // Check for game over conditions and repaint the board after AI's move
        decide(info, gameState, whitePlayer, blackPlayer);
        boardPanel.repaint();
    }

    public void startGame() {
        // Start a new thread to continuously check and make AI moves
        new Thread(() -> {
            while (!GameOver && inProgress) {
                decide(info, gameState, whitePlayer, blackPlayer);
                boolean humanTurn = (gameState.whiteToMove && playerone) || (!gameState.whiteToMove && playertwo);
                if (!GameOver && !humanTurn) {
                    makeAIMove();
                }
                try {
                    // Adjust the delay between AI moves (in milliseconds) as needed
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    void decide(InfoBoard info, ChessEngine gameState, String whitePlayer, String blackPlayer) {
        String s = new String();
        for (Move move : gameState.moveLog) {
            s += move.getChessNotation() + ",";
        }
        if (gameState.checkMate) {
            GameOver = true;
            String winner = gameState.whiteToMove ? "Black" : "White";
            JOptionPane.showMessageDialog(null, winner + " Wins", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            db.run("INSERT INTO `matches`( `Player1Name`, `Player2Name`, `Result`, `MoveLogs`) VALUES ('"
                    + whitePlayer + "','" + blackPlayer + "','" + winner + " wins','" + s + "')");
            info.dispose();
            frame.dispose();
            new InitEngine();
        } else if (gameState.staleMate) {
            GameOver = true;
            JOptionPane.showMessageDialog(null, "Stalemate", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            db.run("INSERT INTO `matches`( `Player1Name`, `Player2Name`, `Result`, `MoveLogs`) VALUES ('"
                    + whitePlayer + "','" + blackPlayer + "','Stalemate','" + s + "')");
            info.dispose();
            frame.dispose();
            new InitEngine();
        }
    }

    private class BoardPanel extends JPanel {
        private static final int TILE_SIZE = 90;
        private static final int BOARD_SIZE = 8;

        private HashMap<String, ImageIcon> images;

        private void loadImages() {
            images = new HashMap<>();

            String[] pieces = { "wp", "wR", "wN", "wB", "wK", "wQ", "bp", "bR", "bN", "bB", "bK", "bQ" };

            for (String piece : pieces) {
                ImageIcon imageIcon = new ImageIcon("Resources/Images/" + piece + ".png");
                Image image = imageIcon.getImage().getScaledInstance(SQ_SIZE, SQ_SIZE, Image.SCALE_SMOOTH);
                images.put(piece, new ImageIcon(image));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            loadImages();
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    Color color = (row + col) % 2 == 0 ? new Color(183, 152, 115) : new Color(115, 83, 58);
                    g.setColor(color);
                    g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    String piece = gameState.board[row][col];
                    if (!piece.equals("--")) {
                        ImageIcon pieceImage = images.get(piece);
                        pieceImage.paintIcon(this, g, col * TILE_SIZE, row * TILE_SIZE);
                    }
                }
            }

            if (selectedTile != null) {
                g2d.setColor(Color.BLUE);
                Stroke oldStroke = g2d.getStroke();
                float borderWidth = 4.0f;
                g2d.setStroke(new BasicStroke(borderWidth));
                int x = selectedTile.getSecond() * TILE_SIZE;
                int y = selectedTile.getFirst() * TILE_SIZE;
                g2d.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                g2d.setStroke(oldStroke);
            }

        }

        private void highlightSquares(ChessEngine gs, List<Move> validMoves,
                Pair<Integer, Integer> sqSelected) {
            Graphics g = getGraphics();
            if (sqSelected != null) {
                int r = sqSelected.getFirst();
                int c = sqSelected.getSecond();

                if (gs.board[r][c].charAt(0) == (gs.whiteToMove ? 'w' : 'b')) {

                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillRect(c * SQ_SIZE, r * SQ_SIZE, SQ_SIZE, SQ_SIZE);

                    g.setColor(new Color(255, 255, 0, 100));
                    for (Move move : validMoves) {
                        if (move.startRow == r && move.startCol == c) {
                            g.fillRect(move.endCol * SQ_SIZE, move.endRow * SQ_SIZE, SQ_SIZE, SQ_SIZE);
                        }
                    }
                }
            }
        }

    }

}
