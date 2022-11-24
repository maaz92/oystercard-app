package com.citystoragesystems.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Order
 *
 * @author  Mohammad Maaz Khan
 */
public class Order implements Serializable {
    private UUID id;

    /**
     * Constructs an Order object
     *
     *
     * @param  id         id of the order
     * @param  name       name of the order
     * @param  temp       temperature of the order
     * @param  shelfLife  shelf life of the order
     * @see               Temperature
     * @author            Mohammad Maaz Khan
     */
    public Order(UUID id, String name, Temperature temp, Long shelfLife, Double decayRate) {
        this.id = id;
        this.name = name;
        this.temp = temp;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
    }

    private String name;

    private Temperature temp;

    private Long shelfLife;

    private Double decayRate;

    private transient Long timeCookedAt;

    private transient Long timePlacedAtShelfOfOwnTemperature;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp= temp;
    }

    public Long getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Long shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Double getDecayRate() {
        return decayRate;
    }

    public void setDecayRate(Double decayRate) {
        this.decayRate = decayRate;
    }

    public Long getTimeCookedAt() {
        return timeCookedAt;
    }

    public void setTimeCookedAt(Long timeCookedAt) {
        this.timeCookedAt = timeCookedAt;
    }

    public Long getTimePlacedAtShelfOfOwnTemperature() {
        return timePlacedAtShelfOfOwnTemperature;
    }

    public void setTimePlacedAtShelfOfOwnTemperature(Long timePlacedAtShelfOfOwnTemperature) {
        this.timePlacedAtShelfOfOwnTemperature = timePlacedAtShelfOfOwnTemperature;
    }

    private static final long serialVersionUID = 42L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
