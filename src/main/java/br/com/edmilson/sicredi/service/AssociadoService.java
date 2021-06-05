package br.com.edmilson.sicredi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.enums.Status;
import br.com.edmilson.sicredi.repositories.AssociadoRepository;
import br.com.edmilson.sicredi.service.excepitions.EntityNullEception;
import br.com.edmilson.sicredi.service.excepitions.ValidacaoException;
import br.com.edmilson.sicredi.service.excepitions.IllegalArgumentException;
import br.com.edmilson.sicredi.validation.CPF;

@Service
public class AssociadoService {
	
	AssociadoRepository repository;

	public AssociadoService(AssociadoRepository repository) {
		this.repository = repository;
	}
	
	public Associado acharAssociado(int id){
		Associado associado = verificarAssociado(id);		
		return associado;				
	}
	
	public List<Associado> acharTodos(){
		return associadosAtivos(repository.findAll());		
	}
	
	public List<Associado> acharTodosAptosAVotar(){
		return associadosAtivos(repository.findAllByStatus(1));		
	}
	
	public Associado salvar(Associado associado) {
		if(associado.getNome().isEmpty()) throw new ValidacaoException("Precisa preencher o nome");
		validarCPF(associado.getCpf());
		if(repository.existsByCpf(associado.getCpf())) throw new ValidacaoException("CPF "+ CPF.imprimeCPF(associado.getCpf()) +" já está cadastrado");		
		return repository.save(associado);		
	}	
	
	public Status verificarSeAptoParaVotar(String cpf) {
		validarCPF(cpf);
		Optional<Associado> associado = repository.findByCpf(cpf);
		if(associado.isEmpty()) throw new ValidacaoException("Nenhum associado encontrado com CPF: " + CPF.imprimeCPF(cpf));
		if(!associado.get().isAtivo()) throw new EntityNullEception("Associado inativo");
		return associado.get().getStatus();		
	}
	
	public Associado ativarAssociado(int id) {
		Optional<Associado> associadoOpt = repository.findById(id);
		if(associadoOpt.isEmpty()) throw new ValidacaoException("Nenhum associado encontrado");
		associadoOpt.get().setAtivo(true);
		return repository.save(associadoOpt.get());		
	}
	
	public Associado mudarStatus(int id, String status) {		
			Associado associado = verificarAssociado(id);
			associado.setStatus(Status.valueOf(status));
			return repository.save(associado);			
	}
	
	public void delete(int id) {
		Associado associado = acharAssociado(id);
		associado.setStatus(Status.UNABLE_TO_VOTE);
		associado.setAtivo(false);		
		repository.save(associado);
	}
	
	private Associado verificarAssociado(int id) {
		Optional<Associado> associado = repository.findById(id);
		if(associado.isEmpty()) throw new ValidacaoException("Nenhum associado encontrado com ID: " +id);
		if(!associado.get().isAtivo()) throw new EntityNullEception("Associado inativo");
		return associado.get();
	}
	
	private List<Associado> associadosAtivos(List<Associado> associados){
		associados = associados.stream().filter(x->x.isAtivo()).collect(Collectors.toList());
		if(associados.isEmpty()) throw new EntityNullEception("Nenhum associado encontrado");
		return associados;
	}
	
	private void validarCPF(String cpf) {
		if(cpf.isEmpty()) throw new ValidacaoException("Precisa preencher o CPF");
		if(cpf.length()!=11) throw new ValidacaoException("CPF possui 11 digitos");
		if(!CPF.isCPF(cpf)) throw new ValidacaoException("CPF " + CPF.imprimeCPF(cpf) +" é inválido");
	}
}
