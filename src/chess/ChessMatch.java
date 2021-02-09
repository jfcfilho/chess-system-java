package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.enuns.Color;
import chess.exceptions.ChessException;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece[][] getPieces() {

		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}

		return mat;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {
//		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
//		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
//		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
//		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
//		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
//		placeNewPiece('d', 1, new King(board, Color.WHITE));
//
//		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
//		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
//		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
//		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
//		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
//		placeNewPiece('d', 8, new King(board, Color.BLACK));
		
		placeNewPiece('h', 7, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
		
		placeNewPiece('b', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 8, new King(board, Color.BLACK));
	}

	public ChessPiece performeChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {

		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();

		validateSourcePosition(source);

		validateTargetPosition(source, target);

		Piece capturedPiece = makeMove(source, target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voc� n�o pode se colocar em cheque!");
		}
		
		check = testCheck(opponent(currentPlayer)) ? true : false;

		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		

		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece pecaTemp = (ChessPiece) board.removePiece(source);
		pecaTemp.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(pecaTemp, target);
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);

		}
		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece pecaTemp = (ChessPiece) board.removePiece(target);
		pecaTemp.decreaseMoveCount();
		board.placePiece(pecaTemp, source);
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			piecesOnTheBoard.add(capturedPiece);
			capturedPieces.remove(capturedPiece);
		}
	}

	private void validateSourcePosition(Position source) {
		if (!board.thereIsAPiece(source)) {
			throw new ChessException("N�o existe pe�a na posi��o de origem!");
		}
		if (currentPlayer != ((ChessPiece) board.piece(source)).getColor()) {
			throw new ChessException("A pe�a escolhida n�o � sua!");
		}
		if (!board.piece(source).isThereAnyPossibleMove()) {
			throw new ChessException("N�o existe movimentos poss�veis para a pe�a escolhida!");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("A pe�a escolhida n�o pode ser movida para a posi��o de destino!");
		}
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	private void nextTurn() {
		turn++;
		currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return color == Color.WHITE ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if(p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("N�o existe Rei da cor " + color + " no tabuleiro!");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentsPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentsPieces) {
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> listPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : listPieces) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j); 
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
}
