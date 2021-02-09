package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.enuns.Color;

public abstract class ChessPiece extends Piece {

	private Color color;
	protected int moveCount;
	
	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	protected boolean isThereOpponentPosition(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		
		return (p != null && p.getColor() != color);
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}
}
