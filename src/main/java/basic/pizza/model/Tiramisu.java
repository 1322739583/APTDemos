package basic.pizza.model;


import basic.milktea.model.MilkTea;
import milktea.annotation.Factory;

@Factory( )
public class Tiramisu implements MilkTea {


    @Override
    public double price() {
        return 9.5;
    }
}