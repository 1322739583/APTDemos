package basic.pizza.model;


import basic.milktea.model.MilkTea;
import milktea.annotation.Factory;

@Factory()
public class MargheritaPizza implements MilkTea {

    @Override
    public double price() {
        return 8.0;
    }
}