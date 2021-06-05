package br.com.edmilson.sicredi.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.edmilson.sicredi.entities.Pauta;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PautaRepositoryTest {
	
	@Autowired
	 PautaRepository repository;	
	
	@Test
	public void atribuirIdPautaQuandoSalva() {		
		//cenário
		Pauta pauta = new Pauta();
		pauta.setTitulo("PautaTeste");
		repository.save(pauta);
		assertTimeout(Duration.ofSeconds(1), ()->{
			Pauta entity = repository.save(pauta);
			assertNotNull(entity.getId());							
		});
	}
	
	@Test
	public void findByTilulo() {
		Pauta pauta = new Pauta();
		pauta.setTitulo("Título");
		repository.save(pauta);
		assertTimeout(Duration.ofSeconds(1), ()->{
			Optional<Pauta> entity = repository.findByTitulo("Título");			
			Assertions.assertThat(entity).isNotEmpty();				
		});
	}
	
	@Test
	public void findAllByStatusPautaNotEmpty() {		
		assertTimeout(Duration.ofSeconds(1), ()->{
			List<Pauta> entity = repository.findAllByStatusPauta(1);			
			Assertions.assertThat(entity).isNotEmpty();				
		});
	}
	
	@Test
	public void findAllByStatusPautaEmpty() {		
		assertTimeout(Duration.ofSeconds(1), ()->{
			List<Pauta> entity = repository.findAllByStatusPauta(2);			
			Assertions.assertThat(entity).isEmpty();				
		});
	}
	
		
}
