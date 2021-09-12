package basic.builder.model;

import milktea.annotation.Builder;
import milktea.annotation.Must;

@Builder
public class Computer {
  //  @Must
    public String cpu;//cpu 必需
  //  @Must
  public String memery;//内存 必需
   // @Must
   public String power;//电源  必需
   // @Must
    private String board;//主板  必需
    private String display;//显示器 可选
    private String box;//机箱 可选
}
