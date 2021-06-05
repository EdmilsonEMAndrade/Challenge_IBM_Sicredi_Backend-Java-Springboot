package br.com.edmilson.sicredi.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.enums.Status;
import br.com.edmilson.sicredi.service.excepitions.EntityNullEception;
import br.com.edmilson.sicredi.service.excepitions.ValidacaoException;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AssociadoServiceTest {
	
	@Autowired
	AssociadoService service;
	
	@Test	
	public void salvar() {
		Associado associado = new Associado("TestNome","20051739089");
		assertTimeout(Duration.ofSeconds(1), ()->{
			Associado entity =service.salvar(associado);			
			assertNotNull(entity.getId());				
		});		
	}
	
	@Test
	public void salvarCPFRepetidoException() {
		Associado associado = new Associado("TestNome2", "77361791077");		
		service.salvar(associado);
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{
				service.salvar(associado);			
			});
		});		
	}
	
	@Test
	public void salvarCPFsInvalidosException() {
		Associado associado = new Associado("TestNome3", "");	
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{
				service.salvar(associado);			
			});
		});
		associado.setCpf("11111111111");
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{
				service.salvar(associado);			
			});
		});
		associado.setCpf("111111");
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{
				service.salvar(associado);			
			});
		});
	}
	
	@Test
	public void salvarNomeNuloException() {
		Associado associado = new Associado("","65137505048");
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{
				service.salvar(associado);			
			});
		});		
	}
	
	@Test
	public void acharAssociadoID() {				
		assertTimeout(Duration.ofSeconds(1), ()->{			
				Associado entity = service.acharAssociado(1);
				assertNotNull(entity);
		});		
	}	
	
	@Test
	public void acharAssociadoIDEception() {				
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{
				service.acharAssociado(100);			
			});
		});		
	}
	
	@Test	
	public void acharTodosAssociados() {
		Associado associado = new Associado("TesteListaAssociados","23396434064");
		service.salvar(associado);
		assertTimeout(Duration.ofSeconds(1), ()->{			
				List<Associado> entity = service.acharTodos();
				assertNotNull(entity);
		});		
	}
	
	@Test
	public void acharTodosAssociadosAptosAVotar() {	
		Associado associado = new Associado("TesteAbleToVote","65137505048");
		service.salvar(associado);
		assertTimeout(Duration.ofSeconds(1), ()->{			
				List<Associado> entity = service.acharTodosAptosAVotar();
				assertNotNull(entity);
		});		
	}
	
	@Test
	public void verificarSeAptoParaVotar() {		
		assertTimeout(Duration.ofSeconds(1), ()->{			
				Status result = service.verificarSeAptoParaVotar("65137505048");
				Assertions.assertThat(result).isEqualTo(Status.ABLE_TO_VOTE);
		});		
	}
	
	@Test
	public void verificarSeNaoAptoParaVotar() {
		Associado associado = new Associado("TestUnableToVote","59422310016",Status.UNABLE_TO_VOTE);
		service.salvar(associado);
		assertTimeout(Duration.ofSeconds(1), ()->{			
				Status result = service.verificarSeAptoParaVotar("59422310016");
				Assertions.assertThat(result).isNotEqualTo(Status.ABLE_TO_VOTE);
				Assertions.assertThat(result).isEqualTo(Status.UNABLE_TO_VOTE);
		});		
	}
	
	@Test
	public void mudarStatusAssociado() {
		Associado associadoUn = new Associado("TestStatus","95326864010",Status.UNABLE_TO_VOTE);
		Associado associadoAb = new Associado("TestStatus","72690575086");
		associadoUn = service.salvar(associadoUn);
		associadoAb = service.salvar(associadoAb);
		int idUn = associadoUn.getId();
		
		assertFalse(associadoUn.getStatus().equals(Status.ABLE_TO_VOTE));	
		assertTimeout(Duration.ofSeconds(1), ()->{			
			Associado result = service.mudarStatus(idUn, "ABLE_TO_VOTE");			
			assertTrue(result.getStatus().equals(Status.ABLE_TO_VOTE));	
		});	
		
		int idAb = associadoAb.getId();
		assertFalse(associadoAb.getStatus().equals(Status.UNABLE_TO_VOTE));	
		assertTimeout(Duration.ofSeconds(1), ()->{			
			Associado result = service.mudarStatus(idAb, "UNABLE_TO_VOTE");			
			assertTrue(result.getStatus().equals(Status.UNABLE_TO_VOTE));	
		});	
	}	

	@Test
	public void deleteEAtivar() {
		Associado associado = new Associado("TestDelete","81243995068");
		associado = service.salvar(associado);
		int id = associado.getId();
		
		assertTimeout(Duration.ofSeconds(1), ()->{			
			Associado entity = service.acharAssociado(id);
			assertNotNull(entity);
		});			
		
		assertTimeout(Duration.ofSeconds(1), ()->{			
			service.delete(id);
		});
		
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(EntityNullEception.class, ()->{
			service.acharAssociado(id);				
			});
		});
		
		assertTimeout(Duration.ofSeconds(1), ()->{			
			Associado entity = service.ativarAssociado(id);
			assertNotNull(entity);
		});
	}
	
}
