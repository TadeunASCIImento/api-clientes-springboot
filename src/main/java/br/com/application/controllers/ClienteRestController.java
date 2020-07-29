package br.com.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	// Chama o formulário para cadastro de clientes.
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView cadastro(Cliente cliente) {
		return new ModelAndView("cadastro");
	}

	// Recebe e persiste os dados do cliente enviados pelo formulário retornando o
	// cliente cadastrado com um campo com a idade calculada a partir da data de
	// nascimento.
	@RequestMapping(value = "/cadastrar/", method = RequestMethod.POST)
	public ModelAndView salvar(Cliente cliente) {
		add(cliente);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ModelAndView("redirect:/clientes/todos/");
	}

	// Localiza um único cliente a partir do CPF, ou uma lista para clientes de
	// mesmo nome.
	@GetMapping(value = "/localizar/")
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
	@GetMapping(value = "/editar/{id}")
	public ModelAndView atualizar(@PathVariable(name = "id", required = false) Long id) {
		Cliente cliente = repository.getOne(id);
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ModelAndView("update", "cliente", cliente);
	}

	// Realiza efetivamente a atualização dos dados do cliente enviando novamente
	// para o banco.
	@PostMapping(value = "/atualizar/")
	public ModelAndView confirmar(Cliente cliente) {
		repository.update(cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(), cliente.getId());
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		return new ModelAndView("update", "clientes", cliente);
	}

	// Exibe a tabela de clientes cadastrados e opção de busca.
	@GetMapping(value = "/todos/")
	public ModelAndView todos() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		for (Cliente cliente : repository.findAll()) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			clientes.add(cliente);
		}
		return new ModelAndView("tabela", "clientes", clientes);
	}

	// Excluí o cliente passando o id como parâmetro.
	@GetMapping(value = "/excluir/{id}")
	public ModelAndView excluir(@PathVariable(value = "id") Long id) {
		delete(id);
		return new ModelAndView("redirect:/clientes/todos/");
	}

	// Cria um recurso no banco (POST).
	@PostMapping
	public ResponseEntity<?> add(Cliente cliente) {
		if (!cliente.equals(null)) {
			cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
			return new ResponseEntity<>(repository.save(cliente), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// Localiza e retorna o recurso passando o CPF e nome com parâmetros. (GET)
	@GetMapping
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
	@PutMapping
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		repository.save(cliente);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	// Atualiza recurso parcialmente, já existente. (PATCH)
	@PatchMapping
	public ResponseEntity<?> update(@RequestBody Cliente cliente) {
		cliente.setIdade(new Util().getIdade(cliente.getDataNascimento()));
		repository.save(cliente);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	// Retorna recursos paginados.
	@GetMapping(value = "/pagina/")
	public ResponseEntity<?> pageable(Pageable pageable) {
		for (Object object : repository.findAll(pageable)) {
			if (object instanceof Cliente) {
				((Cliente) object).setIdade((new Util().getIdade(((Cliente) object).getDataNascimento())));
			}
		}
		return new ResponseEntity<>(repository.findAll(pageable), HttpStatus.OK);
	}

	// Remove recurso passando o id como parâmetro (DELETE)
	@DeleteMapping
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
