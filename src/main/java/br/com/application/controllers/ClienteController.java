package br.com.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	/*
	 * Retorna cliente pelo CPF e nome usando query strings.
	 */
	@RequestMapping(value = "/cliente", method = RequestMethod.GET)
	public String findOne() {
		return "Retornar registro do cliente  pesquisado";
	}

}
