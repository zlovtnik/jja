package com.example.jja.service;

import com.example.jja.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendReportRequest(String reportId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                "report.request",
                reportId);
    }

    public void sendReportStatus(String reportId, String status) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                "report.status",
                new ReportStatus(reportId, status));
    }

    private record ReportStatus(String reportId, String status) {
    }
}