<?php
header("Content-type: application/json; charset=utf-8");

$servername = "localhost";
$username = "id17488564_proyecto_final";
$password = "M%{A|e&$3u^3E|eV";
$dbname = "id17488564_proyectofinal";


$comando=$_GET["comando"];
$correo=$_GET["correo"];

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error)
{
 die("Conexión fallida: " . $conn->connect_error);
}

                      //------------------------------------- Listar los negocios del perfil
if($comando == "NegociosPerfil")
{
  $sql = "SELECT * FROM Negocios WHERE CorreoUsuario='$correo'";
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"misNegocios":'.json_encode($registros).'}';
    } else {
        echo "0 resultados";
     }
}

if($comando == "NegociosHome")
{
  $sql = "SELECT * FROM Negocios";
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"miHome":'.json_encode($registros).'}';
    } else {
        echo '{"miHome":[]}';
     }
}

                    //---------------------------------------------------Listar todos los productos
if($comando == "ListarTodo")
{
     $sql = "SELECT * FROM Productos";
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"todasOfertas":'.json_encode($registros).'}';
    } else {
        echo '{"todasOfertas":[]}';
     }
}


$conn->close();
?>