package com.spring.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.tienda.model.Usuario;
import com.spring.tienda.repository.IUsuarioRepository;

@Service
public class ServiceUsuario implements IServiceUsuario {
	
	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Optional<Usuario> findById(Integer id) {
		
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> FindByEmail(String email) {
		
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public List<Usuario> FindAll() {
		
		return usuarioRepository.findAll();
	}

}
