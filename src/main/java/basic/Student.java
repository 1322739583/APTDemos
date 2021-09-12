package basic;

import milktea.annotation.Builder;

@Builder
public class Student {
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student() {

    }

    public Student(Builder builder) {
        id=builder.id;
        name=builder.name;
    }

    public Builder newBuilder(){
        return new Builder();
    }

    public static final class Builder{
       int id;
       String name;

       public Student build(){
           return new Student(this);
       }

       public Builder setId(int id){
           this.id=id;
           return this;
       }

       public Builder setName(String name){
           this.name=name;
           return this;
       }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
   }
}
