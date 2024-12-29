package com.spring.tienda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.tienda.model.DetalleOrden;
import com.spring.tienda.repository.IDetalleOrdenRepository;

@Service
public class ServiceDetalleOrden implements IServiceDetalleOrden{
	
	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepository;

	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		
		return detalleOrdenRepository.save(detalleOrden);
	}
	
	
	

}
