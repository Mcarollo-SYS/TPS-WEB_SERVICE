<?php
$conn = new mysqli("localhost", "root", "", "dealership");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>