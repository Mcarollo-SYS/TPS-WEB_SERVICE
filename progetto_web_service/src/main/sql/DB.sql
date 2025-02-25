CREATE DATABASE dealership;
USE dealership;

CREATE TABLE brands (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL
);

CREATE TABLE cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    brand_id INT,
    year INT,
    price DECIMAL(10, 2),
    color VARCHAR(30),
    FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE SET NULL
);

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    car_id INT,
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE SET NULL
);

INSERT INTO brands (name, country) VALUES 
('Toyota', 'Japan'), 
('Ford', 'USA');

INSERT INTO cars (model, brand_id, year, price, color) VALUES 
('Corolla', 1, 2020, 15000.00, 'Blue'), 
('Focus', 2, 2019, 12000.00, 'Red');

INSERT INTO customers (first_name, last_name, email, car_id) VALUES 
('Marco', 'Rossi', 'marco@example.com', 1), 
('Laura', 'Bianchi', 'laura@example.com', 2);