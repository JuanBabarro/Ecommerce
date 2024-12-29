package com.spring.tienda;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*

Esta clase sirve para poder cargar las imagenes en el home de la página, recorremos con un each los productos y recuperamos la imagen. 

<!-- Page Features -->
<div class="row text-center">

<div class="col-lg-3 col-md-6 mb-4" th:each="producto: ${productos}"> <!-- ${productos} es el nombre que declaramos en la clase administradorController en el metodo model.addAtributte...-->
  <div class="card h-100">
    <img class="card-img-top" th:src="@{/images/{img} (img=${producto.imagen})}" alt="">
    <div class="card-body">
      <p class="card-text" th:text="${producto.nombre}">Nombre del producto.</p>
    </div>
    <div class="card-footer">
      <a href="productohome.html" class="btn btn-success">Ver producto</a>
    </div>
  </div>
</div>

</div> 

*/

@Configuration
public class ResourceWebConfiguration implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
	}

}
