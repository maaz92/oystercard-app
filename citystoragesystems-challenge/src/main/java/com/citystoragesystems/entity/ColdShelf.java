package com.citystoragesystems.entity;

import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * The cold shelf which can store cold orders
 * It has a capacity to store 10 orders
 * The shelf decay modifier for cold shelf is 1
 *
 * @author  Mohammad Maaz Khan
 */
@Component
public class ColdShelf extends Shelf {

    /**
     * Constructs an instance of ColdShelf that can be used to store cold orders
     *
     *
     * @author  Mohammad Maaz Khan
     */
    public ColdShelf() {
        super("Cold Shelf", Arrays.asList(Temperature.cold), 10, 1.0);
    }
}
