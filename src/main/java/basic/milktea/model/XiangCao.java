package basic.milktea.model;


import milktea.annotation.Factory;

/**
 * 仙草奶茶
 */
@Factory()
public class XiangCao implements MilkTea {
    @Override
    public double price() {
        return 9.0;
    }
}
