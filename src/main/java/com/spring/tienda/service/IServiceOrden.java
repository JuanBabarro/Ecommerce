package com.spring.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring.tienda.model.Orden;
import com.spring.tienda.model.Usuario;

@Service
public interface IServiceOrden {
	
	public Orden save(Orden orden);
	
	public List<Orden> findAll();
	
	public String generarNumeroOrden();
	
	public List<Orden> FindByUsuario (Usuario usuario);
	
	public Optional<Orden> findById(Integer id);

}
