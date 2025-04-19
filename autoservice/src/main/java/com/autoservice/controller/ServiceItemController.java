package com.autoservice.controller;

import com.autoservice.model.Order;
import com.autoservice.model.ServiceItem;
import com.autoservice.model.User;
import com.autoservice.repository.ServiceItemRepository;
import com.autoservice.service.ServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-items")
@RequiredArgsConstructor
public class ServiceItemController {

    private final ServiceItemService serviceItemService;
    private final ServiceItemRepository serviceItemRepository;


    // Получить все услуги (доступно всем авторизованным)
    @GetMapping
    public ResponseEntity<List<ServiceItem>> getAllServiceItems() {
        return ResponseEntity.ok(serviceItemService.findAll());
    }

    // Получить услугу по ID (доступно всем авторизованным)
    @GetMapping("/{id}")
    public ResponseEntity<ServiceItem> getServiceItemById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceItemService.findById(id));
    }
    // Поиск услуг по названию
    @GetMapping("/search")
    public ResponseEntity<List<ServiceItem>> searchServices(@RequestParam String name) {
        return ResponseEntity.ok(serviceItemRepository.findByNameContainingIgnoreCase(name));
    }

    // Создать новую услугу (только для админов)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceItem> createServiceItem(@RequestBody ServiceItem serviceItem) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceItemService.save(serviceItem));
    }

    // Обновить услугу (только для админов)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceItem> updateServiceItem(
            @PathVariable Long id,
            @RequestBody ServiceItem serviceItemDetails) {
        return ResponseEntity.ok(serviceItemService.update(id, serviceItemDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        serviceItemService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}