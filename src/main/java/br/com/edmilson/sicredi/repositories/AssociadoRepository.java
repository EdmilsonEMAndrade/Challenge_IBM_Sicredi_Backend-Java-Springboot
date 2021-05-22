package br.com.edmilson.sicredi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edmilson.sicredi.entities.Associado;

public interface AssociadoRepository extends JpaRepository<Associado, Integer>{
	
	public Boolean existsByCpf(String cpf);
	public List<Associado> findAllByStatus(Integer status);
	public Optional<Associado> findByCpf(String cpf);
	
}
