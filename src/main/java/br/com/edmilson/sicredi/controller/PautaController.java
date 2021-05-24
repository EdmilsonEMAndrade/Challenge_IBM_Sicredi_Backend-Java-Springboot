package br.com.edmilson.sicredi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.edmilson.sicredi.dto.PautaDTO;
import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.entities.PautaAssociado;
import br.com.edmilson.sicredi.service.PautaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/pautas")
@Api(value="Desafio Pauta")
public class PautaController {
	private PautaService service;

	public PautaController(PautaService service) {
		this.service = service;
	}
	
	@ApiOperation(value="Achar pauta por id.")
	@GetMapping("/id/{id}")
	public ResponseEntity<Pauta> pautaById(@PathVariable int id){
		Pauta pauta = service.acharPauta(id);
		return ResponseEntity.ok(pauta);
	}
	
	@ApiOperation(value="Mostra todas pautas.")
	@GetMapping
	public ResponseEntity<List<PautaDTO>> todasPautas(){
		List<PautaDTO> pautas = service.acharTodas();
		return ResponseEntity.ok(pautas);
	}
	
	@ApiOperation(value="Mostra todas pautas que estão em discução (OPEN).")
	@GetMapping("/abertas")
	public ResponseEntity<List<PautaDTO>> todosPautasAbertas(){
		List<PautaDTO> abertas = service.acharTodasAbertas();
		return ResponseEntity.ok(abertas);
	}
	
	@ApiOperation(value="Mostra todos pautas aprovadas.")
	@GetMapping("/aprovadas")
	public ResponseEntity<List<PautaDTO>> todosPautasAprovadas(){
		List<PautaDTO> aprovadas = service.acharTodasAprovadas();
		return ResponseEntity.ok(aprovadas);
	}
	
	@ApiOperation(value="Cadastra uma nova pauta.")
	@ApiResponses(value = {	
		    @ApiResponse(code = 201, response = PautaDTO.class, message = "Created"),		   	    
		})
	@PostMapping
	public ResponseEntity novaPauta(@RequestBody Pauta pauta) {
		PautaDTO obj = service.salvar(pauta);
		return new ResponseEntity(obj, HttpStatus.CREATED);
	}
	
	@ApiOperation(value="?cpf=12345678901&voto={Sim ou Nao}     |Associdado vota na pauta")
	@PostMapping("/votar/{id}")
	public ResponseEntity<PautaAssociado> votar(@PathVariable int id,
										@RequestParam(name="cpf") String cpf,
										@RequestParam(name="voto") String voto){
		PautaAssociado votoInfo = service.votar(id, cpf, voto);
		return ResponseEntity.ok(votoInfo);
	}
	
	@ApiOperation(value="Reabir uma pauta que empatou, mas não colocará em votação")	
	@PostMapping("/reabrir/{id}")
	public ResponseEntity<PautaDTO> reabrirPauta(@PathVariable int id) {
		PautaDTO pauta = service.reabrirPauta(id);
		return ResponseEntity.ok(pauta);
	}
	
	@ApiOperation(value="Abrir a votação com encerramento em 1 min.")
	@ApiResponses(value = {	
		    @ApiResponse(code = 200, response = Pauta.class, message = "OK"),		   	    
		})
	@PostMapping("/abrir/{id}")
	public ResponseEntity<Pauta> abrirPauta(@PathVariable int id) {
		Pauta pauta = service.abrirVotacao(id);
		return ResponseEntity.ok(pauta);
	}
	
	@ApiOperation(value="?time=yyyy_MM_dd_HH_mm        |Abrir a votação, definindo o seu encerramento através do time")
	@ApiResponses(value = {	
		    @ApiResponse(code = 200, response = Pauta.class, message = "OK"),		   	    
		})	
	@PostMapping("/abrir/{id}/encerra")
	public ResponseEntity<Pauta> novaPautaHorario(@PathVariable int id, 
												@RequestParam(name="time") String time) {		
		Pauta pauta = service.abrirVotacao(id, time);
		return ResponseEntity.ok(pauta);
	}
	
	@ApiOperation(value="Atualizar a pauta")
	@PutMapping("/id/{id}")
	public ResponseEntity<PautaDTO> novaPauta(@PathVariable int id,
										@RequestBody Pauta pauta) {
		PautaDTO obj = service.atualizar(id, pauta);
		return ResponseEntity.ok(obj);
	}	
	
	@ApiOperation(value="Deletar Pauta não votada")
	@DeleteMapping("/id/{id}")
	public ResponseEntity<Void> deletarPauta(@PathVariable int id) {
		service.deletarPauta(id);
		return ResponseEntity.noContent().build();
	}

}
