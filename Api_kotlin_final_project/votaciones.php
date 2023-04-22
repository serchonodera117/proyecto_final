<?php
header("Content-type: application/json; charset=utf-8");

$servername = "localhost";
$username = "id17488564_proyecto_final";
$password = "M%{A|e&$3u^3E|eV";
$dbname = "id17488564_proyectofinal";

$comando=$_GET["comando"];

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error){
 die("Conexión fallida: " . $conn->connect_error);
}
                           
                                      //----------------------------votar
if($comando == "votar"){
    $idnegocio=$_GET["idnegocio"];
    $idproducto=$_GET["idproducto"];
    $correoPuntuador=$_GET["correoPuntuador"];
    $Calificacion=$_GET["Calificacion"];

    $sql = "INSERT INTO Puntuaciones(idnegocio, idproducto,
                           correoPuntuador, Calificacion)
                           
                           Values ('$idnegocio','$idproducto',
                                   '$correoPuntuador','$Calificacion')";

    if($conn->query($sql)===true){echo "Puntuado";}
    else { echo '{"error: "' . $sql . ' ' . $conn->error.'"}';}
}

                           //---------------------------------- recuperar la votación dada
if($comando == "listarVotacion"){
    $correo=$_GET["correo"];
    $idproducto=$_GET["idproducto"];
  $sql = "SELECT * FROM Puntuaciones WHERE correoPuntuador='$correo' AND idproducto='$idproducto'";
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
        $registros=array();
        $i=0;
         while($row = $result->fetch_assoc()) {
          $registros[$i]=$row;
          $i++;
        }
     echo  '{"miPuntuacion":'.json_encode($registros).'}';
    } else {
        echo '{"miPuntuacion":[]}';
     }
}
                              //--------------------------- edicion de las puntuaciones
if($comando == "EditarPuntuacion")
{
    $idpuntuacion=$_GET["idpuntuacion"];
    $idnegocio=$_GET["idnegocio"];
    $idproducto=$_GET["idproducto"];
    $correo=$_GET["correo"];
    $Calificacion=$_GET["Calificacion"];

    $sql = "UPDATE Puntuaciones SET  Calificacion='$Calificacion'
                 WHERE correoPuntuador='$correo' AND idproducto='$idproducto'";
                 
    if($conn->query($sql)===true){echo "Editado $Calificacion";}
    else { echo '{"error: "' . $sql . ' ' . $conn->error.'"}';}
}

                           //----------------------- promedio de votacion productos
if($comando == "promedioProducto"){
    $correo=$_GET["correo"];
    $idproducto=$_GET["idproducto"];
    
    $sql = "SELECT Calificacion as promedio FROM Puntuaciones WHERE  idproducto='$idproducto'";
    $result = $conn->query($sql);
   
   if ($result->num_rows > 0) {
          $registros=array();
          $i=0;
           while($row = $result->fetch_assoc()) {
              $registros[$i]=$row;
              $i++;
             }
     echo  '{"promedioP":'.json_encode($registros).'}';
    } else {
        echo '{"promedioP":[]}';
     }
}

$conn->close();
?>