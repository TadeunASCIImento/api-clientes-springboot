package br.com.application.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
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
	 * Retorna cliente pelo CPF passado como parâmetro em cpf.
	 */

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/cliente/cpf/", method = RequestMethod.GET)
	public Cliente findByCpf(@RequestParam("cpf") String cpf) {
		Cliente cliente = repository.findByCpf(cpf);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return cliente;
	}

	/*
	 * Retorna cliente pelo nome passado como parâmetro em nome.
	 */

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/cliente/nome/", method = RequestMethod.GET)
	public Cliente findByNome(@RequestParam("nome") String nome) {
		Cliente cliente = repository.findByNome(nome);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return cliente;
	}

	/*
	 * Cria um novo cliente no banco.
	 */

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/cliente/novo/", method = RequestMethod.POST)
	public void add(@RequestBody Cliente cliente) {
		repository.save(cliente);
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/cliente/delete/", method = RequestMethod.DELETE)
	public void remove(@RequestParam("id") Long id) {
		repository.deleteById(id);

	}

}
