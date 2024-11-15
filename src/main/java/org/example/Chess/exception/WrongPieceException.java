package org.example.Chess.exception;

import org.example.Chess.ChessPiece;
import org.example.Chess.PieceInformation;

public class WrongPieceException extends ChessException {
    public WrongPieceException(ChessPiece expectedChessPiece, PieceInformation actualChessPiece) {
        super("expected " + expectedChessPiece + " but got " + actualChessPiece);
    }
}
