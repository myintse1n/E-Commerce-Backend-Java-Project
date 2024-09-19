package com.shopping.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.shopping.model.OrderItem;

public interface OrderItemRepository extends JpaRepositoryImplementation<OrderItem, Long> {

}
