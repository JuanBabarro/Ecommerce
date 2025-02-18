package com.spring.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.tienda.model.Producto;
import com.spring.tienda.repository.IProductoRepository;

@Service
public class ServiceProducto implements IServiceProducto {
	
	@Autowired
	private IProductoRepository productoRepository;

	@Override
	public Producto save(Producto producto) {
	
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {

		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		
		productoRepository.save(producto);
		
	}

	@Override
	public void delete(Integer id) {
		
		productoRepository.deleteById(id);
		
	}

	@Override
	public List<Producto> findAll() {
		
		return productoRepository.findAll();
	}

}
