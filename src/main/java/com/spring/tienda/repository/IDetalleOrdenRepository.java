package com.spring.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.tienda.model.DetalleOrden;

import jakarta.transaction.Transactional;

public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {
	
	//Esto es para el metodo eliminar producto en Controller Producto
    @Transactional
    @Modifying
    @Query("DELETE FROM DetalleOrden d WHERE d.producto.id = :productoId")
    void deleteByProductoId(@Param("productoId") Integer productoId);
}

