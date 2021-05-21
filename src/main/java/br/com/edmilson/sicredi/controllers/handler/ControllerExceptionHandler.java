package br.com.edmilson.sicredi.controllers.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.edmilson.sicredi.service.excepitions.ElementoNuloException;
import br.com.edmilson.sicredi.service.excepitions.ValidacaoException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<StandardError> validation(ValidacaoException e, HttpServletRequest request){		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);		
	}
	@ExceptionHandler(ElementoNuloException.class)
	public ResponseEntity<StandardError> validation(ElementoNuloException e, HttpServletRequest request){		
		StandardError err = new StandardError(HttpStatus.NO_CONTENT.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(err);		
	}
	
}
