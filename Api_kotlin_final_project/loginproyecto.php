<?php
header("Content-type: application/json; charset=utf-8");

$servername = "localhost";
$username = "id17488564_proyecto_final";
$password = "M%{A|e&$3u^3E|eV";
$dbname = "id17488564_proyectofinal";



$correo=$_GET["correo"];
$contrasena=$_GET["contrasena"];

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error)
{
 die("Conexión fallida: " . $conn->connect_error);
}

$sql = "SELECT * FROM Usuarios where CorreoUsuario='$correo' and ContrasenaUsuario='$contrasena'";
$result = $conn->query($sql);
$registros=array();
$i=0;

if ($result->num_rows > 0) 
 {

  while($row = $result->fetch_assoc()) 
  {

    $registros[$i]=$row;
     $i++;
  }
  echo  '{"usuario":'.json_encode($registros).'}';
  $conn->close();
}
else
echo '{"usuario":[]}';
?>