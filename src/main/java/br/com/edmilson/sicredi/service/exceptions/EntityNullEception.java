package br.com.edmilson.sicredi.service.exceptions;

public class EntityNullEception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EntityNullEception(String message) {
		super(message);
	}
	
}
