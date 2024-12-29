package com.spring.tienda.service;

import org.springframework.stereotype.Service;

import com.spring.tienda.model.DetalleOrden;

@Service
public interface IServiceDetalleOrden {
	
	 public DetalleOrden save(DetalleOrden detalleOrden);
	 
	 

}
