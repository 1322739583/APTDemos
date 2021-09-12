package basic.pizza;



import basic.milktea.model.MilkTea;
import basic.pizza.model.CalzonePizza;
import basic.pizza.model.MargheritaPizza;
import basic.pizza.model.Meal;
import basic.pizza.model.Tiramisu;

import java.util.Scanner;

public class PizzaStore {

    public MilkTea order(String mealName) {
        if (null == mealName) {
            throw new IllegalArgumentException("name of meal is null!");
        }
        if ("Margherita".equals(mealName)) {
            return new MargheritaPizza();
        }

        if ("Calzone".equals(mealName)) {
            return new CalzonePizza();
        }

        if ("Tiramisu".equals(mealName)) {
            return new Tiramisu();
        }

        throw new IllegalArgumentException("Unknown meal '" + mealName + "'");
    }

    private static String readConsole() {
        Scanner scanner = new Scanner(System.in);
        String meal = scanner.nextLine();
        scanner.close();
        return meal;
    }

    public static void main(String[] args) {
        System.out.println("welcome to pizza store");
        PizzaStore pizzaStore = new PizzaStore();
        MilkTea meal = pizzaStore.order(readConsole());
        System.out.println("Bill:$" + meal.price());

    }
}