package br.com.edmilson.sicredi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.enums.Status;
import br.com.edmilson.sicredi.service.AssociadoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	public ResponseEntity<List<Associado>> todosAssociados(){
		List<Associado> associados = service.acharTodos();
		return ResponseEntity.ok(associados);
	}
	
	@ApiOperation(value="Mostra, pelo cpf do associado, se ele pode ou não votar.")
	@GetMapping("/aptos/{cpf}")
	public ResponseEntity<Status> consultaStatusVotar(@PathVariable String cpf){
		Status status = service.verificarSeAptoParaVotar(cpf);
		return ResponseEntity.ok(status);
	}
	
	@ApiOperation(value="Mostra todos associados aptos a votar.")	
	@GetMapping("/aptos")
	public ResponseEntity<List<Associado>> todosAssociadosAptosAVotar(){
		List<Associado> aptos = service.acharTodosAptosAVotar();
		return ResponseEntity.ok(aptos);
	}
	
	@ApiOperation(value="Cadastra um novo associado. Por padrão recebe status:ABLE_TO_VOTE")
	@ApiResponses(value = {	
		    @ApiResponse(code = 201, response = Associado.class, message = "Created"),
		   	    
		})	
	@PostMapping
	public ResponseEntity novoAssociado(@RequestBody Associado associado) {
		Associado obj = service.salvar(associado);		
		return new ResponseEntity(obj, HttpStatus.CREATED);
	}
	
	

}
