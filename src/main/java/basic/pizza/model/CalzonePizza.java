package basic.pizza.model;

import basic.pizza.annotation.Factory;


@Factory(type=CalzonePizza.class, id="Calzone")
public class CalzonePizza implements Meal {
    @Override
    public float getPrice() {
        return 8.5f;
    }
}