package com.shopping.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shopping.dto.ImageDto;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.model.Image;
import com.shopping.repository.ImageRepository;
import com.shopping.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

	private final ImageRepository imageRepository;
	private final IProductService productService;

	@Override
	public Image getImageById(Long imageId) {
		return imageRepository.findById(imageId)
				.orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + imageId));
	}

	@Override
	public void deleteImage(Long imageId) {
		imageRepository.findById(imageId).ifPresentOrElse(imageRepository::delete, () -> {
			throw new ResourceNotFoundException("No image found with id: " + imageId);
		});

	}

	@Override
	public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {

		var product = productService.getProductById(productId);

		List<ImageDto> savedImageDto = new ArrayList<>();

		for (var file : files) {
			try {
				var image = new Image();
				
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));

				image.setProduct(product);
				
				String buildDownloadUrl = "/api/v1/images/image/download/";
				var downloadUrl = buildDownloadUrl + image.getId();				
				image.setDownloadUrl(downloadUrl);
				
				var savedImage = imageRepository.save(image);
				
				savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
				
				ImageDto imageDto = new ImageDto();
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());
				
				savedImageDto.add(imageDto);
				
			} catch (IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

		return savedImageDto;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
				
		Image image = getImageById(imageId);
		
		try {
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			
			imageRepository.save(image);
			
		} catch (IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
