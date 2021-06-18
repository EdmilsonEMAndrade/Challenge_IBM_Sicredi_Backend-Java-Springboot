package br.com.edmilson.sicredi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import br.com.edmilson.sicredi.service.excepitions.EntityNullEception;
import br.com.edmilson.sicredi.service.excepitions.ValidacaoException;
import br.com.edmilson.sicredi.validation.CPF;
import br.com.edmilson.sicredi.validation.Time;

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

	public Pauta acharPauta(int id) {
		Optional<Pauta> pauta = repository.findById(id);
		if (pauta.isEmpty() || !pauta.get().isAtivo())
			throw new ValidacaoException("Nenhuma pauta encontrada com id: " + id);
		return pauta.get();
	}	

	public List<PautaDTO> acharTodas() {		
		return pautasValidas(repository.findAll());		
	}

	public List<PautaDTO> acharTodasAbertas() {		
		return pautasValidas(repository.findAllByStatusPauta(1));		
	}		

	public List<PautaDTO> acharTodasAprovadas() {
		return pautasValidas(repository.findAllByStatusPauta(3));
	}

	public PautaDTO salvar(Pauta pauta) {
		if (pauta.getTitulo().isEmpty())
			throw new ValidacaoException("Título nao pode estar em branco");
		Pauta obj = repository.save(pauta);
		
		return new PautaDTO(obj);
	}	

	public Pauta abrirVotacao(int id, String time) {
		Pauta pauta = acharPauta(id);	
		
		if (!pauta.getStatusPauta().equals(StatusPauta.OPEN))
			throw new ValidacaoException("Pauta já foi aberta para votação");
		
		if(time != null) {			
			if(!Time.isFormatTime(time)) throw new ValidacaoException("O tempo precisa estar no formado yyyy_MM_dd_HH_mm");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
			LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
			if (dateTime.isBefore(LocalDateTime.now())) {
				formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
				throw new ValidacaoException("Data inválida. " + dateTime.format(formatter)+ " já passou. Precisa informar uma data acima de " 
																							+ LocalDateTime.now().format(formatter) + "." );
			}
			pauta.abrirVotacao(dateTime);			
		}else {
			System.out.println("else");
			pauta.abrirVotacao();
		}
		Set<PautaAssociado> votacao = criandoVotacao(pauta);
		pauta.setPautaAssociado(votacao);
		repository.save(pauta);
		return pauta;
	}

	public PautaAssociado votar(int id, String cpf, String voto) {
		Pauta pauta = acharPauta(id);

		if (pauta.getStatusPauta() != StatusPauta.IN_VOTING)
			throw new ValidacaoException("Pauta não está em votação");

		Associado associado = validarCPF(cpf);

		if (associado.getStatus() == Status.UNABLE_TO_VOTE)
			throw new ValidacaoException("Associado não está apto a votar");

		for (PautaAssociado x : pauta.getPautaAssociado()) {			
			if (x.getAssociado().equals(associado)) {
				if (x.getVoto() == Voto.SEM_VOTO) {
					if (voto.equalsIgnoreCase("sim")) {
						x.setVoto(Voto.SIM);
						votacaoRepository.save(x);
						return x;
					} else if (voto.equalsIgnoreCase("nao")) {
						x.setVoto(Voto.NAO);
						votacaoRepository.save(x);
						return x;
					} else {
						throw new ValidacaoException("Voto inválido");
					}
				} else {
					throw new ValidacaoException("Associado "+associado.getNome()+" já votou");
				}
			}
		}
		throw new ValidacaoException("Erro na urna");
	}

	public PautaDTO atualizar(int id, Pauta pauta) {
		Pauta obj = acharPauta(id);
		if (obj.getStatusPauta() != StatusPauta.OPEN)
			throw new ValidacaoException("Pautas votadas ou em votação não podem ser alteradas");
		obj.setTitulo(pauta.getTitulo());
		return salvar(obj);
	}

	public PautaDTO reabrirPauta(int id) {
		Pauta pauta = acharPauta(id);
		if (pauta.getStatusPauta() != StatusPauta.DRAW)
			throw new ValidacaoException("A pauta não pode ser reaberta");
		pauta.setStatusPauta(StatusPauta.OPEN);
		String time = "4000_12_31_00_00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
		pauta.setEncerrarVotacao(dateTime);
		for (PautaAssociado x : pauta.getPautaAssociado()) {
			x.setVoto(Voto.SEM_VOTO);
		}
		repository.save(pauta);
		return new PautaDTO(pauta);
	}	

	public void deletarPauta(int id) {
		Pauta pauta = acharPauta(id);
		if(pauta.getStatusPauta()!=StatusPauta.OPEN) throw new ValidacaoException("Pautas votadas ou em votação não podem ser deletadas");
		pauta.setAtivo();
		repository.save(pauta);
	}
	
	private Associado validarCPF(String cpf) {
		if (cpf.isEmpty())
			throw new ValidacaoException("Precisa preencher o CPF");
		if (cpf.length() != 11)
			throw new ValidacaoException("CPF possui 11 digitos");
		if (!CPF.isCPF(cpf))
			throw new ValidacaoException("CPF " + CPF.imprimeCPF(cpf) + " é inválido");
		Optional<Associado> optAssociado = associadoRepository.findByCpf(cpf);
		if (optAssociado.isEmpty())
			throw new ValidacaoException("Nenhuma associado encontrada com cpf: " + CPF.imprimeCPF(cpf));
		return optAssociado.get();
	}
	
	private List<PautaDTO> pautasValidas(List<Pauta> pautas){
		pautas = pautas.stream().filter(x->x.isAtivo()).collect(Collectors.toList());
		if (pautas.isEmpty())throw new EntityNullEception("Nenhuma pauta cadastrada");
		
		return pautas.stream().map(x -> new PautaDTO(x)).collect(Collectors.toList());		
	}
	
	private Set<PautaAssociado> criandoVotacao(Pauta pauta) {
		List<Associado> aptosVotar = associadoRepository.findAllByStatus(1);
		if (aptosVotar.isEmpty())
			throw new ValidacaoException("Não pode ser aberta a votação, pois não temos associados aptos à votar");
		Set<PautaAssociado> list = new HashSet<>();
		for (Associado x : aptosVotar) {				
			list.add(new PautaAssociado(x, pauta));
		}
		return list;
	}
}
