<?php
/*
 * Web Service per la  gestione cars , brands e customers
 * risposta data in XML
 *
 * Operazioni disponibili:
 * 1. GET ?action=getCars
 *    - Descrizione: Recupera l'elenco di tutte le auto
 *    - Parametri: Nessuno
 *    - Risposta: <response><item id="X"><model></model><brand_id></brand_id></item></response>
 *
 * 2. GET ?action=getBrands
 *    - Descrizione: Recupera l'elenco di tutte le marche
 *    - Parametri: Nessuno
 *    - Risposta: <response><item id="X"><name></name></item></response>
 *
 * 3. GET ?action=getCustomers
 *    - Descrizione: Recupera l'elenco di tutti i clienti
 *    - Parametri: Nessuno
 *    - Risposta: <response><item id="X"><first_name>...</first_name><last_name>...</last_name>...</item>...</response>
 *
 * 4. GET ?action=getCarsWithDetails
 *    - Descrizione: Recupera l'elenco delle auto con dettagli di marche e clienti (JOIN)
 *    - Parametri: Nessuno
 *    - Risposta: <response><item><model>...</model><brand_name>...</brand_name><first_name>...</first_name>...</item>...</response>
 *
 * 5. POST ?action=createCar
 *    - Descrizione: Crea una nuova auto
 *    - Parametri presi dal java come imput : <model>string</model>, <brand_id>int</brand_id>, <year>int</year>, <price>float</price>, <color>string</color>
 *    - Risposta: <response><message>Auto creata</message><id>X</id></response>
 *
 * 6. POST ?action=createCustomer
 *    - Descrizione: Crea un nuovo cliente
 *    - Parametri presi dal java come imput: <first_name>string</first_name>, <last_name>string</last_name>, <email>string</email>, <car_id>int</car_id>
 *    - Risposta: <response><message>Cliente creato</message><id>prova-1</id></response>
 *
 * 7. PUT ?action=updateCar&id={id}
 *    - Descrizione: Aggiorna un'auto esistente
 *    - Parametri: id , <model>string</model>, <price>float</price>, <color>string</color> (XML nel corpo)
 *    - Risposta: <response><message>Auto aggiornata</message></response>
 *
 * 8. DELETE ?action=deleteCar&id={id}
 *    - Descrizione: Elimina un'auto esistente
 *    - Parametri: id
 *    - Risposta: <response><message>Auto eliminata</message></response>
 */

include 'DB.php'; // Include DB
header('Content-Type: application/xml; charset=utf-8'); // Header risposta XML

$xml = new SimpleXMLElement('<?xml version="1.0" encoding="UTF-8"?><response/>'); // Radice XML
$method = $_SERVER['REQUEST_METHOD']; // Metodo HTTP
$action = $_GET['action'] ?? ''; // Parametro action
$id = $_GET['id'] ?? null; // Parametro id

if ($method == 'GET') { // Richieste GET
    if ($action == 'getCars') { // Lista auto
        //query per la Get 
        $result = $conn->query("SELECT * FROM cars");
        while ($row = $result->fetch_assoc()) {
            //vari tag xml
            //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
            //addAttribute(nomeAttributo, valoreAttributo) aggiunge un attributo alla struttura XML
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
    } elseif ($action == 'getBrands') { // Lista marche
        //query per la get 
        $result = $conn->query("SELECT * FROM brands");
        while ($row = $result->fetch_assoc()) {
            //vari tag xml
            //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
            //addAttribute(nomeAttributo, valoreAttributo) aggiunge un attributo alla struttura XML
            $item = $xml->addChild('item');
            $item->addAttribute('id', $row['id']);
            $item->addChild('name', htmlspecialchars($row['name']));
        }
        $result->free();
        http_response_code(200);
    } elseif ($action == 'getCustomers') { // Lista clienti
        //query per la get 
        $result = $conn->query("SELECT * FROM customers");
        while ($row = $result->fetch_assoc()) {
            //vari tag xml
            //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
            //addAttribute(nomeAttributo, valoreAttributo) aggiunge un attributo alla struttura XML
            $item = $xml->addChild('item');
            $item->addAttribute('id', $row['id']);
            $item->addChild('first_name', htmlspecialchars($row['first_name']));
            $item->addChild('last_name', htmlspecialchars($row['last_name']));
            $item->addChild('email', $row['email']);
            $item->addChild('car_id', $row['car_id']);
        }
        $result->free();
        http_response_code(200);
    } elseif ($action == 'getCarsWithDetails') { // Auto con dettagli
        //query per la get 
        $result = $conn->query("SELECT cars.model, cars.year, cars.price, cars.color, brands.name AS brand_name, customers.first_name, customers.last_name 
                                FROM cars LEFT JOIN brands ON cars.brand_id = brands.id 
                                LEFT JOIN customers ON cars.id = customers.car_id");
        while ($row = $result->fetch_assoc()) {
            //vari tag xml 
            //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
            //addAttribute(nomeAttributo, valoreAttributo) aggiunge un attributo alla struttura XML
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
    } else { // Azione non valida
        $xml->addChild('error', 'Azione non valida');
        http_response_code(404);
    }
} elseif ($method == 'POST' && $action == 'createCar') { // Crea auto
    $input = simplexml_load_file("php://input");
    //Prendo le proprietà dell'oggetto input e le converto in variabile
    $model = (string)$input->model;
    $brand_id = (int)$input->brand_id;
    $year = (int)$input->year;
    $price = (float)$input->price;
    $color = (string)$input->color;
    if ($model && $brand_id) {
        //query per la get 
        //metodo che non esegue immediatamente la query e il ? è un segnaposto per il parametro che andrà concatenato dopo
        $stmt = $conn->prepare("INSERT INTO cars (model, brand_id, year, price, color) VALUES (?, ?, ?, ?, ?)");
        //metodo per la configurazione dei parametri (s=String, i=int, d=double)
        $stmt->bind_param("siids", $model, $brand_id, $year, $price, $color);
        $stmt->execute();
        //tag xml per la risposta 
        //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
        $xml->addChild('message', 'Auto creata');
        $xml->addChild('id', $conn->insert_id);
        http_response_code(201);
    } else {
        $xml->addChild('error', 'Campi obbligatori mancanti');
        http_response_code(400);
    }
} elseif ($method == 'PUT' && $action == 'updateCar' && $id) { // Aggiorna auto
    $input = simplexml_load_file("php://input");
    //Prendo le proprietà dell'oggetto input e le converto in variabile
    $model = (string)$input->model;
    $price = (float)$input->price;
    $color = (string)$input->color;
    if ($model) {
        //metodo che non esegue immediatamente la query e il ? è un segnaposto per il parametro che andrà concatenato dopo
        $stmt = $conn->prepare("UPDATE cars SET model = ?, price = ?, color = ? WHERE id = ?");
        //metodo per la configurazione dei parametri (s=String, i=int, d=double)
        $stmt->bind_param("sdsi", $model, $price, $color, $id);
        $stmt->execute();
        //tag xml per la risposta
        //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
        $xml->addChild('message', 'Auto aggiornata');
        http_response_code(200);
    } else {
        $xml->addChild('error', 'Campo "model" obbligatorio');
        http_response_code(400);
    }
} elseif ($method == 'DELETE' && $action == 'deleteCar' && $id) { // Elimina auto
    //metodo che non esegue immediatamente la query e il ? è un segnaposto per il parametro che andrà concatenato dopo
    $stmt = $conn->prepare("DELETE FROM cars WHERE id = ?");
    //metodo per la configurazione dei parametri (s=String, i=int, d=double)
    $stmt->bind_param("i", $id);
    $stmt->execute();
    if ($stmt->affected_rows > 0) {
        //tag xml per la risposta 
        //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
        $xml->addChild('message', 'Auto eliminata');
        http_response_code(200);
    } else {
        //tag xml per la risposta 
        //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
        $xml->addChild('error', 'Auto non trovata');
        http_response_code(404);
    }
} elseif ($method == 'POST' && $action == 'createCustomer') { // Crea cliente
    $input = simplexml_load_file("php://input");
    //Prendo le proprietà dell'oggetto input e le converto in variabile
    $first_name = (string)$input->first_name;
    $last_name = (string)$input->last_name;
    $email = (string)$input->email;
    $car_id = (int)$input->car_id;
    if ($first_name && $last_name) {
        //metodo che non esegue immediatamente la query e il ? è un segnaposto per il parametro che andrà concatenato dopo
        $stmt = $conn->prepare("INSERT INTO customers (first_name, last_name, email, car_id) VALUES (?, ?, ?, ?)");
        //metodo per la configurazione dei parametri (s=String, i=int, d=double)
        $stmt->bind_param("sssi", $first_name, $last_name, $email, $car_id);
        $stmt->execute();
        //addChild(nomeNodo, valoreNodo): aggiunge un nodo alla struttura XML
        $xml->addChild('message', 'Cliente creato');
        $xml->addChild('id', $conn->insert_id);
        http_response_code(201);
    } else {
        $xml->addChild('error', 'Campi obbligatori mancanti');
        http_response_code(400);
    }
} else { // errore 
    $xml->addChild('error', 'Metodo non valido');
    http_response_code(405);
}

$conn->close(); // Chiude la connessione al database
echo $xml->asXML(); // Restituisce la risposta in formato XML
?>