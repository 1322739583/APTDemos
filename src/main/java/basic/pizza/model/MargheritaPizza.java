package basic.pizza.model;


import basic.pizza.annotation.Factory;

@Factory(type=MargheritaPizza.class, id="Margherita")
public class MargheritaPizza implements  Meal {
    @Override
    public float getPrice() {
        return 6.0f;
    }
}