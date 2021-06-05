package br.com.edmilson.sicredi.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.enums.Status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AssociadoRepositoryTest {
	
	@Autowired
	 AssociadoRepository repository;	
	
	@Test
	public void atribuirIdAssociadoQaundoSalva() {		
		//cenário
		Associado associado = new Associado();
		associado.setNome("TestNome");
		associado.setCpf("01234567890");
		//execução
		assertTimeout(Duration.ofSeconds(1), ()->{
			Associado entity =repository.save(associado);			
			assertNotNull(entity.getId());				
		});
	}		
	
	@Test
	public void existAssociadoByCPF() {	
		//cenário
		Associado associado = new Associado();
		associado.setNome("TestNome");
		associado.setCpf("40641417098");
		repository.save(associado);		
		//execução
		assertTimeout(Duration.ofSeconds(1), ()->{		
			boolean result = repository
						.existsByCpf("40641417098");		
			assertTrue(result);			
		});
	}	
	
	@Test
	public void findAssociadoByCPF() {
		assertTimeout(Duration.ofSeconds(1), ()->{
			Optional<Associado> optAssociado = repository.findByCpf("40641417098");
			Assertions.assertThat(optAssociado).isNotEmpty();
			assertTrue(optAssociado.get().getNome().equals("TestNome"));			
		});
	}
	
	@Test
	public void notExistAssociadoByCPF() {	
		assertTimeout(Duration.ofSeconds(1), ()->{
			boolean result = repository.existsByCpf("00000000000");
			assertFalse(result);			
		});
	}
	
	@Test
	public void findAllAssociadosByStatus_ABLE() {
		assertTimeout(Duration.ofSeconds(1), ()->{
			List<Associado> listAssociados = repository.findAllByStatus(1);
			assertNotNull(listAssociados);
		});
	}
	
	@Test
	public void findAllAssociadosByStatus_UNABLE() {
		
		assertTimeout(Duration.ofSeconds(1), ()->{
			List<Associado> listAssociados = repository.findAllByStatus(0);
			Assertions.assertThat(listAssociados).isEmpty();
		});
		
		//cenário 2
		Associado associado = new Associado();
		associado.setNome("TestNome");
		associado.setCpf("09876543210");
		associado.setStatus(Status.UNABLE_TO_VOTE);
		repository.save(associado);
		
		assertTimeout(Duration.ofSeconds(1), ()->{
			List<Associado> listAssociados = repository.findAllByStatus(0);
			Assertions.assertThat(listAssociados).isNotEmpty();
		});
	}	
}
