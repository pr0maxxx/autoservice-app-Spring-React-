package com.autoservice.dto;

import com.autoservice.model.Order;
import com.autoservice.model.ServiceItem;
import com.autoservice.model.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private ServiceItem service; // название услуги
    private OrderStatus status;
    private LocalDateTime date;
    private CarDTO car; // вложенный DTO

    public OrderDTO(Long id, ServiceItem service, OrderStatus status, LocalDateTime date, CarDTO car) {
        this.id = id;
        this.service = service;
        this.status = status;
        this.date = date;
        this.car = car;
    }
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.service = order.getService(); // или map в ServiceItemDTO
        this.status = order.getStatus();
        this.date = order.getAppointmentTime();
        this.car = new CarDTO(order.getCar());
    }
    // конструктор, геттеры/сеттеры
}
