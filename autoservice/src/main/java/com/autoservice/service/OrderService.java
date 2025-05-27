package com.autoservice.service;

import com.autoservice.dto.CarDTO;
import com.autoservice.dto.OrderDTO;
import com.autoservice.dto.OrderRequestDTO;
import com.autoservice.model.Car;
import com.autoservice.model.Order;
import com.autoservice.model.ServiceItem;
import com.autoservice.model.User;
import com.autoservice.model.enums.OrderStatus;
import com.autoservice.repository.CarRepository;
import com.autoservice.repository.OrderRepository;
import com.autoservice.repository.ServiceItemRepository;
import com.autoservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final NotificationService notificationService;


    public Order createOrder(OrderRequestDTO dto, User user) {
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        if (!car.getOwner().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("Car does not belong to user");
        }

        ServiceItem service = serviceItemRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        Order order = new Order();
        order.setCar(car);
        order.setCreatedBy(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setAppointmentTime(dto.getDate());
        order.setService(service);

        // Обязательно установить обратную ссылку, если нужно:
        service.setOrder(order);

        return orderRepository.save(order);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByCreatedById(userId);
        return orders.stream().map(order -> {
            ServiceItem serviceName = order.getService(); // если Service — это объект
            CarDTO carDTO = new CarDTO(order.getCar());        // если Car — это объект
            return new OrderDTO(order.getId(), serviceName, order.getStatus(), order.getAppointmentTime(), carDTO);
        }).collect(Collectors.toList());
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        notificationService.sendStatusChangeEmail(
                order.getUser().getUsername(),
                order.getService().getName(),
                order.getAppointmentTime(),
                status
        );

        if (status == OrderStatus.COMPLETED) {
            order.complete();
        }

        return orderRepository.save(order);
    }

    public List<OrderDTO> getClientOrders(Long clientId) {
        List<Order> orders = orderRepository.findByCarOwnerId(clientId);
        return orders.stream()
                .map(this::mapToDTO)  // Преобразование каждого заказа в DTO
                .collect(Collectors.toList());
    }

    // Маппинг сущности Order в OrderDTO
    private OrderDTO mapToDTO(Order order) {
        Car car = order.getCar();
        CarDTO carDTO = new CarDTO(car);  // Создаем CarDTO из Car сущности

        return new OrderDTO(
                order.getId(),
                order.getService(),  // Сервис (например, название услуги)
                order.getStatus(),
                order.getAppointmentTime(),
                carDTO
        );
    }

    /**
     * Удаление заказа (записи) для услуги.
     */
    public void deleteOrder(Long orderId, Long userId) throws AccessDeniedException {
        // Получаем заказ по ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Проверяем, является ли текущий пользователь владельцем записи
        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this order");
        }

        // Если заказ связан с услугой, убираем связь
        if (order.getService() != null) {
            order.setService(null); // Убираем связь с услугой
        }

        // Удаляем заказ из базы данных
        orderRepository.delete(order);
    }



}