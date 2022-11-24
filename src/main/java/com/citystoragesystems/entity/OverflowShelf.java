package com.citystoragesystems.entity;

import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * The overflow shelf which can store any type of orders
 * It has a capacity to store 15 orders
 * The shelf decay modifier for overflow shelf is 2
 *
 * @author  Mohammad Maaz Khan
 */
@Component
public class OverflowShelf extends Shelf {

    /**
     * Constructs an instance of OverflowShelf that can be used to store any type of orders
     *
     *
     * @author  Mohammad Maaz Khan
     */
    public OverflowShelf() {
        super("Overflow Shelf", Arrays.asList(Temperature.any), 15, 2.0);
    }
}
