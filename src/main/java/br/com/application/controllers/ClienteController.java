package br.com.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import br.com.application.models.Cliente;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private RestTemplate restTamplate;

	String uriBase = "http://localhost:8080/clientes/";

	@RequestMapping(value = "/formulario", method = RequestMethod.GET)
	public ModelAndView formulario(Cliente cliente) {
		ModelAndView modelAndView = new ModelAndView("formulario");
		modelAndView.addObject("clientes",
				restTamplate.getForObject(uriBase.concat("/pagina/"), List.class, Cliente.class));
		return modelAndView;
	}

	@RequestMapping(value = "/cadastrar/", method = RequestMethod.POST)
	public ModelAndView filtrar(@RequestParam(value = "cliente", required = false) Cliente cliente) {
		ModelAndView modelAndView = new ModelAndView("formulario");
		try {
			restTamplate.postForObject(uriBase.concat("/novo/"), cliente, Cliente.class);
			modelAndView.addObject("cliente", cliente);
		} catch (HttpClientErrorException exception) {
			return new ModelAndView("redirect:/clientes/formulario");
		}
		return modelAndView;
	}

}
