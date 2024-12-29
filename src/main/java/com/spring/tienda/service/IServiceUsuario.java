package com.spring.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring.tienda.model.Usuario;

@Service
public interface IServiceUsuario {
	
	public Optional<Usuario> findById(Integer id);
	
	public Usuario save(Usuario usuario);
	
	Optional<Usuario> FindByEmail(String email);
	
	public  List<Usuario> FindAll();
	

}
