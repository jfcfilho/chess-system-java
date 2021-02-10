package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.enuns.Color;

public class King extends ChessPiece {
	
	private ChessMatch chessMatch; 

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		
		return p == null || p.getColor() != getColor();
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		// verificação se existe peça acima
		p.setValues(position.getRow() - 1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// verificação se existe peça à esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// verificação se existe peça à direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// verificação se existe peça abaixo
		p.setValues(position.getRow() + 1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// verificação se existe peça a noroeste
		p.setValues(position.getRow() - 1, position.getColumn() -1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// verificação se existe peça a nordeste
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// verificação se existe peça a sudoeste
		p.setValues(position.getRow() + 1, position.getColumn() -1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// verificação se existe peça a sudeste
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//movimento especial Roque (Castling)
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			// Roque pequeno (Roque do lado direito do Rei)
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			if(testRookCastling(posT1)) {
				//casa imediatamente à direita do Rei
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				//segunda casa imediatamente à direita do Rei
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat[p.getRow()][p.getColumn() + 2] = true;		
				}
			}
			// Roque grande (Roque do lado esquerdo do Rei)
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
			if(testRookCastling(posT2)) {
				//casa imediatamente à esquerda do Rei
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				//segunda casa imediatamente à esquerda do Rei
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				//terceira casa imediatamente à esquerda do Rei
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat[p.getRow()][p.getColumn() - 2] = true;		
				}
			}
		}
				
		return mat;
	}
	
	private boolean testRookCastling(Position position) {
		
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
	}
}
