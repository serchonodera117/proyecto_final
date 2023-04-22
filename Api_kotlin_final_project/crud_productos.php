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

                    //Agregar productos
if($comando == "Agregar")
{
    $idnegocio=$_GET["idnegocio"];
    $correoVendedor=$_GET["correoVendedor"];
    $NombreNegocio=$_GET["NombreNegocio"];
    $NombreProducto=$_GET["NombreProducto"];
    $SeccionProducto=$_GET["SeccionProducto"];
    $ExistenciaProducto=$_GET["ExistenciaProducto"];
    $DescripcionProducto=$_GET["DescripcionProducto"];
    $TipoOferta=$_GET["TipoOferta"];
    $ImagenProducto=$_GET["ImagenProducto"];
    $Precio=$_GET["Precio"];
    $PrecioEnvio=$_GET["PrecioEnvio"];

    $sql = "INSERT INTO Productos(idnegocio, correoVendedor, NombreNegocio, NombreProducto, SeccionProducto,
             ExistenciaProducto, DescripcionProducto, TipoOferta, ImagenProducto, Precio, PrecioEnvio)
             
             VALUES('$idnegocio', '$correoVendedor','$NombreNegocio', '$NombreProducto', '$SeccionProducto',
             '$ExistenciaProducto', '$DescripcionProducto', '$TipoOferta', '$ImagenProducto', '$Precio', '$PrecioEnvio')";
             


    if($conn->query($sql)===true){echo "Producto registrado con éxito";}
    else { echo "Error: " . $sql . "<br>" . $conn->error;}
}

                       //---------------------------------------------------Listar productos
if($comando == "Listar")
{
    $idnegocio=$_GET["idnegocio"];
    $sql = "SELECT * FROM Productos where  idnegocio='$idnegocio'";
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
     $registros=array();
     $i=0;
      while($row = $result->fetch_assoc()) {
        $registros[$i]=$row;
        $i++;
     }
     echo  '{"misProductos":'.json_encode($registros).'}';
    } else {
        echo '{"misProductos":[]}';
     }
}

                    //------------------------------------------------------- Editar productos
if($comando == "Editar")
{
     $idproducto=$_GET["idproducto"];
    $NombreProducto=$_GET["NombreProducto"];
    $SeccionProducto=$_GET["SeccionProducto"];
    $ExistenciaProducto=$_GET["ExistenciaProducto"];
    $DescripcionProducto=$_GET["DescripcionProducto"];
    $TipoOferta=$_GET["TipoOferta"];
    $ImagenProducto=$_GET["ImagenProducto"];
    $Precio=$_GET["Precio"];
    $PrecioEnvio=$_GET["PrecioEnvio"];
    $Visibilidad=$_GET["Visibilidad"];

    $sql = "UPDATE Productos SET  NombreProducto='$NombreProducto',
                                  SeccionProducto='$SeccionProducto',
                                  ExistenciaProducto='$ExistenciaProducto',
                                  DescripcionProducto='$DescripcionProducto',
                                  TipoOferta='$TipoOferta',
                                  ImagenProducto='$ImagenProducto',
                                  Precio='$Precio',
                                  PrecioEnvio='$PrecioEnvio',
                                  Visibilidad='$Visibilidad'
                                  
                                  WHERE idproducto='$idproducto'";
                                  
                                  
        $sql2 = "UPDATE CarritoVentas SET ExistenciaProducto='$ExistenciaProducto'
                               WHERE idproducto='$idproducto'";                              

if( $conn->query($sql)=== TRUE &&  $conn->query($sql2)=== TRUE) {echo "Producto editado con éxito";}
  else {echo "Error: " . $sql . "<br>" . $conn->error;}  
}

              //-------------------------------------------------------------------- Eliminación de producto
if($comando == "Eliminar")
{
  $idproducto=$_GET["idproducto"];

  $sql = "DELETE FROM Productos WHERE idproducto='$idproducto'";
  
  if( $conn->query($sql)=== TRUE) {echo "Producto eliminado con éxito";}
  else {echo "Error: " . $sql . "<br>" . $conn->error;}  
}


$conn->close();
?>