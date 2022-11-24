package com.citystoragesystems.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Base class for any type of shelf
 * Each shelf has a capacity and a shelf decay modifier
 *
 * @author  Mohammad Maaz Khan
 */
public abstract class Shelf {
    private final String name;

    private final List<Temperature> allowableTemperatures;

    private final Integer capacity;

    protected final List<Order> orders;

    private final double shelfDecayModifier;

    /**
     * Constructs an instance of Subclass of Shelf that can be used to store orders.
     *
     *
     * @param  name                  name of the shelf
     * @param  allowableTemperatures  allowable temperature of the shelf
     * @param  capacity              capacity of shelf
     * @param  shelfDecayModifier    shelf decay modifier of the shelf
     * @see                          Temperature
     * @author                       Mohammad Maaz Khan
     */
    public Shelf(String name, List<Temperature> allowableTemperatures, Integer capacity, Double shelfDecayModifier) {
        this.name = name;
        this.allowableTemperatures = allowableTemperatures;
        this.capacity = capacity;
        this.shelfDecayModifier = shelfDecayModifier;
        this.orders = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public List<Temperature> getAllowableTemperatures() {
        return allowableTemperatures;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Double getShelfDecayModifier() {
        return shelfDecayModifier;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return name.equals(shelf.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "name='" + name + '\'' +
                '}';
    }
}
