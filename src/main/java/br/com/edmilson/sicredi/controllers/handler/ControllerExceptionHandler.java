package br.com.edmilson.sicredi.controllers.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.edmilson.sicredi.service.exceptions.EntityNullEception;
import br.com.edmilson.sicredi.service.exceptions.ValidacaoException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<StandardError> validation(ValidacaoException e, HttpServletRequest request){		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);		
	}
	@ExceptionHandler(EntityNullEception.class)
	public ResponseEntity<StandardError> validation(EntityNullEception e, HttpServletRequest request){		
		StandardError err = new StandardError(HttpStatus.NO_CONTENT.value(), e.getMessage());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(err);		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<StandardError> validation(IllegalArgumentException e, HttpServletRequest request){		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), "Status inválido. Use ABLE_TO_VOTE || UNABLE_TO_VOTE");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);		
	}
}
