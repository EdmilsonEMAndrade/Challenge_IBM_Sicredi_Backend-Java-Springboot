package br.com.edmilson.sicredi.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edmilson.sicredi.entities.Pauta;
import br.com.edmilson.sicredi.service.PautaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/pautas")
@Api(value="Desafio Pauta")
public class PautaController {
	private PautaService service;

	public PautaController(PautaService service) {
		this.service = service;
	}
	
	@ApiOperation(value="Mostra todas pautas.")
	@GetMapping
	public ResponseEntity<?> todasPautas(){
		List<Pauta> pautas = service.acharTodas();
		return ResponseEntity.ok(pautas);
	}
	
	@ApiOperation(value="Mostra todas pautas abertas.")
	@GetMapping("/abertas")
	public ResponseEntity<?> todosPautasAbertas(){
		List<Pauta> abertas = service.acharTodasAbertas();
		return ResponseEntity.ok(abertas);
	}
	
	@ApiOperation(value="Mostra todos pautas aprovadas.")
	@GetMapping("/aprovadas")
	public ResponseEntity<?> todosPautasAprovadas(){
		List<Pauta> aprovadas = service.acharTodasAprovadas();
		return ResponseEntity.ok(aprovadas);
	}
	
	@ApiOperation(value="Cadastra uma nova pauta.")
	@PostMapping
	public ResponseEntity<?> novaPauta(@RequestBody Pauta pauta) {
		service.salvar(pauta);
		return ResponseEntity.ok(pauta);
	}
	
	@ApiOperation(value="Cadastra uma nova pauta.")
	@PostMapping("/abrir/{id}")
	public ResponseEntity<?> novaPauta(@PathVariable int id) {
		Pauta pauta = service.abrirVotacao(id);
		return ResponseEntity.ok(pauta);
	}
	
	

}
