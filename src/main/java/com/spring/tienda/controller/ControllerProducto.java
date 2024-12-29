package com.spring.tienda.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.tienda.model.Producto;
import com.spring.tienda.model.Usuario;
import com.spring.tienda.service.ServiceProducto;
import com.spring.tienda.service.ServiceUsuario;
import com.spring.tienda.service.UploadFileService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/productos")
public class ControllerProducto {
	
	@Autowired
	private ServiceProducto serviceProducto;
	
	@Autowired
	private ServiceUsuario serviceUsuario;
	
	@Autowired
	private UploadFileService upload;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos",serviceProducto.findAll()); //Model lleva datos del backend al front
		
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) {

		//idusuario es el mismo que usamos en " session.setAttribute("idusuario", user.get().getId());" en UsuarioController metodo acceder.
		Usuario usuario = serviceUsuario.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		producto.setUsuario(usuario);
		
		//guardar la imagen
		if (producto.getId() == null) { // como el producto todavía no se creó, el id es null, cuando se crea un producto 
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
			
		} else {  
			
		}
		
		serviceProducto.save(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		
		Producto p = new Producto();
		Optional<Producto> optionalProducto = serviceProducto.get(id);
		p = optionalProducto.get();
		
		model.addAttribute("productos", p);
		
		
		return "productos/edit";
	}
	
	@PostMapping("/update") // para los cambios de imagen
	public String update(Producto producto, @RequestParam("img") MultipartFile file) {
		
		Producto p = new Producto();
		p = serviceProducto.get(producto.getId()).get();
		
		if (file.isEmpty()) { // editamos el producto pero no cambiamos la imagen
			
			producto.setImagen(p.getImagen());
			
		} else { // si queremos cambiar la imagen cuando editamos el producto
					
			//eliminamos cuando no sea la imagen por defecto
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
			
		}
		
		producto.setUsuario(p.getUsuario());
		serviceProducto.update(producto);
		return "redirect:/productos";
		
	}
	
	@GetMapping("/delete/{id}")
	public String getMethodName(@PathVariable Integer id) {
	    // Intentamos obtener el producto
	    Producto p = serviceProducto.get(id).orElseThrow(() -> 
	        new IllegalArgumentException("Producto no encontrado")
	    );

	    try {
	        // Verifica y elimina la imagen si no es la predeterminada
	        if (!p.getImagen().equals("default.jpg")) {
	            upload.deleteImage(p.getImagen());
	        }

	        // Elimina el producto
	        serviceProducto.delete(id);

	    } catch (Exception e) {
	        // Manejo de errores para evitar eliminar productos con relaciones
	        System.out.println("Error al eliminar producto: " + e.getMessage());
	        return "redirect:/productos?error=true";
	    }

	    return "redirect:/productos";
	}


	
	
	

	
	

}
