package com.autoservice.repository;


import com.autoservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByLastNameContainingIgnoreCase(String username);
    @Query("""
    SELECT DISTINCT u FROM User u
    LEFT JOIN FETCH u.cars c
    LEFT JOIN FETCH c.orders o
    LEFT JOIN FETCH o.service
    """)
    List<User> findAllWithCarsAndOrders();

}