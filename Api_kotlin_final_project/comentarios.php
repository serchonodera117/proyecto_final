<?php
header("Content-type: application/json; charset=utf-8");

$servername = "localhost";
$username = "id17488564_proyecto_final";
$password = "M%{A|e&$3u^3E|eV";
$dbname = "id17488564_proyectofinal";

$comando=$_GET["comando"];

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error)
{
 die("Conexión fallida: " . $conn->connect_error);
}

                  //-------------------------------------------Guardar comentario
if($comando=="comentar"){
    $idproducto=$_GET["idproducto"];
    $TipoComentario=$_GET["TipoComentario"];
    $NombreUsuario=$_GET["NombreUsuario"];
    $ImagenUsuario=$_GET["ImagenUsuario"];
    $Contenido=$_GET["Contenido"];
    $FechaComentario=$_GET["FechaComentario"];

    $sql = "INSERT INTO ComentarioRespuesta(idproducto, TipoComentario, NombreUsuario, 	
           ImagenUsuario, Contenido, FechaComentario)
            
            VALUES('$idproducto','$TipoComentario','$NombreUsuario',
                   '$ImagenUsuario','$Contenido','$FechaComentario')";

   if($conn->query($sql)===TRUE){echo "Comentado";}
   else {echo '{"error: "' . $sql . ' ' . $conn->error.'"}';}                 
}
                     //------------------------------------------Listar comentarios del producto 
if($comando == "ListarComentarios")
{
    $idproducto=$_GET["idproducto"];
     $sql = "SELECT * FROM ComentarioRespuesta 
                  WHERE idproducto='$idproducto'";
  $result = $conn->query($sql);
   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"comentarios":'.json_encode($registros).'}';
    } else {
        echo '{"comentarios":[]}';
     }
}

                  //-------------------------------------------Guardar respuesta
if($comando=="responder"){
    $idcomentariorespuesta=$_GET["idcomentariorespuesta"];
    $NombreUsuarioRespuesta=$_GET["NombreUsuarioRespuesta"];
    
    $idproducto=$_GET["idproducto"];
    $TipoComentario=$_GET["TipoComentario"];
    $NombreUsuario=$_GET["NombreUsuario"];
    $ImagenUsuario=$_GET["ImagenUsuario"];
    $Contenido=$_GET["Contenido"];
    $FechaComentario=$_GET["FechaComentario"];

    $sql = "INSERT INTO ComentarioRespuesta(idcomentariorespuesta, idproducto, TipoComentario, NombreUsuario, 
        NombreUsuarioRespuesta, ImagenUsuario, Contenido, FechaComentario)
            
            VALUES('$idcomentariorespuesta','$idproducto','$TipoComentario','$NombreUsuario',
              '$NombreUsuarioRespuesta', '$ImagenUsuario','$Contenido','$FechaComentario')";

   if($conn->query($sql)===TRUE){echo "Respondido";}
   else {echo '{"error: "' . $sql . ' ' . $conn->error.'"}';}                 
}
                     //------------------------------------------Listar respuestas del comentario 
if($comando == "ListarRespuestas")
{
    $idcomentarioResponder=$_GET["idcomentarioResponder"];
     $sql = "SELECT * FROM ComentarioRespuesta 
                  WHERE idcomentariorespuesta='$idcomentarioResponder'"; //donde la idcomentatio = id respuesta
                  
  $result = $conn->query($sql);
   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"respuestas":'.json_encode($registros).'}';
    } else {
        echo '{"respuestas":[]}';
     }
}
$conn->close();
?>