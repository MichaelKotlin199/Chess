package org.example;

import org.example.Chess.Chess;
import org.example.Chess.ChessPiece;
import org.example.Chess.IntPair;
import org.example.Chess.PieceInformation;

public class Main {
    public static void printField(PieceInformation[][] field) {
        for (int i = 0; i < Chess.rowCount; i++) {
            for (int j = 0; j < Chess.columnCount; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Chess chess = new Chess();
        printField(chess.getCurrentStateOfField());
        PieceInformation inf = chess.getCurrentStateOfField()[0][0];

    }
}