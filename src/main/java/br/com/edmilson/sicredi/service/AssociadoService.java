package br.com.edmilson.sicredi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.enums.Status;
import br.com.edmilson.sicredi.repositories.AssociadoRepository;
import br.com.edmilson.sicredi.service.excepitions.ElementoNuloException;
import br.com.edmilson.sicredi.service.excepitions.ValidacaoException;
import br.com.edmilson.sicredi.validation.CPF;

@Service
public class AssociadoService {
	
	AssociadoRepository repository;

	public AssociadoService(AssociadoRepository repository) {
		this.repository = repository;
	}
	
	public List<Associado> acharTodos(){
		List<Associado> associados = repository.findAll();
		if(associados.isEmpty()) throw new ElementoNuloException("Nenhum associado cadastrado");
		return associados;
	}
	
	public List<Associado> acharTodosAptosAVotar(){
		List<Associado> aptosVotar = repository.findAllByStatus(1);
		if(aptosVotar.isEmpty()) throw new ElementoNuloException("Nenhum associado está apto para votar");		
		return aptosVotar;
	}
	
	public Associado salvar(Associado associado) {
		if(associado.getNome().isEmpty()) throw new ValidacaoException("Precisa preencher o nome");
		if(associado.getCpf().isEmpty()||!CPF.isCPF(associado.getCpf())) throw new ValidacaoException("CPF " + CPF.imprimeCPF(associado.getCpf()) +" é inválido");
		if(repository.existsByCpf(associado.getCpf())) throw new ValidacaoException("CPF "+ CPF.imprimeCPF(associado.getCpf()) +" já está cadastrado");		
		Associado obj = repository.save(associado);
		return obj;
	}	
	
	public Status verificarSeAptoParaVotar(String cpf) {
		if(cpf.length()!=11) throw new ValidacaoException("CPF possui 11 digitos");
		if(!CPF.isCPF(cpf)) throw new ValidacaoException("CPF " + CPF.imprimeCPF(cpf) +" é inválido");
		Optional<Associado> associado = repository.findByCpf(cpf);
		if(associado.isEmpty()) throw new ValidacaoException("Nenhum associado encontrado com CPF: " + CPF.imprimeCPF(cpf));
		return associado.get().getStatus();		
	}
}
