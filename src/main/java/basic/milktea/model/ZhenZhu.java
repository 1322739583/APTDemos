package basic.milktea.model;

import basic.milktea.model.MilkTea;
import milktea.annotation.Factory;

/**
 * 珍珠奶茶
 */
@Factory()
public class ZhenZhu implements MilkTea {
    @Override
    public double price() {
        return 8.0;
    }
}
