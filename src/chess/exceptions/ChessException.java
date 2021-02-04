package chess.exceptions;

import boardgame.exceptions.BoardException;

public class ChessException extends BoardException {

	private static final long serialVersionUID = 8954997655877517208L;
	
	public ChessException(String message) {
		super(message);
	}

}
