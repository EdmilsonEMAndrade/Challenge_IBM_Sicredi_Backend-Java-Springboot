package br.com.edmilson.sicredi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.entities.PautaAssociado;

public interface PautaAssociadoRepository extends JpaRepository<PautaAssociado, Integer>{
	
	public Optional<PautaAssociado> findById_PautaAndId_Associado(Integer id_pauta, Integer id_associado);

}
