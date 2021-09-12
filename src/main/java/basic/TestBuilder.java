package basic;



public class TestBuilder {
    public static void main(String[] args) {
//        ComputerClient client=new ComputerClient()
//                .newBuilder()
//                .setBoard("ASUS")
//                .setCpu("i5")
//                .setMemery("8g")
//                .build();
//        System.out.println("menery:"+client.memery);

        Student student=new Student()
                .newBuilder()
                .setName("xiao")
                .build();
        System.out.println(student.getName());
    }

}
