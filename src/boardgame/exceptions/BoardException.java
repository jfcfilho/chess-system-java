package boardgame.exceptions;

public class BoardException extends RuntimeException {

	private static final long serialVersionUID = 3562041729316842780L;

	public BoardException(String message){
		super(message);
	}
}
