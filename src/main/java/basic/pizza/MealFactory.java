package basic.pizza;

import basic.milktea.model.MilkTea;
import basic.pizza.model.CalzonePizza;
import basic.pizza.model.MargheritaPizza;
import basic.pizza.model.Meal;
import basic.pizza.model.Tiramisu;


public class MealFactory {

    public MilkTea create(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null!");
        }
        if ("Calzone".equals(id)) {
            return new CalzonePizza();
        }

        if ("Tiramisu".equals(id)) {
            return new Tiramisu();
        }

        if ("Margherita".equals(id)) {
            return new MargheritaPizza();
        }

        throw new IllegalArgumentException("Unknown id = " + id);
    }
}