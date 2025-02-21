<?php
/*
 * Web Service API per un concessionario di auto
 * - GET ?action=getCars : Lista auto
 * - GET ?action=getBrands : Lista marche
 * - GET ?action=getCustomers : Lista clienti
 * - GET ?action=getCarsWithDetails : Lista auto con marche e clienti
 * - POST ?action=createCar : Crea auto
 * - PUT ?action=updateCar&id={id} : Aggiorna auto
 * - DELETE ?action=deleteCar&id={id} : Elimina auto
 */

include 'DB.php';
header("Content-Type: application/xml; charset=utf-8");

$method = $_SERVER['REQUEST_METHOD'];
$action = $_GET['action'] ?? '';
$id = $_GET['id'] ?? null;

$xml = new SimpleXMLElement("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response/>");

switch ($method) {
    case 'GET':
        if ($action == 'getCars') {
            $result = $conn->query("SELECT * FROM cars");
            foreach ($result->fetch_all(MYSQLI_ASSOC) as $row) {
                $item = $xml->addChild('item');
                foreach ($row as $key => $value) $item->addChild($key, htmlspecialchars($value));
            }
            http_response_code(200);
        } elseif ($action == 'getBrands') {
            $result = $conn->query("SELECT * FROM brands");
            foreach ($result->fetch_all(MYSQLI_ASSOC) as $row) {
                $item = $xml->addChild('item');
                foreach ($row as $key => $value) $item->addChild($key, htmlspecialchars($value));
            }
            http_response_code(200);
        } elseif ($action == 'getCustomers') {
            $result = $conn->query("SELECT * FROM customers");
            foreach ($result->fetch_all(MYSQLI_ASSOC) as $row) {
                $item = $xml->addChild('item');
                foreach ($row as $key => $value) $item->addChild($key, htmlspecialchars($value));
            }
            http_response_code(200);
        } elseif ($action == 'getCarsWithDetails') {
            $result = $conn->query("SELECT cars.model, cars.year, cars.price, cars.color, brands.name AS brand_name, customers.first_name, customers.last_name 
                                   FROM cars 
                                   LEFT JOIN brands ON cars.brand_id = brands.id 
                                   LEFT JOIN customers ON cars.id = customers.car_id");
            foreach ($result->fetch_all(MYSQLI_ASSOC) as $row) {
                $item = $xml->addChild('item');
                foreach ($row as $key => $value) $item->addChild($key, htmlspecialchars($value));
            }
            http_response_code(200);
        } else {
            $xml->addChild('error', 'Invalid action');
            http_response_code(404);
        }
        break;

    case 'POST':
        if ($action == 'createCar') {
            $input = simplexml_load_file("php://input");
            $model = (string)$input->model;
            $brand_id = (int)$input->brand_id;
            $year = (int)$input->year;
            $price = (float)$input->price;
            $color = (string)$input->color;
            if ($model && $brand_id) {
                $stmt = $conn->prepare("INSERT INTO cars (model, brand_id, year, price, color) VALUES (?, ?, ?, ?, ?)");
                $stmt->bind_param("siids", $model, $brand_id, $year, $price, $color);
                $stmt->execute();
                $xml->addChild('message', 'Car created');
                $xml->addChild('id', $conn->insert_id);
                http_response_code(201);
            } else {
                $xml->addChild('error', 'Missing required fields');
                http_response_code(400);
            }
        } else {
            $xml->addChild('error', 'Invalid action');
            http_response_code(404);
        }
        break;

    case 'PUT':
        if ($action == 'updateCar' && $id) {
            $input = simplexml_load_file("php://input");
            $model = (string)$input->model;
            $price = (float)$input->price;
            $color = (string)$input->color;
            if ($model) {
                $stmt = $conn->prepare("UPDATE cars SET model = ?, price = ?, color = ? WHERE id = ?");
                $stmt->bind_param("sdsi", $model, $price, $color, $id);
                $stmt->execute();
                $xml->addChild('message', 'Car updated');
                http_response_code(200);
            } else {
                $xml->addChild('error', 'Missing model');
                http_response_code(400);
            }
        } else {
            $xml->addChild('error', 'Invalid action or missing ID');
            http_response_code(404);
        }
        break;

    case 'DELETE':
        if ($action == 'deleteCar' && $id) {
            $stmt = $conn->prepare("DELETE FROM cars WHERE id = ?");
            $stmt->bind_param("i", $id);
            $stmt->execute();
            if ($stmt->affected_rows > 0) {
                $xml->addChild('message', 'Car deleted');
                http_response_code(200);
            } else {
                $xml->addChild('error', 'Car not found');
                http_response_code(404);
            }
        } else {
            $xml->addChild('error', 'Invalid action or missing ID');
            http_response_code(404);
        }
        break;

    default:
        $xml->addChild('error', 'Method not allowed');
        http_response_code(405);
}

echo $xml->asXML();
?>