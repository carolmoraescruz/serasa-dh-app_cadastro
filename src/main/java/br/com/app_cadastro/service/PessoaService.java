package br.com.app_cadastro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.app_cadastro.domain.Pessoa;
import br.com.app_cadastro.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	PessoaRepository repository;
	
	public void inserir(Pessoa pessoa) {
		repository.save(pessoa);
	}

}
