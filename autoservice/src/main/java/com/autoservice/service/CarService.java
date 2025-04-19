package com.autoservice.service;

import com.autoservice.model.Car;
import com.autoservice.model.Order;
import com.autoservice.model.User;
import com.autoservice.repository.CarRepository;
import com.autoservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;

    // Получение всех автомобилей
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    // Поиск автомобиля по ID
    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id: " + id));
    }

    // Поиск автомобилей пользователя
    public List<Car> findByUserId(Long userId) {
        User user = userService.findById(userId);
        return carRepository.findByOwnerId(user.getId());
    }

    // Создание нового автомобиля
    public Car createCar(Car car, String username) {
        User owner = userService.findByUsername(username);

        if (carRepository.existsByVin(car.getVin())) {
            throw new IllegalStateException("Car with this VIN already exists");
        }

        if (carRepository.existsByLicensePlate(car.getLicensePlate())) {
            throw new IllegalStateException("Car with this license plate already exists");
        }

        car.setOwner(owner);
        return carRepository.save(car);
    }

    // Обновление данных автомобиля
    public Car updateCar(Long id, Car carDetails, String username) throws AccessDeniedException {
        Car car = findById(id);
        User owner = userService.findByUsername(username);

        if (!car.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("You don't own this car");
        }

        if (carDetails.getVin() != null && !carDetails.getVin().equals(car.getVin())) {
            if (carRepository.existsByVin(carDetails.getVin())) {
                throw new IllegalStateException("Car with this VIN already exists");
            }
            car.setVin(carDetails.getVin());
        }

        if (carDetails.getLicensePlate() != null && !carDetails.getLicensePlate().equals(car.getLicensePlate())) {
            if (carRepository.existsByLicensePlate(carDetails.getLicensePlate())) {
                throw new IllegalStateException("Car with this license plate already exists");
            }
            car.setLicensePlate(carDetails.getLicensePlate());
        }

        car.setMake(carDetails.getMake());
        car.setModel(carDetails.getModel());
        car.setYear(carDetails.getYear());

        return carRepository.save(car);
    }

    // Удаление автомобиля
    public void deleteCar(Long carId, Long userId) throws AccessDeniedException {
        // Получаем автомобиль по ID
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id: " + carId));

        // Получаем все связанные записи (Orders) для этого автомобиля
        List<Order> orders = orderRepository.findByCarId(carId);

        // Удаляем связанные заказы (если нужно)
        for (Order order : orders) {
            order.setCar(null); // Отсоединяем автомобиль от записи
            orderRepository.save(order); // Сохраняем изменения
        }

        // Теперь можно безопасно удалить автомобиль
        carRepository.delete(car);
    }

}
