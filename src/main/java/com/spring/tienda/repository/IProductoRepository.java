package com.spring.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.tienda.model.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer>{

}
