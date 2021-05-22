package br.com.edmilson.sicredi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.entities.enums.StatusPauta;

public class PautaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String titulo;
	private Integer statusPauta = 1;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	final private LocalDateTime CREATED_AT = LocalDateTime.now();

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime votacaoAberta;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime encerrarVotacao;
	
	public PautaDTO() {}

	public PautaDTO(Pauta entity) {
		super();
		this.id = entity.getId();
		this.titulo = entity.getTitulo();
		this.statusPauta = entity.getStatusPauta().getCod();
		this.votacaoAberta = entity.getVotacaoAberta();
		this.encerrarVotacao = entity.getEncerrarVotacao();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public StatusPauta getStatusPauta() {		
		return StatusPauta.toEnum(statusPauta);
	}
	
	public void setStatusPauta(StatusPauta statusPauta) {
		this.statusPauta = statusPauta.getCod();
	}
	public LocalDateTime getVotacaoAberta() {
		return votacaoAberta;
	}

	public void setVotacaoAberta(LocalDateTime votacaoAberta) {
		this.votacaoAberta = votacaoAberta;
	}

	public LocalDateTime getEncerrarVotacao() {
		return encerrarVotacao;
	}

	public void setEncerrarVotacao(LocalDateTime encerrarVotacao) {
		this.encerrarVotacao = encerrarVotacao;
	}
	
	
	
	

}
