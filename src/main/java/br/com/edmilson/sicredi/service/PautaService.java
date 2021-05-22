package br.com.edmilson.sicredi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.edmilson.sicredi.dto.PautaDTO;
import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.entities.PautaAssociado;
import br.com.edmilson.sicredi.entities.enums.Status;
import br.com.edmilson.sicredi.entities.enums.StatusPauta;
import br.com.edmilson.sicredi.entities.enums.Voto;
import br.com.edmilson.sicredi.repositories.AssociadoRepository;
import br.com.edmilson.sicredi.repositories.PautaAssociadoRepository;
import br.com.edmilson.sicredi.repositories.PautaRepository;
import br.com.edmilson.sicredi.service.excepitions.ValidacaoException;
import br.com.edmilson.sicredi.validation.CPF;

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
	
	public List<PautaDTO> acharTodas(){
		List<Pauta> pautas = repository.findAll();
		if(pautas.isEmpty()) throw new ValidacaoException("Nenhuma pauta cadastrada");
		List<PautaDTO> pautasdto = pautas.stream().map(x->new PautaDTO(x)).collect(Collectors.toList());
		return pautasdto;
	}
	
	public List<PautaDTO> acharTodasAbertas(){
		List<Pauta> aprovadas = repository.findAllByStatusPauta(1);
		if(aprovadas.isEmpty()) throw new ValidacaoException("Nenhuma pauta aberta encontrada");		
		List<PautaDTO> pautasdto = aprovadas.stream().map(x->new PautaDTO(x)).collect(Collectors.toList());
		return pautasdto;
	}
	
	public List<PautaDTO> acharTodasAprovadas(){
		List<Pauta> aprovadas = repository.findAllByStatusPauta(3);
		if(aprovadas.isEmpty()) throw new ValidacaoException("Nenhuma pauta aprovada encontrada");		
		List<PautaDTO> pautasdto = aprovadas.stream().map(x->new PautaDTO(x)).collect(Collectors.toList());
		return pautasdto;
	}
	
	public PautaDTO salvar(Pauta pauta) {
		if(pauta.getTitulo().isEmpty()) throw new ValidacaoException("Título nao pode estar em branco");
		Pauta obj = repository.save(pauta);
		criandoVotacao(obj);
		return new PautaDTO(obj);
	}
	
	public void criandoVotacao(Pauta pauta) {
		List<Associado> aptosVotar = associadoRepository.findAllByStatus(1);
		if(aptosVotar.isEmpty()) throw new ValidacaoException("Nenhum associado está apto para votar");	
		for (Associado x : aptosVotar) {
			PautaAssociado votacao = new PautaAssociado(x, pauta);
			x.getPautaAssociado().add(votacao);
			pauta.getPautaAssociado().add(votacao);
			votacaoRepository.save(new PautaAssociado(x, pauta));			
		}
	}
	
	public Pauta abrirVotacao(int id) {
		Optional<Pauta> pauta = repository.findById(id);
		if(pauta.isEmpty()) throw new ValidacaoException("Nenhuma pauta encontrada com id: " + id);		
		if(!pauta.get().getStatusPauta().equals(StatusPauta.OPEN)) throw new ValidacaoException("Pauta já foi aberta para votação");
		pauta.get().abrirVotacao();
		this.salvar(pauta.get());		
		return pauta.get();
	}
	public Pauta abrirVotacao(int id, String time) {
		Optional<Pauta> pauta = repository.findById(id);		
		if(pauta.isEmpty()) throw new ValidacaoException("Nenhuma pauta encontrada com id: " + id);		
		if(!pauta.get().getStatusPauta().equals(StatusPauta.OPEN)) throw new ValidacaoException("Pauta já foi aberta para votação");
		//TODO tratar erro quando time vem em formato diferente
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm"); 
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);		
		pauta.get().abrirVotacao(dateTime);
		this.salvar(pauta.get());		
		return pauta.get();
	}

	public Pauta acharPauta(int id) {
		Optional<Pauta> pauta = repository.findById(id);
		if(pauta.isEmpty()) throw new ValidacaoException("Nenhuma pauta encontrada com id: " + id);
		return pauta.get();
	}
	
	public PautaAssociado votar(int id, String cpf, String voto) {
		Pauta pauta = acharPauta(id);
		if(pauta.getStatusPauta()!= StatusPauta.IN_VOTING) throw new ValidacaoException("Pauta não está em votação");
		if(cpf.length()!= 11) throw new ValidacaoException("CPF contém 11 digitos");
		Optional<Associado> optAssociado = associadoRepository.findByCpf(cpf);
		if(optAssociado.isEmpty())throw new ValidacaoException("Nenhuma associado encontrada com cpf: " + CPF.imprimeCPF(cpf));
		if(optAssociado.get().getStatus()!= Status.ABLE_TO_VOTE) throw new ValidacaoException("Associado não está apto a votar");
		
		for (PautaAssociado x : pauta.getPautaAssociado()) {
			if(x.getAssociado() == optAssociado.get()) {
				
				if(voto.equalsIgnoreCase("sim")) {
					x.setVoto(Voto.SIM);
					votacaoRepository.save(x);
					return x;
				}else if(voto.equalsIgnoreCase("nao")) {
					x.setVoto(Voto.NAO);
					votacaoRepository.save(x);
					return x;
				}else {
					throw new ValidacaoException("Voto inválido");
				}
			}
			
		}
		throw new ValidacaoException("Erro na urna");
	}
	
}
