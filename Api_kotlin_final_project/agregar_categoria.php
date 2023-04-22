<?php
$servername = "localhost";
$username = "id17488564_proyecto_final";
$password = "M%{A|e&$3u^3E|eV";
$dbname = "id17488564_proyectofinal";

$codigo=$_GET["codigo"];
$NombreCategoria=$_GET["NombreCategoria"];
$ImagenCategoria=$_GET["ImagenCategoria"];

$conn =  new mysqli($servername, $username, $password, $dbname);
  if($conn->connect_error)
  {
      die("Connection failed: " . $conn->connect_error);
  }
  
  
if($codigo == "Agregar")
{  
  $sql = "INSERT INTO CategoriaNegocio (NombreCategoria, ImagenCategoria)
         VALUES ('$NombreCategoria', '$ImagenCategoria')";



  if( $conn->query($sql) === TRUE){echo "Nueva categoría añadida";}
    else {echo "Error: " . $sql . "<br>" . $conn->error;}    
}

if($codigo == "Listar")
{
   $sql = "SELECT * FROM CategoriaNegocio";
   $result = $conn->query($sql);

   if ($result->num_rows > 0) {
       $registros=array();
       $i=0;
       while($row = $result->fetch_assoc()) {
         //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
       $registros[$i]=$row;
       $i++;
      }

    echo '{"categorias":'.json_encode($registros).'}';
    } else {
     echo "0 resultados";
    }
}

$conn->close();
    ?>