package com.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.Order;

public interface OrderRepository extends JpaRepositoryImplementation<Order, Long> {

	List<Order> findByUserId(Long userId);

}
