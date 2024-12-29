package com.spring.tienda.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.tienda.model.DetalleOrden;
import com.spring.tienda.model.Orden;
import com.spring.tienda.model.Producto;
import com.spring.tienda.model.Usuario;
import com.spring.tienda.service.IServiceDetalleOrden;
import com.spring.tienda.service.IServiceOrden;
import com.spring.tienda.service.ServiceProducto;
import com.spring.tienda.service.ServiceUsuario;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private ServiceProducto serviceProducto;

	@Autowired
	private ServiceUsuario serviceUsuario;

	@Autowired
	private IServiceOrden ordenService;

	@Autowired
	private IServiceDetalleOrden serviceDetalleOrden;

	// Para almacenar los detalles de la orden
	List<DetalleOrden> detalle = new ArrayList<DetalleOrden>();

	// datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession session) {
	    model.addAttribute("productos", serviceProducto.findAll());

	    // Obtener la sesión y el ID del usuario
	    Integer idUsuario = (Integer) session.getAttribute("idusuario");
	    if (idUsuario != null) {
	        // Consultar el usuario y determinar si es administrador
	        Optional<Usuario> usuario = serviceUsuario.findById(idUsuario);
	        if (usuario.isPresent()) {
	            boolean isAdmin = usuario.get().getTipo().equalsIgnoreCase("ADMIN");
	            model.addAttribute("isAdmin", isAdmin); // Si es administrador
	            model.addAttribute("sesion", usuario.get()); // Información del usuario
	        }
	    } else {
	        model.addAttribute("isAdmin", false); // No hay sesión activa, no es admin
	        model.addAttribute("sesion", null); // Sesión vacía
	    }

	    return "usuario/home"; // Retorna la vista principal
	}


	// parte de este metodo sirve para poder cambiar el nav segun si es admin o user
	@GetMapping("productohome/{id}") // el id es del producto y la session para lo que explique arriba
	public String productoHome(@PathVariable Integer id, Model model, HttpSession session) {
		Optional<Producto> productoOptional = serviceProducto.get(id);

		if (productoOptional.isPresent()) {
			Producto p = productoOptional.get();
			model.addAttribute("producto", p);

			// Obtener el usuario desde la sesión
			Integer usuarioId = (Integer) session.getAttribute("idusuario");
			if (usuarioId != null) {
				Optional<Usuario> usuario = serviceUsuario.findById(usuarioId);
				boolean isAdmin = usuario.get().getTipo().equals("ADMIN");
				model.addAttribute("isAdmin", isAdmin);
			} else {
				model.addAttribute("isAdmin", false);
			}

			return "usuario/productohome";

		} else {
			model.addAttribute("error", "Producto no encontrado");
			return "usuario/error"; // Página de error personalizada o alguna página genérica
		}
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model, HttpSession session) {
	    DetalleOrden detalleOrden = new DetalleOrden();
	    Producto p;

	    double sumaTotal = 0;

	    Optional<Producto> optionalProducto = serviceProducto.get(id);
	    if (optionalProducto.isPresent()) {
	        p = optionalProducto.get();

	        // Verificar si el producto ya está en el carrito
	        Optional<DetalleOrden> existente = detalle.stream()
	                .filter(dt -> dt.getProducto().getId().equals(id))
	                .findFirst();

	        if (existente.isPresent()) {
	            // Si ya existe, actualizar la cantidad y el total
	            DetalleOrden d = existente.get();
	            d.setCantidad(d.getCantidad() + cantidad);
	            d.setTotal(d.getPrecio() * d.getCantidad());
	        } else {
	            // Si no existe, crear un nuevo DetalleOrden
	            detalleOrden.setCantidad(cantidad);
	            detalleOrden.setPrecio(p.getPrecio());
	            detalleOrden.setNombre(p.getNombre());
	            detalleOrden.setTotal(p.getPrecio() * cantidad);
	            detalleOrden.setProducto(p);

	            detalle.add(detalleOrden);
	        }
	    }

	    sumaTotal = detalle.stream().mapToDouble(DetalleOrden::getTotal).sum();

	    orden.setTotal(sumaTotal);
	    model.addAttribute("cart", detalle);
	    model.addAttribute("orden", orden);

		// Obtener el usuario desde la sesión
		Integer usuarioId = (Integer) session.getAttribute("idusuario");
		if (usuarioId != null) {
			Optional<Usuario> usuario = serviceUsuario.findById(usuarioId);
			boolean isAdmin = usuario.get().getTipo().equals("ADMIN");
			model.addAttribute("isAdmin", isAdmin);
		} else {
			model.addAttribute("isAdmin", false);
		}

	    return "usuario/carrito";
	}



	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		// Filtrar productos
		List<DetalleOrden> ordenesNueva = detalle.stream()
				.filter(detalleOrden -> !detalleOrden.getProducto().getId().equals(id)).collect(Collectors.toList());

		// Actualizar lista global
		detalle = ordenesNueva;

		// Calcular el nuevo total
		double sumaTotal = detalle.stream().mapToDouble(DetalleOrden::getTotal).sum();
		orden.setTotal(sumaTotal);

		// Añadir atributos al modelo
		model.addAttribute("cart", detalle);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/getCart") // para el "carrito" al lado del "Login"
	public String getCart(Model model, HttpSession session) {
		model.addAttribute("cart", detalle);
		model.addAttribute("orden", orden);

		// session
		model.addAttribute("sesion", session.getAttribute("idusuario"));

		return "/usuario/carrito";
	}

	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
	    Usuario usuario = serviceUsuario.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

	    model.addAttribute("cart", detalle);
	    model.addAttribute("orden", orden);
	    model.addAttribute("usuario", usuario);

	    return "usuario/resumenorden";
	}


	// guardar la orden
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());

		// Usuario
		// idusuario es el mismo que usamos en " session.setAttribute("idusuario",
		// user.get().getId());" en UsuarioController metodo acceder.
		Usuario usuario = serviceUsuario.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		orden.setUsuario(usuario);
		ordenService.save(orden);

		// guardar detalles
		for (DetalleOrden dt : detalle) {
			dt.setOrden(orden);
			serviceDetalleOrden.save(dt);

		}

		// limpiar lista y orden
		orden = new Orden();
		detalle.clear();

		return "redirect:/";
	}

	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model, HttpSession session) {
	    // Buscar productos que coincidan con el nombre
	    List<Producto> productos = serviceProducto.findAll().stream()
	            .filter(x -> x.getNombre().toLowerCase().contains(nombre.toLowerCase()))
	            .collect(Collectors.toList());
	    model.addAttribute("productos", productos);

	    // Verificar si el usuario está logueado
	    Object idUsuarioObj = session.getAttribute("idusuario");
	    if (idUsuarioObj == null) {
	        // Si no hay usuario en la sesión, redirigir al login
	        return "redirect:/usuario/login";
	    }

	    // Obtener usuario desde la base de datos usando el ID de la sesión
	    int idUsuario = Integer.parseInt(idUsuarioObj.toString());
	    Optional<Usuario> usuarioOptional = serviceUsuario.findById(idUsuario);

	    if (!usuarioOptional.isPresent()) {
	        // Si el usuario no existe en la base de datos, redirigir al login
	        return "redirect:/usuario/login";
	    }

	    Usuario usuario = usuarioOptional.get();

	    // Pasar información de la sesión y del usuario al modelo
	    model.addAttribute("sesion", session);
	    model.addAttribute("tipo", usuario.getTipo()); // Esto se usa para decidir qué header mostrar

	    // Determinar la vista según el rol del usuario
	    if ("ADMIN".equalsIgnoreCase(usuario.getTipo())) {
	        return "administrador/home"; // Vista para admin
	    } else {
	        return "usuario/home"; // Vista para usuario
	    }
	}


}
