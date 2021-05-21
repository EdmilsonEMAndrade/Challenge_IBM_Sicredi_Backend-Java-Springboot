package br.com.edmilson.sicredi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.entities.PautaAssociado;
import br.com.edmilson.sicredi.entities.enums.StatusPauta;
import br.com.edmilson.sicredi.repositories.AssociadoRepository;
import br.com.edmilson.sicredi.repositories.PautaAssociadoRepository;
import br.com.edmilson.sicredi.repositories.PautaRepository;
import br.com.edmilson.sicredi.service.excepitions.ElementoNuloException;
import br.com.edmilson.sicredi.service.excepitions.ValidacaoException;

@Service
public class PautaService {
	
	PautaRepository repository;
	PautaAssociadoRepository votacaoRepository;
	AssociadoRepository associadoRepository;	

	public PautaService(PautaRepository repository, PautaAssociadoRepository votacaoRepository,
			AssociadoRepository associadoRepository) {
		this.repository = repository;
		this.votacaoRepository = votacaoRepository;
		this.associadoRepository = associadoRepository;
	}
	
	public List<Pauta> acharTodas(){
		List<Pauta> pautas = repository.findAll();
		if(pautas.isEmpty()) throw new ElementoNuloException("Nenhuma pauta cadastrada");
		return pautas;
	}
	
	public List<Pauta> acharTodasAbertas(){
		List<Pauta> aprovadas = repository.findAllByStatusPauta(1);
		if(aprovadas.isEmpty()) throw new ElementoNuloException("Nenhuma pauta aberta encontrada");		
		return aprovadas;
	}
	
	public List<Pauta> acharTodasAprovadas(){
		List<Pauta> aprovadas = repository.findAllByStatusPauta(3);
		if(aprovadas.isEmpty()) throw new ElementoNuloException("Nenhuma pauta aprovada encontrada");		
		return aprovadas;
	}
	
	public void salvar(Pauta pauta) {
		if(pauta.getTitulo().isEmpty()) throw new ValidacaoException("Título nao pode estar em branco");
		repository.save(pauta);
		Optional<Pauta> pautaOpt = repository.findByTitulo(pauta.getTitulo());
		criandoVotacao(pautaOpt.get());
	}
	
	public void criandoVotacao(Pauta pauta) {
		List<Associado> aptosVotar = associadoRepository.findAllByStatus(1);
		if(aptosVotar.isEmpty()) throw new ElementoNuloException("Nenhum associado está apto para votar");	
		for (Associado x : aptosVotar) {
			PautaAssociado votacao = new PautaAssociado(x, pauta);
			x.getPautaAssociado().add(votacao);
			pauta.getPautaAssociado().add(votacao);
			votacaoRepository.save(new PautaAssociado(x, pauta));			
		}
	}
	
	public Pauta abrirVotacao(int id) {
		Optional<Pauta> pauta = repository.findById(id);
		if(pauta.isEmpty()) throw new ElementoNuloException("Nenhuma pauta encontrada com id: " + id);		
		if(!pauta.get().getStatusPauta().equals(StatusPauta.OPEN)) throw new ValidacaoException("Pauta já foi aberta para votação");
		pauta.get().abrirVotacao();
		this.salvar(pauta.get());		
		return pauta.get();
	}
	public Pauta abrirVotacao(int id, String time) {
		Optional<Pauta> pauta = repository.findById(id);		
		if(pauta.isEmpty()) throw new ElementoNuloException("Nenhuma pauta encontrada com id: " + id);		
		if(!pauta.get().getStatusPauta().equals(StatusPauta.OPEN)) throw new ValidacaoException("Pauta já foi aberta para votação");
		//TODO tratar erro quando time vem em formato diferente
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);		
		pauta.get().abrirVotacao(dateTime);
		this.salvar(pauta.get());		
		return pauta.get();
	}
	
}
