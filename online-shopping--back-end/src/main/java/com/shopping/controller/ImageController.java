package com.shopping.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shopping.exception.ResourceNotFoundException;
import com.shopping.response.ApiResponse;
import com.shopping.service.image.IImageService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor
public class ImageController {

	private final IImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,
			@RequestParam Long productId) {

		try {
			var imageDtos = imageService.saveImages(files, productId);
			return ResponseEntity.ok(new ApiResponse("upload success!", imageDtos));

		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("upload failed!", e.getMessage()));
		}
	}

	@GetMapping("/image/download/{imageId}")
	public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {

		var image = imageService.getImageById(imageId);
		ByteArrayResource resource = new ByteArrayResource(
				image.getImage().getBytes(1, (int) image.getImage().length()));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
				.body(resource);
	}

	@PutMapping("/image/update/{imageId}")
	public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {

		try {
			var image = imageService.getImageById(imageId);
			if (image != null) {
				imageService.updateImage(file, imageId);
				return ResponseEntity.ok(new ApiResponse("Update success", image));
			}
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("update failed!", INTERNAL_SERVER_ERROR));
	}
	
	@DeleteMapping("/image/{imageId}/delete")
	public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {

		try {
			var image = imageService.getImageById(imageId);
			if (image != null) {
				imageService.deleteImage(imageId);
				return ResponseEntity.ok(new ApiResponse("Delete success!", null));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
	}


}
