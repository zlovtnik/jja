package us.com.rclabs.app.parameters;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entity class representing a Parameter.
 * This class is mapped to a database table using JPA annotations.
 */
@Data
@Entity
public class FndParameter {

    /**
     * Unique identifier for the Parameter.
     * This value is generated automatically.
     * -- GETTER --
     *  Gets the unique identifier of the Parameter.
     *
     * @return the id of the Parameter

     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the Parameter.
     * -- GETTER --
     *  Gets the name of the Parameter.
     *
     * @return the name of the Parameter

     */
    private String name;

    /**
     * Value of the Parameter.
     * -- GETTER --
     *  Gets the value of the Parameter.
     *
     * @return the value of the Parameter

     */
    private String value;
}