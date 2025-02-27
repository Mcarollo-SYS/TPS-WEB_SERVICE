package web_service.model;

import static jakarta.xml.bind.annotation.XmlAccessType.FIELD;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute; // Aggiunto
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
@XmlAccessorType(FIELD)
public class Car {
    //metodo costruttore con parametri
    public Car(int id, String model, int brandId, int year, double price, String color) {
        this.id = id;
        this.model = model;
        this.brand_id = brandId;
        this.year = year;
        this.price = price;
        this.color = color;
    }
    @XmlAttribute(name = "id") 
    private int id;

    @XmlElement(name = "model")
    private String model;

    @XmlElement(name = "brand_id")
    private int brand_id;

    @XmlElement(name = "year")
    private int year;

    @XmlElement(name = "price")
    private double price;

    @XmlElement(name = "color")
    private String color;

    //costruttore vuot o
    public Car() {
    }
    //vari getter e setter 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    //metodo toString 
    @Override
    public String toString() {
        return "Car{id=" + id + ", model='" + model + "', brandId=" + brand_id + ", year=" + year + 
               ", price=" + price + ", color='" + color + "'}";
    }
}