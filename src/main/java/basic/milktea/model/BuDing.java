package basic.milktea.model;


import milktea.annotation.Factory;

/**
 * 布丁奶茶
 */
@Factory(name = "BuDing")
public class BuDing implements  MilkTea {
    @Override
    public double price() {
        return 8.5;
    }
}
