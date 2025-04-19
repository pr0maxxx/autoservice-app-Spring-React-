package com.autoservice.repository;

import com.autoservice.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    // Найти все услуги, не привязанные к заказу
    List<ServiceItem> findByOrderIsNull();

    // Найти все услуги для конкретного заказа
    List<ServiceItem> findByOrderId(Long orderId);

    // Найти услуги по названию (поиск)
    List<ServiceItem> findByNameContainingIgnoreCase(String name);

    // Найти самые популярные услуги (по количеству использований в заказах)
    @Query("SELECT s FROM ServiceItem s LEFT JOIN s.order GROUP BY s.id ORDER BY COUNT(s.order) DESC")
    List<ServiceItem> findMostPopularServices();
}