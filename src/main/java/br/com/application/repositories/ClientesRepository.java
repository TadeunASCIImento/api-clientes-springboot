package br.com.application.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.application.models.Cliente;

@Repository
public interface ClientesRepository extends PagingAndSortingRepository<Cliente, Long> {

	/*
	 * Busca o cliente pelo CPF ou nome completo.
	 */
	@Query("SELECT c FROM Cliente c WHERE c.cpf = :cpf OR c.nome = :nome")
	public Cliente findOne(@RequestParam("cpf") String cpf, @RequestParam("nome") String nome);

}
