package web_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class Car {
    private int id;
    private String model;
    private int brand_id;
    private int year;
    private double price;
    private String color;

    public Car() {}
    public Car(int id, String model, int brandId, int year, double price, String color) {
        this.id = id;
        this.model = model;
        this.brand_id = brandId;
        this.year = year;
        this.price = price;
        this.color = color;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getBrand_id() { return brand_id; }
    public void setBrand_id(int brand_id) { this.brand_id = brand_id; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    @Override
    public String toString() {
        return "Car{id=" + id + ", model='" + model + "', brandId=" + brand_id + ", year=" + year + ", price=" + price + ", color='" + color + "'}";
    }
}