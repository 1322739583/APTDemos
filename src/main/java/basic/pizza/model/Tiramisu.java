package basic.pizza.model;

import basic.pizza.annotation.Factory;


@Factory(type=Tiramisu.class, id="Tiramisu")
public class Tiramisu implements Meal {
    @Override
    public float getPrice() {
        return 4.5f;
    }
}