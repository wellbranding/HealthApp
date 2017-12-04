package udacityteam.healthapp.activities;

import java.util.List;

/**
 * Created by kunda on 10/4/2017.
 */

public class Request {
    private String phone;
    private String name;
    private String manis;
    private String total;
    private String status;//List of food order

    public Request() {
    }

    public Request(String phone, String name, String manis, String total) {
        this.phone = phone;
        this.name = name;
        this.manis = manis;
        this.total = total;
      //  this.foods = foods;
        this.status = "0";
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return  phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManis() {
        return manis;
    }

    public void setManis(String manis) {
        this.manis = manis;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


}
