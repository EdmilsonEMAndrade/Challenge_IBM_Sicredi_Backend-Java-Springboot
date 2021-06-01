package br.com.edmilson.sicredi.service.excepitions;

public class EntityNullEception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EntityNullEception(String message) {
		super(message);
	}
	
}
