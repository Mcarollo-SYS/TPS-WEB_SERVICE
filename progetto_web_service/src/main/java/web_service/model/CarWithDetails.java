package web_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class CarWithDetails {
    private String model;
    private int year;
    private double price;
    private String color;
    private String brand_name;
    private String first_name;
    private String last_name;

    public CarWithDetails() {}

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getBrand_name() { return brand_name; }
    public void setBrand_name(String brand_name) { this.brand_name = brand_name; }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    @Override
    public String toString() {
        return "CarWithDetails{model='" + model + "', year=" + year + ", price=" + price + ", color='" + color + "', brandName='" + brand_name + "', firstName='" + first_name + "', lastName='" + last_name + "'}";
    }
}
