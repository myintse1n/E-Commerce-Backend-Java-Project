package com.shopping.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.shopping.dto.ImageDto;
import com.shopping.model.Image;

public interface IImageService {

	Image getImageById(Long imageId);

	void deleteImage(Long imageId);

	List<ImageDto> saveImages(List<MultipartFile> files, Long productId);

	void updateImage(MultipartFile file, Long imageId);

}
