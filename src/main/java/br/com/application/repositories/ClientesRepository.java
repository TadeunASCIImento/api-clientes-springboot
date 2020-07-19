package br.com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.application.models.Cliente;

@Repository
public interface ClientesRepository extends JpaRepository<Cliente, Long> {

	/*
	 * Retorna o cliente bucando pelo cpf informado.
	 */
	@Query("select c from Cliente c where c.cpf = :cpf")
	public Cliente findByCpf(@RequestParam("cpf") String cpf);

	/*
	 * Retorna o cliente bucando pelo nome completo informado.
	 */

	@Query("select c from Cliente c where c.nome = :nome")
	public Cliente findByNome(@RequestParam("nome") String nome);
}
