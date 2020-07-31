package br.com.application.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.application.models.Cliente;

@Repository
public interface ClientesRepository extends JpaRepository<Cliente, Long> {

	/*
	 * Busca o cliente pelo CPF ou nome completo. c.
	 */
	@Query("from Cliente c where c.cpf = ?1 OR c.nome = ?2")
	List<Cliente> find(@RequestParam String cpf, @RequestParam String nome);

	@Modifying
	@Transactional
	@Query("update Cliente c set c.nome = :nome, c.cpf = :cpf ,c.dataNascimento = :dataNascimento where c.id = :id")
	void update(@RequestParam("nome") String nome, @RequestParam("cpf") String cpf,
			@RequestParam("dataNascimento") String dataNascimento, @RequestParam("id") Long id);

}
