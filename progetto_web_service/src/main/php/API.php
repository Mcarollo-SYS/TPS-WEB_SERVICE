<?php
/*
 * Web Service API per la gestione di una biblioteca
 * Operazioni disponibili:
 * - GET /api.php?action=getBooks : Lista tutti i libri (Read)
 * - GET /api.php?action=getAuthors : Lista tutti gli autori (Read)
 * - GET /api.php?action=getBooksWithAuthors : Lista libri con autori (Join)
 * - POST /api.php?action=createBook : Crea un nuovo libro
 * - PUT /api.php?action=updateBook&id={id} : Aggiorna un libro esistente
 * - DELETE /api.php?action=deleteBook&id={id} : Elimina un libro
 * Parametri opzionali:
 * - format=xml (default) o format=json per scegliere il formato della risposta
 */

include 'DB.php';

// Impostazione header dinamico per XML o JSON
$format = $_GET['format'] ?? 'xml'; // Default XML
header("Content-Type: " . ($format === 'json' ? 'application/json' : 'application/xml') . "; charset=utf-8");

// Funzione per inviare risposte con codici di stato
function sendResponse($data, $status = 200, $format = 'xml') {
    http_response_code($status);
    if ($format === 'json') {
        echo json_encode($data);
    } else {
        $xml = new SimpleXMLElement("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response/>");
        arrayToXml($data, $xml);
        echo $xml->asXML();
    }
    exit;
}

// Funzione per convertire array in XML
function arrayToXml($data, &$xml) {
    foreach ($data as $key => $value) {
        if (is_array($value)) {
            $subnode = $xml->addChild(is_numeric($key) ? 'item' : $key);
            arrayToXml($value, $subnode);
        } else {
            $xml->addChild($key, htmlspecialchars($value));
        }
    }
}

// Funzioni per ottenere i dati
function getBooks() {
    global $conn;
    $sql = "SELECT * FROM books";
    $result = $conn->query($sql);
    return $result->fetch_all(MYSQLI_ASSOC);
}

function getAuthors() {
    global $conn;
    $sql = "SELECT * FROM authors";
    $result = $conn->query($sql);
    return $result->fetch_all(MYSQLI_ASSOC);
}

function getBooksWithAuthors() {
    global $conn;
    $sql = "SELECT books.title, authors.name FROM books JOIN authors ON books.author_id = authors.author_id";
    $result = $conn->query($sql);
    return $result->fetch_all(MYSQLI_ASSOC);
}

// Gestione delle richieste
$method = $_SERVER['REQUEST_METHOD'];
$action = $_GET['action'] ?? '';
$id = $_GET['id'] ?? null;

switch ($method) {
    case 'GET':
        if ($action === 'getBooks') {
            sendResponse(getBooks(), 200, $format);
        } elseif ($action === 'getAuthors') {
            sendResponse(getAuthors(), 200, $format);
        } elseif ($action === 'getBooksWithAuthors') {
            sendResponse(getBooksWithAuthors(), 200, $format);
        } else {
            sendResponse(['error' => 'Invalid action'], 404, $format);
        }
        break;

    case 'POST':
        if ($action === 'createBook') {
            $input = ($format === 'xml') ? simplexml_load_file("php://input") : json_decode(file_get_contents("php://input"), true);
            $title = $input->title ?? $input['title'];
            $author_id = $input->author_id ?? $input['author_id'];
            $publication_year = $input->publication_year ?? $input['publication_year'];

            if ($title && $author_id) {
                $sql = "INSERT INTO books (title, author_id, publication_year) VALUES (?, ?, ?)";
                $stmt = $conn->prepare($sql);
                $stmt->bind_param("sii", $title, $author_id, $publication_year);
                $stmt->execute();
                sendResponse(['message' => 'Book created', 'id' => $conn->insert_id], 201, $format);
            } else {
                sendResponse(['error' => 'Missing required fields'], 400, $format);
            }
        } else {
            sendResponse(['error' => 'Invalid action'], 404, $format);
        }
        break;

    case 'PUT':
        if ($action === 'updateBook' && $id) {
            $input = ($format === 'xml') ? simplexml_load_file("php://input") : json_decode(file_get_contents("php://input"), true);
            $title = $input->title ?? $input['title'];

            if ($title) {
                $sql = "UPDATE books SET title = ? WHERE id = ?";
                $stmt = $conn->prepare($sql);
                $stmt->bind_param("si", $title, $id);
                $stmt->execute();
                sendResponse(['message' => 'Book updated'], 200, $format);
            } else {
                sendResponse(['error' => 'Missing title'], 400, $format);
            }
        } else {
            sendResponse(['error' => 'Invalid action or missing ID'], 404, $format);
        }
        break;

    case 'DELETE':
        if ($action === 'deleteBook' && $id) {
            $sql = "DELETE FROM books WHERE id = ?";
            $stmt = $conn->prepare($sql);
            $stmt->bind_param("i", $id);
            $stmt->execute();
            if ($stmt->affected_rows > 0) {
                sendResponse(['message' => 'Book deleted'], 200, $format);
            } else {
                sendResponse(['error' => 'Book not found'], 404, $format);
            }
        } else {
            sendResponse(['error' => 'Invalid action or missing ID'], 404, $format);
        }
        break;

    default:
        sendResponse(['error' => 'Method not allowed'], 405, $format);
}
?>