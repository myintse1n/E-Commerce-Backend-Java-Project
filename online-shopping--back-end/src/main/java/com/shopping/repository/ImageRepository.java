package com.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.Image;

public interface ImageRepository extends JpaRepositoryImplementation<Image, Long> {

	List<Image> findByProductId(Long productId);
}
