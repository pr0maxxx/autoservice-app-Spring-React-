package com.autoservice.repository;

import com.autoservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByVin(String vin);
    Optional<Car> findByLicensePlate(String licensePlate);

    List<Car> findByOwnerId(Long ownerId); // если owner теперь User — можно оставить

    boolean existsByVin(String vin);
    boolean existsByLicensePlate(String licensePlate);

    @Query("SELECT c FROM Car c WHERE c.owner.id = :userId")
    List<Car> findByUserId(@Param("userId") Long userId);
}
