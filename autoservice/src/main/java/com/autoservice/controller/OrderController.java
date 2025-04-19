package com.autoservice.controller;

import com.autoservice.dto.OrderDTO;
import com.autoservice.dto.OrderRequestDTO;
import com.autoservice.model.Order;
import com.autoservice.model.User;
import com.autoservice.model.enums.OrderStatus;
import com.autoservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //Получение списка заказов текущего пользователя
    @GetMapping("/my")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@AuthenticationPrincipal User user) {
        List<OrderDTO> orders = orderService.getUserOrders(user.getId());
        return ResponseEntity.ok(orders);
    }

    //Создание нового заказа
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequestDTO dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.createOrder(dto, user));
    }


    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(new OrderDTO(updatedOrder));
    }

    //Получение заказов клиента (доступно владельцу или администратору)
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderDTO>> getClientOrders(
            @PathVariable Long clientId,
            @AuthenticationPrincipal User user) {
        // Получаем заказы клиента и возвращаем их в виде DTO
        List<OrderDTO> ordersDTO = orderService.getClientOrders(clientId);
        return ResponseEntity.ok(ordersDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id, @AuthenticationPrincipal User user) throws AccessDeniedException {
        orderService.deleteOrder(id, user.getId());
        return ResponseEntity.noContent().build();
    }

}