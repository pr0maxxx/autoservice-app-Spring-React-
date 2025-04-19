package com.autoservice.service;

import com.autoservice.model.Order;
import com.autoservice.model.ServiceItem;
import com.autoservice.model.User;
import com.autoservice.repository.ServiceItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final OrderService orderService;

    public List<ServiceItem> findAll() {
        return serviceItemRepository.findAll();
    }

    public ServiceItem findById(Long id) {
        return serviceItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceItem not found"));
    }

    public ServiceItem save(ServiceItem serviceItem) {
        return serviceItemRepository.save(serviceItem);
    }

    public ServiceItem update(Long id, ServiceItem serviceItemDetails) {
        ServiceItem serviceItem = findById(id);
        serviceItem.setName(serviceItemDetails.getName());
        serviceItem.setDescription(serviceItemDetails.getDescription());
        serviceItem.setPrice(serviceItemDetails.getPrice());
        serviceItem.setDurationMinutes(serviceItemDetails.getDurationMinutes());
        return serviceItemRepository.save(serviceItem);
    }
    public void deleteById(Long id) {
        if (!serviceItemRepository.existsById(id)) {
            throw new RuntimeException("Услуга не найдена");
        }
        serviceItemRepository.deleteById(id);
    }
}