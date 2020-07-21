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
@RequestMapping("/api")
public class ClienteController {

	@Autowired
	private ClientesRepository repository;

	/*
	 * Cria um novo cliente no banco.
	 */

	@RequestMapping(value = "/clientes/novo/", method = RequestMethod.POST)
	public void addCliente(@RequestBody Cliente cliente) {
		repository.save(cliente);
	}

	/*
	 * Retorna cliente pelo CPF passado como parâmetro em cpf.
	 */

	@RequestMapping(value = "/clientes/cpf/", method = RequestMethod.GET)
	public ResponseEntity<?> findByCpf(@RequestParam("cpf") String cpf) {
		Cliente cliente = repository.findByCpf(cpf);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Retorna cliente pelo nome passado como parâmetro em nome.
	 */

	@RequestMapping(value = "/clientes/nome/", method = RequestMethod.GET)
	public ResponseEntity<?> findByNome(@RequestParam("nome") String nome) {
		Cliente cliente = repository.findByNome(nome);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Atualiza cliente, cria novo cliente caso não exista.
	 */

	@RequestMapping(value = "/clientes/create/", method = RequestMethod.PUT)
	public void create(@RequestBody Cliente cliente) {
		repository.save(cliente);
	}

	/*
	 * Atualiza cliente, já existente.
	 */

	@RequestMapping(value = "/clientes/update/", method = RequestMethod.PATCH)
	public void update(@RequestBody Cliente cliente) {
		repository.save(cliente);
	}

	/*
	 * Retorna os clientes de forma páginada.
	 */

	@RequestMapping(value = "/clientes/pagina/", method = RequestMethod.GET)
	public ResponseEntity<?> findAll(Pageable pageable) {
		for (Object object : repository.findAll(pageable)) {
			if (object instanceof Cliente) {
				((Cliente) object).setIdade(new Util().getIdade(((Cliente) object).getDataNascimento()));
			}
		}
		return new ResponseEntity<>(repository.findAll(pageable), HttpStatus.OK);
	}

	/*
	 * Deleta cliente passando o id como parâmetro
	 */

	@RequestMapping(value = "/clientes/delete/", method = RequestMethod.DELETE)
	public void remove(@RequestParam("id") Long id) {
		repository.deleteById(id);
	}

}
