package br.com.edmilson.sicredi.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import br.com.edmilson.sicredi.entities.enums.StatusPauta;
import br.com.edmilson.sicredi.entities.enums.Voto;
@Entity
public class PautaAssociado implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PautaAssociadoPK id = new PautaAssociadoPK();
	
	private Voto voto;

	public PautaAssociado() {}

	public PautaAssociado(Associado associado, Pauta pauta) {
		id.setAssociado(associado);
		id.setPauta(pauta);
	}

	public PautaAssociadoPK getId() {
		return id;
	}

	public void setId(PautaAssociadoPK id) {
		this.id = id;
	}

	public Voto getVoto() {
		return voto;
	}

	public void setVoto(Voto voto) {
		if(getPauta().getEncerrarVotacao().isAfter(LocalDateTime.now())
				&& getPauta().getStatusPauta() == StatusPauta.IN_VOTING) {
			this.voto = voto;			
		}
	};
	
	public Associado getAssociado() {
		return id.getAssociado();
	}
	
	public Pauta getPauta() {
		return id.getPauta();
	}
	
}
