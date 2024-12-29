package com.spring.tienda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.tienda.model.Orden;
import com.spring.tienda.model.Usuario;
import com.spring.tienda.repository.IOrdenRepository;

@Service
public class ServiceOrden implements IServiceOrden {
	
	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		
		return ordenRepository.findAll();
	}
	
	public String generarNumeroOrden() {
		
		int numero = 0;
		String numeroConcatenado = "";
		
		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<Integer>();
		
		//agregamos a la lista numeros los integers de ordenes ya que de la bd vienen en cadena
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		
		if(ordenes.isEmpty()) {
			numero = 1;
			
		} else { // obtenemos el mayor numero (el último) de la lista numeros
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if(numero < 10) {
			numeroConcatenado = "000000000"+String.valueOf(numero);
		} else if(numero < 100) {
			numeroConcatenado = "00000000"+String.valueOf(numero);
		} else if (numero < 1000) {
			numeroConcatenado = "0000000"+String.valueOf(numero);
		} else if(numero < 10000) {
			numeroConcatenado = "000000"+String.valueOf(numero);
		}
		
		
		return numeroConcatenado;
	}

	@Override // para obtener las ordenes del usuario
	public List<Orden> FindByUsuario(Usuario usuario) {
		
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		
		return ordenRepository.findById(id);
	}

}
