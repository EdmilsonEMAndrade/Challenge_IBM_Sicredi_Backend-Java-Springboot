package br.com.edmilson.sicredi.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.edmilson.sicredi.entities.enums.StatusPauta;
import br.com.edmilson.sicredi.entities.enums.Voto;

@Entity
@Table(name="pautas", schema="pauta")
public class Pauta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Column(nullable=false)
	private String titulo;	
	

	@Column(nullable=false)
	private Integer statusPauta = 1;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    final private LocalDateTime CREATED_AT = LocalDateTime.now();
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime votacaoAberta;	

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime encerrarVotacao;
	
	@OneToMany(mappedBy = "id.pauta")
	private Set<PautaAssociado> pautaAssociado = new HashSet<>();
	
	public Pauta() {}

	public Pauta(@NotBlank String titulo) {
		this.titulo = titulo;		
	}
	
	public void resultado() {
		if(encerrarVotacao != null && encerrarVotacao.isBefore(LocalDateTime.now())) {
			int aprovam = pautaAssociado.stream().filter(x-> x.getVoto() == Voto.SIM).collect(Collectors.toList()).size();
			int desaprovam = pautaAssociado.stream().filter(x-> x.getVoto() == Voto.NAO).collect(Collectors.toList()).size();
			if(aprovam>desaprovam) {
				setStatusPauta(StatusPauta.APPROVED);
			}else if(aprovam<desaprovam) {
				setStatusPauta(StatusPauta.REFUSED);
			}else {
				setStatusPauta(StatusPauta.DRAW);
			}
		}
		
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
		resultado();
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
		resultado();
		return encerrarVotacao;
	}

	public void setEncerrarVotacao(LocalDateTime encerrarVotacao) {
		this.encerrarVotacao = encerrarVotacao;
	}	
	
	public Set<PautaAssociado> getPautaAssociado() {
		return pautaAssociado;
	}

	public void setPautaAssociado(Set<PautaAssociado> pautaAssociado) {
		this.pautaAssociado = pautaAssociado;
	}	

	public String abrirVotacao() {
		votacaoAberta = LocalDateTime.now();
		encerrarVotacao = LocalDateTime.of(votacaoAberta.getYear(), votacaoAberta.getMonth(), 
											votacaoAberta.getDayOfMonth(), votacaoAberta.getHour(),
											votacaoAberta.getMinute()+1, votacaoAberta.getSecond());
		setStatusPauta(StatusPauta.IN_VOTING);
		return "Pauta [id=" + id + ", titulo=" + titulo + ", statusPauta=" + this.getStatusPauta().getStatus() + ", CREATED_AT=" + CREATED_AT
				+ ", Votação Aberta em " + votacaoAberta + ", será encerrada em " + encerrarVotacao + "]";
	}
	
	public String abrirVotacao(LocalDateTime time) {
		votacaoAberta = LocalDateTime.now();
		encerrarVotacao = time;
		setStatusPauta(StatusPauta.IN_VOTING);
		return "Pauta [id=" + id + ", titulo=" + titulo + ", statusPauta=" + this.getStatusPauta().getStatus() + ", CREATED_AT=" + CREATED_AT
				+ ", Votação Aberta em " + votacaoAberta + ", será encerrada em " + encerrarVotacao + "]";
	}

	@Override
	public String toString() {
		return "Pauta [id=" + id + ", titulo=" + titulo + ", statusPauta=" + this.getStatusPauta().getStatus() + ", CREATED_AT=" + CREATED_AT
				+ ", Votação Aberta em" + votacaoAberta + ", encerrada em " + encerrarVotacao + "]";
	}	
	
}
