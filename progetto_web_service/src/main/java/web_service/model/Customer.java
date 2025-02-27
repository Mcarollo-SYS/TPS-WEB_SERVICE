package web_service.model;

import static jakarta.xml.bind.annotation.XmlAccessType.FIELD;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
@XmlAccessorType(FIELD)
public class Customer {

    //metodo costruttore con parametri
    public Customer(int id, String firstName, String lastName, String email, int carId) {
        this.id = id;
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.car_id = carId;
    }
    //vari tag xml 
    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "first_name")
    private String first_name;

    @XmlElement(name = "last_name")
    private String last_name;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "car_id")
    private int car_id;

    //metodo costruttore vuoto 
    public Customer() {
    }

    //vari getter e setter 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    //metodo tostring 
    @Override
    public String toString() {
        return "Customer{id=" + id + ", firstName='" + first_name + "', lastName='" + last_name + "', email='" + email + "', carId=" + car_id + "}";
    }
}