package com.spring.tienda.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//Servicio para controlar la imagen
@Service
public class UploadFileService {
	
	private String folder="images//";
	
	public String saveImage(MultipartFile file) {
		
		if(!file.isEmpty()) {
			
			try {
				
				byte [] bytes = file.getBytes();
				Path path = Paths.get(folder+file.getOriginalFilename());
				Files.write(path, bytes);
				
				return file.getOriginalFilename();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		return "default.jpg";
	}
	
	public void deleteImage(String nombreImagen) {
	
		 String ruta = "images//";
		 File file = new File(ruta+nombreImagen);
		 file.delete();
		
	}

}
