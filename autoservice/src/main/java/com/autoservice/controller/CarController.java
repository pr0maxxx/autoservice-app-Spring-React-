package com.autoservice.controller;

import com.autoservice.model.Car;
import com.autoservice.model.User;
import com.autoservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    /**
     * Получение списка всех автомобилей (для администраторов)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.findAll());
    }

    /**
     * Получение автомобиля по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.findById(id);
        if (car == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(car);
    }

    //Получение автомобилей текущего пользователя
    @GetMapping("/my")
    public ResponseEntity<List<Car>> getUserCars(@AuthenticationPrincipal User user) {
        List<Car> cars = carService.findByUserId(user.getId());
        return ResponseEntity.ok(cars);
    }

    /**
     * Добавление нового автомобиля
     */
    @PostMapping
    public ResponseEntity<Car> addCar(
            @RequestBody Car car,
            @AuthenticationPrincipal User user) {
        // Валидация данных
        if (car == null || car.getModel() == null || car.getMake() == null) {
            return ResponseEntity.badRequest().build();
        }

        Car createdCar = carService.createCar(car, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCar);
    }

    /**
     * Обновление данных автомобиля
     */
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(
            @PathVariable Long id,
            @RequestBody Car carDetails,
            @AuthenticationPrincipal User user,
            Authentication authentication) throws AccessDeniedException {

        Car existingCar = carService.findById(id);
        if (existingCar == null) {
            return ResponseEntity.notFound().build();
        }
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!existingCar.getOwner().getId().equals(user.getId()) && !isAdmin) {
            throw new AccessDeniedException("Недостаточно прав для обновления этого автомобиля.");
        }

        Car updatedCar = carService.updateCar(id, carDetails, user.getUsername());
        return ResponseEntity.ok(updatedCar);
    }

    /**
     * Удаление автомобиля
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            Authentication authentication) throws AccessDeniedException {

        Car existingCar = carService.findById(id);
        if (existingCar == null) {
            return ResponseEntity.notFound().build();
        }

        // Проверка прав доступа (пользователь может удалить только свой автомобиль или быть администратором)
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!existingCar.getOwner().getId().equals(user.getId()) && !isAdmin) {
            throw new AccessDeniedException("Недостаточно прав для удаления этого автомобиля.");
        }

        carService.deleteCar(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
