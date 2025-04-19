package com.autoservice.model;


import com.autoservice.model.enums.OrderStatus;
import com.autoservice.model.enums.TimeSlotStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;


import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "service_orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.NEW;

    @Column(nullable = false)
    private LocalDateTime appointmentTime; // Время записи

    @Enumerated(EnumType.STRING)
    private TimeSlotStatus timeSlotStatus = TimeSlotStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    @NotNull
    @JsonBackReference("car-orders")
    private Car car;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-orders")
    @JsonIgnore
    private User createdBy;
    public User getUser() {
        return createdBy;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    @JsonManagedReference("order-services")
    private ServiceItem service;

    public ServiceItem getService() {
        return service;
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
