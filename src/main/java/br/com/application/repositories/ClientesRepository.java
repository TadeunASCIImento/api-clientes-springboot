package br.com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.application.models.Cliente;

@Repository
public interface ClientesRepository extends JpaRepository<Cliente, Long> {

}
