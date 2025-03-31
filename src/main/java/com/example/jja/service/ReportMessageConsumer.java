package com.example.jja.service;

import com.example.jja.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportMessageConsumer {

    private final ReportMessageProducer messageProducer;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleReportRequest(String reportId, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Received report request for ID: {}", reportId);

            // Process the report request
            processReport(reportId);

            // Send status update
            messageProducer.sendReportStatus(reportId, "COMPLETED");

            log.info("Successfully processed report request for ID: {}", reportId);
        } catch (Exception e) {
            log.error("Error processing report request for ID: {}", reportId, e);
            messageProducer.sendReportStatus(reportId, "FAILED");
        }
    }

    private void processReport(String reportId) {
        // Implement your report processing logic here
        // This is where you would:
        // 1. Fetch report configuration
        // 2. Connect to the database
        // 3. Generate the report
        // 4. Save the results
        log.info("Processing report with ID: {}", reportId);
    }
}