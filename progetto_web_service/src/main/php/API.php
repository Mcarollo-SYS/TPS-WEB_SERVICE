<?php
/*
 * - GET ?action=getCars : Lista auto
 * - GET ?action=getBrands : Lista marche
 * - GET ?action=getCustomers : Lista clienti
 * - GET ?action=getCarsWithDetails : Lista auto con marche e clienti
 * - POST ?action=createCar : Crea auto
 * - PUT ?action=updateCar&id={id} : Aggiorna auto
 * - DELETE ?action=deleteCar&id={id} : Elimina auto
 */

include 'DB.php'; 
header('Content-Type: application/xml; charset=utf-8');

// Radice XML
$xml = new SimpleXMLElement('<?xml version="1.0" encoding="UTF-8"?><response/>');

//  parametri HTTP
$method = $_SERVER['REQUEST_METHOD'];
$action = $_GET['action'] ?? '';
$id = $_GET['id'] ?? null;

// Gestione delle richieste
if ($method == 'GET') {
    if ($action == 'getCars') {
        $result = $conn->query("SELECT * FROM cars");
        while ($row = $result->fetch_assoc()) {
            $item = $xml->addChild('item');
            $item->addAttribute('id', $row['id']);
            $item->addChild('model', htmlspecialchars($row['model']));
            $item->addChild('brand_id', $row['brand_id']);
            $item->addChild('year', $row['year']);
            $item->addChild('price', $row['price']);
            $item->addChild('color', $row['color']);
        }
        $result->free();
        http_response_code(200);
    } elseif ($action == 'getBrands') {
        $result = $conn->query("SELECT * FROM brands");
        while ($row = $result->fetch_assoc()) {
            $item = $xml->addChild('item');
            $item->addAttribute('id', $row['id']);
            $item->addChild('name', htmlspecialchars($row['name']));
        }
        $result->free();
        http_response_code(200);
    } elseif ($action == 'getCustomers') {
        $result = $conn->query("SELECT * FROM customers");
        while ($row = $result->fetch_assoc()) {
            $item = $xml->addChild('item');
            $item->addAttribute('id', $row['id']);
            $item->addChild('first_name', htmlspecialchars($row['first_name']));
            $item->addChild('last_name', htmlspecialchars($row['last_name']));
            $item->addChild('car_id', $row['car_id']);
        }
        $result->free();
        http_response_code(200);
    } elseif ($action == 'getCarsWithDetails') {
        $result = $conn->query("SELECT cars.model, cars.year, cars.price, cars.color, brands.name AS brand_name, customers.first_name, customers.last_name 
                                FROM cars 
                                LEFT JOIN brands ON cars.brand_id = brands.id 
                                LEFT JOIN customers ON cars.id = customers.car_id");
        while ($row = $result->fetch_assoc()) {
            $item = $xml->addChild('item');
            $item->addChild('model', htmlspecialchars($row['model']));
            $item->addChild('year', $row['year']);
            $item->addChild('price', $row['price']);
            $item->addChild('color', htmlspecialchars($row['color']));
            $item->addChild('brand_name', htmlspecialchars($row['brand_name']));
            $item->addChild('first_name', htmlspecialchars($row['first_name']));
            $item->addChild('last_name', htmlspecialchars($row['last_name']));
        }
        $result->free();
        http_response_code(200);
    } else {
        $xml->addChild('error', 'Azione non valida');
        http_response_code(404);
    }
} elseif ($method == 'POST' && $action == 'createCar') {
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
        $xml->addChild('message', 'Auto creata');
        $xml->addChild('id', $conn->insert_id);
        http_response_code(201);
    } else {
        $xml->addChild('error', 'Campi obbligatori mancanti');
        http_response_code(400);
    }
} elseif ($method == 'PUT' && $action == 'updateCar' && $id) {
    $input = simplexml_load_file("php://input");
    $model = (string)$input->model;
    $price = (float)$input->price;
    $color = (string)$input->color;

    if ($model) {
        $stmt = $conn->prepare("UPDATE cars SET model = ?, price = ?, color = ? WHERE id = ?");
        $stmt->bind_param("sdsi", $model, $price, $color, $id);
        $stmt->execute();
        $xml->addChild('message', 'Auto aggiornata');
        http_response_code(200);
    } else {
        $xml->addChild('error', 'Campo "model" obbligatorio');
        http_response_code(400);
    }
} elseif ($method == 'DELETE' && $action == 'deleteCar' && $id) {
    $stmt = $conn->prepare("DELETE FROM cars WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    if ($stmt->affected_rows > 0) {
        $xml->addChild('message', 'Auto eliminata');
        http_response_code(200);
    } else {
        $xml->addChild('error', 'Auto non trovata');
        http_response_code(404);
    }
} else {
    $xml->addChild('error', 'Metodo o azione non valida');
    http_response_code(405);
}

$conn->close();
echo $xml->asXML();
?>