package br.com.edmilson.sicredi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.edmilson.sicredi.entities.Associado;
import br.com.edmilson.sicredi.entities.enums.Status;
import br.com.edmilson.sicredi.service.AssociadoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/associados")
@Api(value="Desafio Pauta")
public class AssociadoController {
	private AssociadoService service;

	public AssociadoController(AssociadoService service) {
		this.service = service;
	}
	
	@ApiOperation(value="Procura associado pelo ID")
	@GetMapping("/id/{id}")
	public ResponseEntity<Associado> acharAssociado(@PathVariable int id){
		Associado associados = service.acharAssociado(id);
		return ResponseEntity.ok(associados);
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
	
	@ApiOperation(value="Reativar associado pelo ID. Por padrão recebe status:UNABLE_TO_VOTE")
	@PostMapping("/id/{id}")
	public ResponseEntity<Associado> ativarAssociado(@PathVariable int id) {
		Associado associado = service.ativarAssociado(id);
		return ResponseEntity.ok(associado);
	}
	
	@ApiOperation(value="Cadastra um novo associado. O status (ABLE_TO_VOTE ou UNABLE_TO_VOTE) pode ser informado na hora do cadastro, mas por padrão é informado pela API externa.")
	@ApiResponses(value = {	
		    @ApiResponse(code = 201, response = Associado.class, message = "Created"),
		   	    
		})	
	@PostMapping
	public ResponseEntity novoAssociado(@RequestBody Associado associado) {
		Associado obj = service.salvar(associado);		
		return new ResponseEntity(obj, HttpStatus.CREATED);
	}
	
	@ApiOperation(value="?status= ABLE_TO_VOTE || UNABLE_TO_VOTE      |Mudar Status de votação")
	@PatchMapping("/id/{id}")
	public ResponseEntity<Associado> mudarStatus(@PathVariable int id,
													@RequestParam(name="status") String status){
		Associado response = service.mudarStatus(id, status);
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value="Deleta associado pelo ID")
	@DeleteMapping("/id/{id}")
	public ResponseEntity<Void> deletarAssociado(@PathVariable int id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	

}
