package br.com.edmilson.sicredi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edmilson.sicredi.entities.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Integer>{

	public List<Pauta> findAllByStatusPauta(Integer statusPauta);
	public Optional<Pauta> findByTitulo(String titulo);
	
}
