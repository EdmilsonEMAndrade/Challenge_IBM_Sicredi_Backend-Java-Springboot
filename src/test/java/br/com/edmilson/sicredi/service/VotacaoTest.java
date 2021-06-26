package br.com.edmilson.sicredi.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.edmilson.sicredi.dto.PautaDTO;
import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.entities.enums.StatusPauta;
import br.com.edmilson.sicredi.service.exceptions.ValidacaoException;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class VotacaoTest {
	
	@Autowired
	PautaService service;
	@Autowired
	AssociadoService associadoService;	
	
	@Test
	public void simularVotosPauta() throws InterruptedException{
		Associado associado = new Associado("Associado1","51364533030");
		associado = associadoService.salvar(associado);							
		Pauta pautaApro = new Pauta("Apro");
		PautaDTO dtoApro = service.salvar(pautaApro);
		Pauta pautaNeg = new Pauta("Neg");
		PautaDTO dtoNeg = service.salvar(pautaNeg);
		Pauta pautaEmp = new Pauta("Emp");
		PautaDTO dtoEmp = service.salvar(pautaEmp);
		
		assertTimeout(Duration.ofSeconds(1), ()->{
				service.abrirVotacao(dtoApro.getId(), null);
				service.abrirVotacao(dtoNeg.getId(), null);
				service.abrirVotacao(dtoEmp.getId(), null);
		});
		
		assertTimeout(Duration.ofSeconds(1), ()->{		
				service.votar(dtoApro.getId(), "51364533030" , "sim");
				
				service.votar(dtoNeg.getId(), "51364533030", "nao");
		});
		
		assertTimeout(Duration.ofSeconds(1), ()->{
			assertThrows(ValidacaoException.class, ()->{		
				service.votar(dtoApro.getId(), "51364533030" , "sim");
			});
		});
		
		Thread.sleep(62000);	
		
		assertTimeout(Duration.ofSeconds(1), ()->{
			Pauta neg = service.acharPauta(dtoNeg.getId());
			assertTrue(neg.getStatusPauta().equals(StatusPauta.REFUSED));			
		});
			assertTimeout(Duration.ofSeconds(1), ()->{
				Pauta emp = service.acharPauta(dtoEmp.getId());
				assertTrue(emp.getStatusPauta().equals(StatusPauta.DRAW));
		});
			assertTimeout(Duration.ofSeconds(1), ()->{
				Pauta apro = service.acharPauta(dtoApro.getId());
				assertTrue(apro.getStatusPauta().equals(StatusPauta.APPROVED));
			});
	}	
	
}
