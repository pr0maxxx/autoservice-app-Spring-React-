package com.autoservice.repository;

import com.autoservice.model.Order;
import com.autoservice.model.enums.OrderStatus;
import com.autoservice.model.enums.TimeSlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCreatedById(Long userId);
    List<Order> findByCarId(Long carId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCarOwnerId(Long ownerId);
    boolean existsByCarId(Long carId);

}