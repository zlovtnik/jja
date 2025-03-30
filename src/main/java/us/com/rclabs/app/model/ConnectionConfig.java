package us.com.rclabs.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

/**
 * Data model representing a connection configuration.
 */
@Data
@Entity
public class ConnectionConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String connectionName;
    private String userID;
    private String password;
    private String url;
}