package br.com.edmilson.sicredi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.service.AssociadoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/associados")
@Api(value="Desafio Pauta")
public class AssociadoController {
	private AssociadoService service;

	public AssociadoController(AssociadoService service) {
		this.service = service;
	}
	
	@ApiOperation(value="Mostra todos associados.")
	@GetMapping
	public ResponseEntity<?> todosAssociados(){
		List<Associado> associados = service.acharTodos();
		return ResponseEntity.ok(associados);
	}
	
	@ApiOperation(value="Mostra todos associados aptos a votar.")
	@GetMapping("/aptos")
	public ResponseEntity<?> todosAssociadosAptosAVotar(){
		List<Associado> aptos = service.acharTodosAptosAVotar();
		return ResponseEntity.ok(aptos);
	}
	
	@ApiOperation(value="Cadastra um novo associado.")
	@PostMapping
	public ResponseEntity<?> novoAssociado(@RequestBody Associado associado) {
		service.salvar(associado);
		return ResponseEntity.ok(associado);
	}
	
	

}
