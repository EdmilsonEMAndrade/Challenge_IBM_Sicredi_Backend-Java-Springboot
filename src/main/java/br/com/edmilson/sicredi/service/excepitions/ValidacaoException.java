package br.com.edmilson.sicredi.service.excepitions;

public class ValidacaoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidacaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidacaoException(String message) {
		super(message);		
	}
	
}
