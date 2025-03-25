package us.com.rclabs.app.parameters;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity class representing a Parameter.
 * This class is mapped to a database table using JPA annotations.
 */
@Entity
public class Parameter {

    /**
     * Unique identifier for the Parameter.
     * This value is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the Parameter.
     */
    private String name;

    /**
     * Value of the Parameter.
     */
    private String value;

    // Getters and Setters

    /**
     * Gets the unique identifier of the Parameter.
     *
     * @return the id of the Parameter
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the Parameter.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the Parameter.
     *
     * @return the name of the Parameter
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Parameter.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the Parameter.
     *
     * @return the value of the Parameter
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the Parameter.
     *
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}