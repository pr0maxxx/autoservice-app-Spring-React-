package com.autoservice.service;

import com.autoservice.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendStatusChangeEmail(String to, String serviceName, LocalDateTime appointmentTime, OrderStatus newStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Изменение статуса записи на услугу");

        String formattedDateTime = appointmentTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String body = String.format(
                "Статус вашей записи на услугу \"%s\" на %s был изменён на: %s.",
                serviceName, formattedDateTime, getStatusDisplayName(newStatus)
        );

        message.setText(body);
        mailSender.send(message);
    }
    private String getStatusDisplayName(OrderStatus status) {
        return switch (status) {
            case NEW -> "В обработке";
            case IN_PROGRESS -> "В процессе";
            case WAITING_FOR_PARTS -> "Ожидание запчастей";
            case READY_FOR_PICKUP -> "Готова к выдаче";
            case COMPLETED -> "Завершена";
            case CANCELLED -> "Отменена";
        };
    }
}
