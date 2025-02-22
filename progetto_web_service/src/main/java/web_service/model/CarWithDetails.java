package web_service.model;

import static jakarta.xml.bind.annotation.XmlAccessType.FIELD;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
@XmlAccessorType(FIELD)
public class CarWithDetails {
    @XmlElement(name = "model")
    private String model;

    @XmlElement(name = "year")
    private int year;

    @XmlElement(name = "price")
    private double price;

    @XmlElement(name = "color")
    private String color;

    @XmlElement(name = "brand_name")
    private String brand_name;

    @XmlElement(name = "first_name")
    private String first_name;

    @XmlElement(name = "last_name")
    private String last_name;

    public CarWithDetails() {}

    public CarWithDetails(String model, int year, double price, String color, String brandName, String firstName, String lastName) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color;
        this.brand_name = brandName;
        this.first_name = firstName;
        this.last_name = lastName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
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

    @Override
    public String toString() {
        return "CarWithDetails{model='" + model + "', year=" + year + ", price=" + price + ", color='" + color + 
               "', brandName='" + brand_name + "', firstName='" + first_name + "', lastName='" + last_name + "'}";
    }
}