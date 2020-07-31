package br.com.application.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.application.models.Cliente;
import br.com.application.repositories.ClientesRepository;
import br.com.application.util.Util;

@RestController
@Transactional
public class ClienteRestController {

	@Autowired
	private ClientesRepository repository;

	// Chama o formulário para cadastro de clientes.
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView cadastro(Cliente cliente) {
		return new ModelAndView("cadastro");
	}

	// Recebe e persiste os dados do cliente enviados pelo formulário retornando o
	// cliente cadastrado com um campo com a idade calculada a partir da data de
	// nascimento.
	@RequestMapping(value = "/cadastrar/", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors())
			return new ModelAndView("cadastro", "/", result.getAllErrors());
		add(cliente);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ModelAndView("redirect:/todos/");
	}

	// Localiza um único cliente a partir do CPF, ou uma lista para clientes de
	// mesmo nome.
	@RequestMapping(value = "/localizar/", method = RequestMethod.GET)
	public ModelAndView localizar(@RequestParam(name = "cpf", required = false) String cpf,
			@RequestParam(name = "nome", required = false) String nome) {
		List<Cliente> clientes = new ArrayList<>();
		for (Cliente cliente : repository.find(cpf, nome)) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			clientes.add(cliente);
		}
		return new ModelAndView("cliente", "clientes", clientes);
	}

	// Busca cliente e abre o formulário para edição dos dados para atualização.
	@RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
	public ModelAndView atualizar(@PathVariable(name = "id", required = false) Long id) {
		Cliente cliente = repository.getOne(id);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ModelAndView("update", "cliente", cliente);
	}

	// Realiza efetivamente a atualização dos dados do cliente enviando novamente
	// para o banco.
	@RequestMapping(value = "/atualizar/", method = RequestMethod.POST)
	public ModelAndView confirmar(Cliente cliente) {
		repository.update(cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(), cliente.getId());
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ModelAndView("update", "clientes", cliente);
	}

	// Exibe a tabela de clientes cadastrados e opção de busca.
	@RequestMapping(value = "/todos/", method = RequestMethod.GET)
	public ModelAndView todos() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		for (Cliente cliente : repository.findAll()) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			clientes.add(cliente);
		}
		return new ModelAndView("tabela", "clientes", clientes);
	}

	// Excluí o cliente passando o id como parâmetro.
	@RequestMapping(value = "/excluir/{id}", method = RequestMethod.GET)
	public ModelAndView excluir(@PathVariable(value = "id") Long id) {
		delete(id);
		return new ModelAndView("redirect:/todos/");
	}

	// Cria um recurso no banco (POST).
	@RequestMapping(value = "/novo/", method = RequestMethod.POST)
	public ResponseEntity<?> add(Cliente cliente) {
		if (!cliente.equals(null)) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			return new ResponseEntity<>(repository.save(cliente), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// Localiza e retorna o recurso passando o CPF e nome com parâmetros. (GET)
	@RequestMapping(value = "/buscar/", method = RequestMethod.GET)
	public ResponseEntity<?> find(@RequestParam(name = "cpf", required = false) String cpf,
			@RequestParam(name = "nome", required = false) String nome) {
		List<Cliente> clientes = new ArrayList<>();
		for (Cliente cliente : repository.find(cpf, nome)) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			clientes.add(cliente);
		}
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	// Atualiza recurso, ou cria um caso não exista (PUT).
	@RequestMapping(value = "/create/", method = RequestMethod.PUT)
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		repository.save(cliente);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	// Atualiza recurso parcialmente, já existente. (PATCH)
	@RequestMapping(value = "/update/", method = RequestMethod.PATCH)
	public ResponseEntity<?> update(@RequestBody Cliente cliente) {
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		repository.save(cliente);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	// Retorna recursos paginados.
	@RequestMapping(value = "/page/", method = RequestMethod.GET)
	public ResponseEntity<?> pageable(Pageable pageable) {
		for (Object object : repository.findAll(pageable)) {
			if (object instanceof Cliente) {
				((Cliente) object).setIdade((new Util().getIdade(((Cliente) object).getDataNascimento())));
			}
		}
		return new ResponseEntity<>(repository.findAll(pageable), HttpStatus.OK);
	}

	// Remove recurso passando o id como parâmetro (DELETE)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
