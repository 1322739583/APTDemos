//package basic.milktea;
//
//
//import basic.milktea.model.BuDing;
//import basic.milktea.model.MilkTea;
//import basic.milktea.model.XiangCao;
//import basic.milktea.model.ZhenZhu;
//
//public class MilkTeaFactory {
//
//    public static MilkTea createByName(String name){
//        if (name==null){
//           throw new IllegalArgumentException("name can't be null");
//        }
//
//        //如果用else if判断多个条件，最后一个else如果不写条件会降低可读性
//        if ("ZhenZhu".equals(name)){
//            //因为所有条件都有return所以和if else是一样的
//            return new ZhenZhu();
//        }
//
//        if ("XiangCao".equals(name)){
//            return new  XiangCao();
//        }
//
//        if ("BuDing".equals(name)){
//            return new BuDing();
//        }
//
//        throw new IllegalArgumentException("Unkown name "+name);
//    }
//
//    public static MilkTea createByClass(Class< ? extends MilkTea> clazz){
//        if (clazz==null){
//            throw  new IllegalArgumentException("Class file can't be null.Please input the Class file name etc: xxx.class");
//        }
//
//        if (clazz.equals( ZhenZhu.class)){
//            //因为所有条件都有return所以和if else是一样的
//            return new ZhenZhu();
//        }
//
//        if (clazz.equals( XiangCao.class)){
//            return new XiangCao();
//        }
//
//        if (clazz.equals(BuDing.class)){
//            return new BuDing();
//        }
//
//        throw new IllegalArgumentException("Unkown Class "+clazz.getSimpleName());
//    }
//}
