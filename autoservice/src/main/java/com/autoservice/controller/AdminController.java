package com.autoservice.controller;

import com.autoservice.dto.CarDTO;
import com.autoservice.dto.OrderDTO;
import com.autoservice.dto.UserDto;
import com.autoservice.model.Car;
import com.autoservice.model.Order;
import com.autoservice.model.User;
import com.autoservice.repository.UserRepository;
import com.autoservice.service.AuthService;
import com.autoservice.service.CarService;
import com.autoservice.service.OrderService;
import com.autoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;
    private final UserService userService;
    private final OrderService orderService;
    private final CarService carService;
    private final UserRepository userRepository;

    // Получает список всех пользователей в системе
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{userId}/cars")
    public List<Car> getUserCars(@PathVariable Long userId) {
        return carService.findByUserId(userId);
    }


    // Повышает права пользователя до ADMIN
    @PostMapping("/users/{userId}/promote")
    public ResponseEntity<Void> promoteToAdmin(@PathVariable Long userId) {
        authService.promoteToAdmin(userId);
        return ResponseEntity.ok().build();
    }

    // Блокирует пользователя (бан)
    @PostMapping("/users/{userId}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long userId) {
        authService.banUser(userId);
        return ResponseEntity.ok().build();
    }

    // Разблокирует пользователя (снимает бан)
    @PostMapping("/users/{userId}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable Long userId) {
        authService.unbanUser(userId);
        return ResponseEntity.ok().build();
    }

    // Получает список всех записей в системе
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}