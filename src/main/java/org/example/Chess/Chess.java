package org.example.Chess;

import org.example.Chess.exception.WrongPieceException;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Chess {
    public static int rowCount = 8;
    public static int columnCount = 8;
    private boolean gameOver = false;
    private PieceInformation[][] field = new PieceInformation[rowCount][columnCount];

    public Chess() {
        field = getTheInitialPositionOfThePieces();
    }

    public PieceInformation getPieceInformation(int row, int column) {
        return field[row][column];
    }

    public PieceInformation getPieceInformation(IntPair position) {
        return getPieceInformation(position.first(), position.second());
    }

    static public PieceInformation[][] getTheInitialPositionOfThePieces() {
        PieceInformation[][] field = new PieceInformation[rowCount][columnCount];
        for (int j = 0; j < columnCount; ++j) {
            field[1][j] = new PieceInformation(ChessPiece.Pawn, Player.Black);
            field[rowCount - 1 - 1][j] = new PieceInformation(ChessPiece.Pawn, Player.White);
        }

        ChessPiece[] pieces = {ChessPiece.Rook, ChessPiece.Knight, ChessPiece.Bishop, ChessPiece.Queen};
        for (int j = 0; j < pieces.length; ++j) {
            ChessPiece piece = pieces[j];
            field[0][j] = field[0][columnCount - 1 - j] = new PieceInformation(piece, Player.Black);
            field[rowCount - 1][j] = field[rowCount - 1][columnCount - 1 - j] = new PieceInformation(piece, Player.White);
        }
        field[0][4] = new PieceInformation(ChessPiece.King, Player.Black);
        field[rowCount - 1][4] = new PieceInformation(ChessPiece.King, Player.White);
        return field;
    }

    public PieceInformation[][] getCurrentStateOfField() {
        PieceInformation[][] copy = new PieceInformation[rowCount][columnCount];
        for (int i = 0; i < rowCount; ++i) {
            System.arraycopy(field[i], 0, copy[i], 0, columnCount);
        }
        return copy;
    }

    public boolean isFreePlace(IntPair position) {
        return isFreePlace(position.first(), position.second());
    }

    public boolean isFreePlace(int i, int j) {
        return isRightPosition(i, j) && field[i][j] == null;
    }

    public boolean isRightPosition(IntPair position) {
        return isRightPosition(position.first(), position.second());
    }

    public boolean isRightPosition(int i, int j) {
        return 0 <= i && i < rowCount && 0 <= j && j < columnCount;
    }

    public boolean isThereCheckTo(Player player) {
        return false;
    }

    public List<IntPair> getPossibleMoves(IntPair currentPosition) {
        return switch (field[currentPosition.first()][currentPosition.second()].chessPiece()) {
            case Rook -> getPossibleMovesForRook(currentPosition);
            case Knight -> getPossibleMovesForKnight(currentPosition);
            case Bishop -> getPossibleMovesForBishop(currentPosition);
            case Queen -> getPossibleMovesForQueen(currentPosition);
            case King -> getPossibleMovesForKing(currentPosition);
            case Pawn -> getPossibleMovesForPawn(currentPosition);
        };
    }

    private List<IntPair> getPossibleMovesForPawn(IntPair currentPosition) {
        PieceInformation pawn = getPieceInformation(currentPosition);
        if (pawn == null || pawn.chessPiece() != ChessPiece.Pawn) {
            throw new WrongPieceException(ChessPiece.Pawn, pawn);
        }
        List<IntPair> possibleMoves = new ArrayList<>();
        int i = currentPosition.first();
        int j = currentPosition.second();
        if (pawn.player() == Player.Black) {
            for (int j_ : Arrays.asList(j + 1, j - 1)) {
                if (isRightPosition(i + 1, j_) && !isFreePlace(i + 1, j_) && field[i][j].player() != field[i + 1][j_].player()) {
                    possibleMoves.add(new IntPair(i + 1, j_));
                }
            }
            if (i < 3) {
                IntPair nextPosition = new IntPair(i + 1, j);
                while (nextPosition.first() < 4 && isFreePlace(nextPosition)) {
                    possibleMoves.add(nextPosition);
                    nextPosition = new IntPair(nextPosition.first() + 1, j);
                }
            }
            else if (isFreePlace(i + 1, j)) {
                possibleMoves.add(new IntPair(i + 1, j));
            }
        }
        else {
            for (int j_ : Arrays.asList(j + 1, j - 1)) {
                if (isRightPosition(i - 1, j_) && !isFreePlace(i - 1, j_) && field[i][j].player() != field[i - 1][j_].player()) {
                    possibleMoves.add(new IntPair(i - 1, j_));
                }
            }
            if (i > 4) {
                IntPair nextPosition = new IntPair(i - 1, j);
                while (nextPosition.first() > 3 && isFreePlace(nextPosition)) {
                    possibleMoves.add(nextPosition);
                    nextPosition = new IntPair(nextPosition.first() - 1, j);
                }
            }
            else if (isFreePlace(i - 1, j)) {
                possibleMoves.add(new IntPair(i - 1, j));
            }
        }
        return possibleMoves;
    }

    private List<IntPair> getPossibleMovesForRook(IntPair currentPosition) {
        PieceInformation rook = getPieceInformation(currentPosition);
        if (rook.chessPiece() != ChessPiece.Rook) {
            throw new WrongPieceException(ChessPiece.Rook, rook);
        }
        List<IntPair> possibleMoves = new ArrayList<>();
        int i = currentPosition.first();
        int j = currentPosition.second();
        // down
        int i_ = i + 1;
        int j_ = j;
        while (isFreePlace(i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
            ++i_;
        }
        if (isEnemyFor(rook.player(), i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
        }
        // up
        i_ = i - 1;
        while (isFreePlace(i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
            --i_;
        }
        if (isEnemyFor(rook.player(), i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
        }
        // right
        i_ = i;
        j_ = j + 1;
        while (isFreePlace(i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
            ++j_;
        }
        if (isEnemyFor(rook.player(), i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
        }
        // left
        j_ = j - 1;
        while (isFreePlace(i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
            --j_;
        }
        if (isEnemyFor(rook.player(), i_, j_)) {
            possibleMoves.add(new IntPair(i_, j_));
        }
        return possibleMoves;
    }

    private boolean isEnemyFor(Player player, int i, int j) {
        return isRightPosition(i, j) && field[i][j] != null && field[i][j].player() != player;
    }

    private List<IntPair> getPossibleMovesForKnight(IntPair currentPosition) {
        PieceInformation knight = getPieceInformation(currentPosition.first(), currentPosition.second());
        if (knight.chessPiece() != ChessPiece.Knight) {
            throw new WrongPieceException(ChessPiece.Knight, knight);
        }
        List<IntPair> possibleMoves = new ArrayList<>();
        int i = currentPosition.first();
        int j = currentPosition.second();

        return possibleMoves;
    }

    private List<IntPair> getPossibleMovesForQueen(IntPair currentPosition) {
        PieceInformation queen = getPieceInformation(currentPosition.first(), currentPosition.second());
        if (queen.chessPiece() != ChessPiece.Queen) {
            throw new WrongPieceException(ChessPiece.Queen, queen);
        }
        List<IntPair> possibleMoves = new ArrayList<>();
        int i = currentPosition.first();
        int j = currentPosition.second();

        return possibleMoves;
    }

    private List<IntPair> getPossibleMovesForKing(IntPair currentPosition) {
        PieceInformation king = getPieceInformation(currentPosition.first(), currentPosition.second());
        if (king.chessPiece() != ChessPiece.King) {
            throw new WrongPieceException(ChessPiece.King, king);
        }
        List<IntPair> possibleMoves = new ArrayList<>();
        int i = currentPosition.first();
        int j = currentPosition.second();

        return possibleMoves;
    }

    private List<IntPair> getPossibleMovesForBishop(IntPair currentPosition) {
        PieceInformation bishop = getPieceInformation(currentPosition.first(), currentPosition.second());
        if (bishop.chessPiece() != ChessPiece.Bishop) {
            throw new WrongPieceException(ChessPiece.Bishop, bishop);
        }
        List<IntPair> possibleMoves = new ArrayList<>();
        int i = currentPosition.first();
        int j = currentPosition.second();

        return possibleMoves;
    }

    public boolean isThereMatTo(Player player) {
        if (!isThereCheckTo(player)) {
            return false;
        }
        // for every move check
        return true;
    }
}
