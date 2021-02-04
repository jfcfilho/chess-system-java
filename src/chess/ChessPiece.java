package chess;

import boardgame.Board;
import boardgame.Piece;
import chess.enuns.Color;

public class ChessPiece extends Piece {

	private Color color;
	
	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
