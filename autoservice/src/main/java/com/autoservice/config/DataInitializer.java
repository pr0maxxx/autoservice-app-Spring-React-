package com.autoservice.config;

import com.autoservice.model.Car;
import com.autoservice.model.ServiceItem;
import com.autoservice.model.User;
import com.autoservice.model.enums.Role;
import com.autoservice.repository.CarRepository;
import com.autoservice.repository.ServiceItemRepository;
import com.autoservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final ServiceItemRepository serviceItemRepository;

    @PostConstruct
    @Transactional
    public void init() {
        if (userRepository.count() == 0) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            var admin = User.builder()
                    .username("admin@example.com")
                    .password(encoder.encode("admin"))
                    .role(Role.ROLE_ADMIN)
                    .banned(false)
                    .firstName("Admin")
                    .lastName("Example")
                    .phone("+79991234567")
                    .build();

            var user = User.builder()
                    .username("user@example.com")
                    .password(encoder.encode("user"))
                    .role(Role.ROLE_USER)
                    .banned(false)
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .phone("+79998765432")
                    .build();

            userRepository.saveAll(List.of(admin, user));

            var car1 = Car.builder()
                    .make("Toyota").model("Camry").year(2015)
                    .vin("XUUNF2345JJ0654").licensePlate("A111AA77")
                    .owner(admin).build();

            var car2 = Car.builder()
                    .make("Honda").model("Civic").year(2018)
                    .vin("DSFKSK22332HH56").licensePlate("B222BB99")
                    .owner(user).build();

            carRepository.saveAll(List.of(car1, car2));

            var service1 = new ServiceItem(null, "Замена масла", "Замена масла и масляного фильтра", BigDecimal.valueOf(2000), 30, null);
            var service2 = new ServiceItem(null, "Ротация шин", "Поворот всех четырёх шин для равномерного износа", BigDecimal.valueOf(2500), 20, null);
            var service3 = new ServiceItem(null, "Проверка тормозов", "Проверка и регулировка тормозной системы", BigDecimal.valueOf(3000), 25, null);

            serviceItemRepository.saveAll(List.of(service1, service2, service3));
        }
    }
}
