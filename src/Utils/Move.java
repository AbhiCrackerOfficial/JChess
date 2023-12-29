package Utils;

import java.util.HashMap;
import java.util.Map;

public class Move {
    private static final Map<Integer, String> ranksToRows = new HashMap<>();
    private static final Map<Integer, String> filesToCols = new HashMap<>();

    static {
        ranksToRows.put(7, "1");
        ranksToRows.put(6, "2");
        ranksToRows.put(5, "3");
        ranksToRows.put(4, "4");
        ranksToRows.put(3, "5");
        ranksToRows.put(2, "6");
        ranksToRows.put(1, "7");
        ranksToRows.put(0, "8");

        filesToCols.put(0, "a");
        filesToCols.put(1, "b");
        filesToCols.put(2, "c");
        filesToCols.put(3, "d");
        filesToCols.put(4, "e");
        filesToCols.put(5, "f");
        filesToCols.put(6, "g");
        filesToCols.put(7, "h");
    }

    public int startRow;
    public int startCol;
    public int endRow;
    public int endCol;
    public String pieceMoved;
    public String pieceCaptured;
    public boolean isPawnPromotion;
    public boolean isCastleMove;
    private int moveID;

    public Move(int startRow, int startCol, int endRow, int endCol, String[][] board, boolean isCastleMove) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.pieceMoved = board[startRow][startCol];
        this.pieceCaptured = board[endRow][endCol];
        this.isPawnPromotion = false;
        this.isCastleMove = isCastleMove;

        if ((pieceMoved.equals("wp") && endRow == 0) || (pieceMoved.equals("bp") && endRow == 7)) {
            this.isPawnPromotion = true;
        }

        this.moveID = startRow * 1000 + startCol * 100 + endRow * 10 + endCol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Move) {
            Move other = (Move) obj;
            return this.moveID == other.moveID;
        }
        return false;
    }

    public String getChessNotation() {
        return getRankFile(startRow, startCol) + getRankFile(endRow, endCol);
    }

    private String getRankFile(int r, int c) {
        // System.out.println(filesToCols.get(c) + "|" + ranksToRows.get(r));
        return filesToCols.get(c) + ranksToRows.get(r);
    }
}
