package br.com.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.application.models.Cliente;
import br.com.application.repositories.ClientesRepository;
import br.com.application.util.Util;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClientesRepository repository;

	/*
	 * Retorna os clientes de forma páginada.
	 */

	@RequestMapping(value = "/paginas/",method = RequestMethod.GET)
	public ResponseEntity<?> findAll(Pageable pageable) {
		return new ResponseEntity<>(repository.findAll(pageable), HttpStatus.OK);
	}

	/*
	 * Retorna cliente pelo CPF passado como parâmetro em cpf.
	 */

	@RequestMapping(value = "/cliente/cpf/", method = RequestMethod.GET)
	public ResponseEntity<?> findByCpf(@RequestParam("cpf") String cpf) {
		Cliente cliente = repository.findByCpf(cpf);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Retorna cliente pelo nome passado como parâmetro em nome.
	 */

	@RequestMapping(value = "/cliente/nome/", method = RequestMethod.GET)
	public ResponseEntity<?> findByNome(@RequestParam("nome") String nome) {
		Cliente cliente = repository.findByNome(nome);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Cria um novo cliente no banco.
	 */

	@RequestMapping(value = "/cliente/novo/", method = RequestMethod.POST)
	public void add(@RequestBody Cliente cliente) {
		repository.save(cliente);
	}

	/*
	 * Deleta cliente passando o id como parâmetro
	 */

	@RequestMapping(value = "/cliente/delete/", method = RequestMethod.DELETE)
	public void remove(@RequestParam("id") Long id) {
		repository.deleteById(id);

	}

}
