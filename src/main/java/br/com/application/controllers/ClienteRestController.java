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
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "/cadastro/", method = RequestMethod.GET)
	public ModelAndView formulario(Cliente cliente) {
		return new ModelAndView("cadastro");
	}

	@RequestMapping(value = "/cadastrar/", method = RequestMethod.POST)
	public ModelAndView salvar(Cliente cliente) {
		add(cliente);
		return new ModelAndView("redirect:/clientes/todos/");
	}

	@RequestMapping(value = "/todos/", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("tabela");
		List<Cliente> clientes = new ArrayList<Cliente>();
		for (Cliente cli : repository.findAll()) {
			cli.setIdade(new Util().getIdade(cli.getDataNascimento()));
			clientes.add(cli);
		}
		modelAndView.addObject("clientes", clientes);
		return modelAndView;
	}

	@RequestMapping(value = "/excluir/{id}", method = RequestMethod.GET)
	public ModelAndView excluir(@PathVariable(value = "id") Long id) {
		repository.deleteById(id);
		return new ModelAndView("redirect:/clientes/cadastro/");
	}

	@RequestMapping(value = "/localizar/", method = RequestMethod.GET)
	public ModelAndView localizar(@RequestParam(name = "cpf", required = false) String cpf,
			@RequestParam(name = "nome", required = false) String nome) {
		Cliente cliente = repository.findOne(cpf, nome);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		ModelAndView modelAndView = new ModelAndView("cliente");
		modelAndView.addObject("cliente", cliente);
		return modelAndView;
	}

	/*
	 * Cria um novo cliente no banco(POST).
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
	 * Localiza e retorna o cliente passando o CPF e nome com parâmetros.(GET)
	 */

	@RequestMapping(value = "/cliente/", method = RequestMethod.GET)
	public ResponseEntity<?> find(@RequestParam(name = "cpf", required = false) String cpf,
			@RequestParam(name = "nome", required = false) String nome) {
		Cliente cliente = repository.findOne(cpf, nome);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Atualiza cliente, cria novo cliente caso não exista(PUT).
	 */

	@RequestMapping(value = "/create/", method = RequestMethod.PUT)
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		repository.save(cliente);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Atualiza cliente, já existente.(PATCH)
	 */

	@RequestMapping(value = "/update/", method = RequestMethod.PATCH)
	public ResponseEntity<?> update(@RequestBody Cliente cliente) {
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		repository.save(cliente);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	/*
	 * Retorna os clientes de forma páginada.(PAGINAÇÃO)
	 */

	@RequestMapping(value = "/pagina/", method = RequestMethod.GET)
	public ResponseEntity<?> pageable(Pageable pageable) {
		for (Object object : repository.findAll(pageable)) {
			if (object instanceof Cliente) {
				((Cliente) object).setIdade(new Util().getIdade(((Cliente) object).getDataNascimento()));
			}
		}
		return new ResponseEntity<>(repository.findAll(pageable), HttpStatus.OK);
	}

	/*
	 * Deleta cliente passando o id como parâmetro (DELETE)
	 */

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
