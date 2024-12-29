package com.spring.tienda.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.tienda.model.DetalleOrden;
import com.spring.tienda.model.Orden;
import com.spring.tienda.model.Usuario;
import com.spring.tienda.service.IServiceOrden;
import com.spring.tienda.service.IServiceUsuario;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private IServiceUsuario serviceUsuario;
	
	@Autowired
	private IServiceOrden serviceOrden;
	
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		usuario.setTipo("USER");
		serviceUsuario.save(usuario);
		
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		Optional<Usuario> user =  serviceUsuario.FindByEmail(usuario.getEmail());
		
		if(user.isPresent()) { // Si hay un usuario
		   session.setAttribute("idusuario", user.get().getId()); // guardamos en "idusuario" el id del usuario user
		   if(user.get().getTipo().equals("ADMIN")) { // verificamos que el usuario es ADMIN
			   return "redirect:/administrador";
		   } else {
			   return "redirect:/";
		   }
		} else { // si no hay un usuario
			System.out.println("El usuario no existe");
		}
		
		return "redirect:/";
	}
	
	
	//Con esto mostramos las ordenes correspondientes al usuario logeado
	@GetMapping("/compras")
	public String obtenerCompras(HttpSession session, Model model) {
	    // Validar que el usuario esté autenticado
	    Object idUsuarioObj = session.getAttribute("idusuario");
	    if (idUsuarioObj == null) {
	        return "redirect:/login"; // Redirigir a la página de inicio de sesión si no hay usuario
	    }

	    // Obtener el ID del usuario desde la sesión
	    int idUsuario;
	    try {
	        idUsuario = Integer.parseInt(idUsuarioObj.toString());
	    } catch (NumberFormatException e) {
	        return "redirect:/login"; // Redirigir en caso de error al convertir el ID
	    }

	    // Buscar el usuario en la base de datos
	    Usuario usuario = serviceUsuario.findById(idUsuario).orElse(null);
	    if (usuario == null) {
	        return "redirect:/login"; // Redirigir si el usuario no existe en la base de datos
	    }

	    // Obtener las órdenes asociadas al usuario
	    List<Orden> ordenes = serviceOrden.FindByUsuario(usuario);

	    // Pasar los datos al modelo
	    model.addAttribute("sesion", idUsuario);
	    model.addAttribute("ordenes", ordenes);

	    return "usuario/detallecompra"; // Vista de las compras
	}

	

	@GetMapping("/cerrar")
	public String cerraroSesion(HttpSession session) {
		
		session.removeAttribute("idusuario");
		
		return "usuario/login";
	}
	
	
	@GetMapping("/detalle/{id}")
	public String detallesOrden(@PathVariable Integer id, HttpSession session, Model model) {
	    // Validar sesión
	    Object idUsuarioObj = session.getAttribute("idusuario");
	    if (idUsuarioObj == null) {
	        return "redirect:/login"; // Redirigir si no hay sesión activa
	    }

	    // Obtener la orden por ID
	    Orden orden = serviceOrden.findById(id).orElse(null);
	    if (orden == null) {
	        return "redirect:/usuario/compras"; // Redirigir si la orden no existe
	    }

	    // Procesar detalles
	    List<DetalleOrden> detalles = orden.getDetalle().stream()
	        .map(detalle -> {
	            // Calcular subtotal y agregarlo al objeto
	            detalle.setTotal(detalle.getCantidad() * detalle.getPrecio());
	            return detalle;
	        }).toList();

	    // Agregar los detalles y la sesión al modelo
	    model.addAttribute("detalles", detalles);
	    model.addAttribute("sesion", idUsuarioObj);

	    return "usuario/compras"; // Retorna la vista correspondiente
	}




	

}
