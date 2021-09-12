package basic.builder.model;

import milktea.annotation.Builder;
import milktea.annotation.Info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于测试的复杂例子
 */
@Info
public class FlexModel implements Serializable {
    public static final String TAG=FlexModel.class.getSimpleName();
    public String name;
    private int id;
    private String gender;

    List<String> list=new ArrayList<>();

    public FlexModel(String name, int id, String gender) {
        this.name = name;
        this.id = id;
        this.gender = gender;
    }

    public FlexModel() {
    }

    public static boolean info(List<String> list){
         return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "FlexModel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", gender='" + gender + '\'' +
                '}';
    }


}
