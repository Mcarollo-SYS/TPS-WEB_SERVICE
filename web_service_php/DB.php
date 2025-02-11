<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "library"; // Assicurati che il nome del tuo database sia corretto

// Crea la connessione
$conn = new mysqli($servername, $username, $password, $dbname);

// Verifica la connessione
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>