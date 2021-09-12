package basic.pizza.model;


import basic.milktea.model.MilkTea;
import milktea.annotation.Factory;

@Factory()
public class CalzonePizza implements MilkTea {

    @Override
    public double price() {
        return 7.7;
    }
}