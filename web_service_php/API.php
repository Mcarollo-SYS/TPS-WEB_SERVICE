<?php
include 'DB.php';

// Impostazione dell'header per la risposta XML
header("Content-Type: application/xml; charset=utf-8");

// Funzione per generare una risposta in XML
function generateXML($data, $rootElement) {
    $xml = new SimpleXMLElement("<$rootElement/>");
    foreach ($data as $row) {
        $item = $xml->addChild('item');
        foreach ($row as $key => $value) {
            $item->addChild($key, htmlspecialchars($value));
        }
    }
    return $xml->asXML();
}

// Funzione per ottenere i dati dei libri
function getBooks() {
    global $conn;
    $sql = "SELECT * FROM books";
    $result = $conn->query($sql);
    
    $books = [];
    while ($row = $result->fetch_assoc()) {
        $books[] = $row;
    }
    
    return generateXML($books, 'books');
}

// Funzione per ottenere i dati degli autori
function getAuthors() {
    global $conn;
    $sql = "SELECT * FROM authors";
    $result = $conn->query($sql);
    
    $authors = [];
    while ($row = $result->fetch_assoc()) {
        $authors[] = $row;
    }
    
    return generateXML($authors, 'authors');
}

// Funzione per ottenere i libri con i rispettivi autori (JOIN)
function getBooksWithAuthors() {
    global $conn;
    $sql = "SELECT books.title, authors.name FROM books JOIN authors ON books.author_id = authors.author_id";
    $result = $conn->query($sql);
    
    $booksWithAuthors = [];
    while ($row = $result->fetch_assoc()) {
        $booksWithAuthors[] = $row;
    }
    
    return generateXML($booksWithAuthors, 'booksWithAuthors');
}

// Gestione delle richieste
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (isset($_GET['action'])) {
        $action = $_GET['action'];
        
        switch ($action) {
            case 'getBooks':
                echo getBooks();
                break;
            case 'getAuthors':
                echo getAuthors();
                break;
            case 'getBooksWithAuthors':
                echo getBooksWithAuthors();
                break;
            default:
                echo "<error>Invalid action</error>";
                break;
        }
    } else {
        echo "<error>Action not specified</error>";
    }
} else {
    echo "<error>Invalid HTTP method. Only GET is allowed.</error>";
}
?>