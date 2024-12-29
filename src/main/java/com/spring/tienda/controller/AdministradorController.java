package com.spring.tienda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.tienda.model.Orden;
import com.spring.tienda.model.Producto;
import com.spring.tienda.service.IServiceOrden;
import com.spring.tienda.service.IServiceUsuario;
import com.spring.tienda.service.ServiceProducto;



@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	
	@Autowired
	private ServiceProducto serviceProducto;
	
	@Autowired
	private IServiceUsuario serviceUsuario;
	
	@Autowired
	private IServiceOrden serviceOrden;
	
	@GetMapping("")
	public String home(Model model) {
		
		List<Producto> productos =  serviceProducto.findAll();
		model.addAttribute("productos",productos);
		
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		
		model.addAttribute("usuarios", serviceUsuario.FindAll());
		
		return "administrador/usuarios";
	}
	
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		
		model.addAttribute("ordenes", serviceOrden.findAll());
		
		return "administrador/ordenes";
	}
	
	@GetMapping("/detalleorden/{id}")
	public String detallesOrdenes(@PathVariable Integer id, Model model) {
		
		Orden orden = serviceOrden.findById(id).get(); // Optional<Orden> orden = serviceOrden.findById(id);
        model.addAttribute("detalle", orden.getDetalle()); // cada objeto Orden tiene una lista de detalleOrden
        
		return "administrador/detalleorden";
	}

}
