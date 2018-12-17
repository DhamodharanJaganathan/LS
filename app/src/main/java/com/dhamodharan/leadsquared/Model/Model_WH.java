package com.dhamodharan.leadsquared.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_WH {

  @SerializedName("cnt")
  @Expose
  private Integer cnt;
  @SerializedName("list")
  @Expose
  private java.util.List<List> list = null;

  public Integer getCnt() {
    return cnt;
  }

  public void setCnt(Integer cnt) {
    this.cnt = cnt;
  }

  public java.util.List<List> getList() {
    return list;
  }

  public void setList(java.util.List<List> list) {
    this.list = list;
  }


  public class List {

    @SerializedName("main")
    @Expose
    private Main main;

    public Main getMain() {
      return main;
    }

    public void setMain(Main main) {
      this.main = main;
    }

  }

  public class Main {

    @SerializedName("temp")
    @Expose
    private double temp;

    public double getTemp() {
      return temp;
    }

    public void setTemp(double temp) {
      this.temp = temp;
    }

  }
}
