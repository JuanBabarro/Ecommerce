package com.spring.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.tienda.model.Orden;
import com.spring.tienda.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {

	// para obtener las ordenes del usuario
	List<Orden> findByUsuario(Usuario usuario);
	
}
