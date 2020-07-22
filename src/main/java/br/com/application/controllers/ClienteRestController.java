package br.com.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.application.models.Cliente;
import br.com.application.repositories.ClientesRepository;
import br.com.application.util.Util;

@RestController
@RequestMapping("/clientes")
public class ClienteRestController {

	@Autowired
	private ClientesRepository repository;

	@RequestMapping(value = "/formulario", method = RequestMethod.GET)
	public ModelAndView formulario(Cliente cliente) {
		ModelAndView modelAndView = new ModelAndView("formulario");
		modelAndView.addObject("clientes", repository.findAll());
		return modelAndView;
	}

	/*
	 * Cria um novo cliente no banco.
	 */

	@RequestMapping(value = "/novo/", method = RequestMethod.POST)
	public ResponseEntity<?> add(Cliente cliente) {
		if (!cliente.equals(null)) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			return new ResponseEntity<>(repository.save(cliente), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/*
	 * Retorna cliente pelo CPF passado como parâmetro em cpf.
	 */

	@RequestMapping(value = "/cpf/{cpf}", method = RequestMethod.GET)
	public ResponseEntity<?> findByCpf(@PathVariable(name = "cpf") String cpf) {
		Cliente cliente = repository.findByCpf(cpf);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Retorna cliente pelo nome passado como parâmetro em nome.
	 */

	@RequestMapping(value = "/nome/{nome}", method = RequestMethod.GET)
	public ResponseEntity<?> findByNome(@PathVariable(name = "nome") String nome) {
		Cliente cliente = repository.findByNome(nome);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Atualiza cliente, cria novo cliente caso não exista.
	 */

	@RequestMapping(value = "/create/", method = RequestMethod.PUT)
	public void create(@RequestBody Cliente cliente) {
		repository.save(cliente);
	}

	/*
	 * Atualiza cliente, já existente.
	 */

	@RequestMapping(value = "/update/", method = RequestMethod.PATCH)
	public ResponseEntity<?> update(@RequestBody Cliente cliente) {
		return new ResponseEntity<>(repository.save(cliente), HttpStatus.OK);
	}

	/*
	 * Retorna os clientes de forma páginada.
	 */

	@RequestMapping(value = "/pagina/", method = RequestMethod.GET)
	public ResponseEntity<?> list(Pageable pageable) {
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

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> remove(@PathVariable(value = "id") Long id) {
		repository.deleteById(id);
		return new ResponseEntity<Cliente>(HttpStatus.OK);
	}

	@RequestMapping(value = "/clientes/", method = RequestMethod.GET)
	public List<Cliente> findAll() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		for (Cliente cliente : repository.findAll()) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			clientes.add(cliente);
		}
		return clientes;
	}

}
