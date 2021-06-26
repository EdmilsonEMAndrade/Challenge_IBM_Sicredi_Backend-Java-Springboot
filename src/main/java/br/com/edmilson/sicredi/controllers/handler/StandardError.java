package br.com.edmilson.sicredi.controllers.handler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StandardError implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer status;
	private String mensagem;
	private LocalDateTime timeStamp;
	
	public StandardError(Integer status, String mensagem) {
		this.status = status;
		this.mensagem = mensagem;
		this.timeStamp = LocalDateTime.now();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}	
	
}
