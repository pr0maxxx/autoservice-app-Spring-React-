package com.autoservice.dto;

import com.autoservice.model.Car;
import lombok.Data;

@Data
public class CarDTO {
    private String make;
    private String model;

    public CarDTO(Car car) {
        this.make = car.getMake();
        this.model = car.getModel();
    }
}
