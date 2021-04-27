package com.hithaui.controllers;

import java.io.IOException;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AppController {

	@PostMapping("/images")
	public String uploadFile(@PathParam("file") MultipartFile file) throws IOException {
		// upload file to cloudinary ...
		return file.getOriginalFilename();
	}
}
