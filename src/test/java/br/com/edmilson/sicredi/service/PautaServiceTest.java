package br.com.edmilson.sicredi.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.edmilson.sicredi.dto.PautaDTO;
import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.entities.enums.StatusPauta;
import br.com.edmilson.sicredi.service.exceptions.EntityNullEception;
import br.com.edmilson.sicredi.service.exceptions.ValidacaoException;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PautaServiceTest {
	
	@Autowired
	PautaService service;
	@Autowired
	AssociadoService associadoService;
	
	@Test	
	public void salvar() {
		Pauta  pauta = new Pauta("TestNome");
		assertTimeout(Duration.ofSeconds(1), ()->{
			PautaDTO entity = service.salvar(pauta);			
			assertNotNull(entity.getId());				
		});		
	}
		
	@Test
	public void acharPautaID() {				
		assertTimeout(Duration.ofSeconds(1), ()->{			
				Pauta entity = service.acharPauta(1);
				assertNotNull(entity);
		});		
	}	
	
	@Test
	public void acharPautaIDEception() {				
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{
				service.acharPauta(100);			
			});
		});		
	}
	
	@Test	
	public void acharTodasPautas() {
		Pauta pauta = new Pauta("TesteListaPautas");
		service.salvar(pauta);
		assertTimeout(Duration.ofSeconds(1), ()->{			
				List<PautaDTO> entity = service.acharTodas();
				assertNotNull(entity);
		});		
	}
	
	@Test
	public void acharTodasPautasAbertas() {	
		Pauta pauta = new Pauta("TesteOpen");
		service.salvar(pauta);
		assertTimeout(Duration.ofSeconds(1), ()->{			
				List<PautaDTO> entity = service.acharTodasAbertas();
				assertNotNull(entity);
		});		
	}
	@Test
	public void acharTodasPautasAprovadas() {	
		Pauta pauta = new Pauta("TesteOpen");
		service.salvar(pauta);
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(EntityNullEception.class, ()->{		
				service.acharTodasAprovadas();							
			});
		});		
	}
	
	@Test
	public void abrirVotacao1Exception() {
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{		
				service.abrirVotacao(1,"");							
			});
		});	
	}
	
	@Test
	public void abrirVotacao() {
		Associado associado = new Associado("Votante","40340847077");
		associadoService.salvar(associado);	
		assertTimeout(Duration.ofSeconds(1), ()->{
			Pauta result = service.abrirVotacao(1,null);
			assertTrue(result.getStatusPauta().equals(StatusPauta.IN_VOTING));			
		});
	}
	
	@Test
	public void simularVotosPautaFechadaException(){
		Associado associado = new Associado("Exception","58804458011");
		associado = associadoService.salvar(associado);							
		Pauta pauta = new Pauta("ExceptionVoto");
		PautaDTO dto = service.salvar(pauta);
		
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{		
				service.votar(dto.getId(),"82092289004" , "sim");							
			});
		});
	}		
}
