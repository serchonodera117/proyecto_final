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

if($comando == "agregarAlCarro")
{
    $correoCliente=$_GET["correoCliente"];
    $idproducto=$_GET["idproducto"];
    $CantidadSolicitada=$_GET["CantidadSolicitada"];
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

    $sql = "INSERT INTO CarritoVentas (correoCliente, idproducto, CantidadSolicitada,
                                       	idnegocio, correoVendedor, NombreNegocio,
                                           NombreProducto, SeccionProducto, ExistenciaProducto,
                                           DescripcionProducto,  TipoOferta, ImagenProducto, 
                                           Precio, 	PrecioEnvio)
                        
                    VALUES('$correoCliente', '$idproducto', '$CantidadSolicitada',
                      '$idnegocio', '$correoVendedor', '$NombreNegocio',
                      '$NombreProducto', '$SeccionProducto', '$ExistenciaProducto',
                      '$DescripcionProducto', '$TipoOferta', '$ImagenProducto',
                      '$Precio', '$PrecioEnvio')";
            
           if($conn->query($sql) === TRUE) {echo "$NombreProducto se ha agregado a su carrito";}
           else{echo "Error: " . $sql . "<br>" . $conn->error;}
}

                      //------------------------------------- Listar los elementos del carrito
if($comando == "ElementosCarrito")
{
    $correo=$_GET["correo"];
  $sql = "SELECT * FROM CarritoVentas WHERE correoCliente='$correo'";
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"miCarrito":'.json_encode($registros).'}';
    } else {
       echo '{"miCarrito":[]}';
     }
}
                                            //------------------------------------ editar la cantidad
if($comando == "EditarCantidadSolicitada")
{
    $idproductocarrito=$_GET["idproductocarrito"];
    $CantidadSolicitada=$_GET["CantidadSolicitada"];
    $precio=$_GET["precio"];
    
    $sql = "UPDATE CarritoVentas SET 	CantidadSolicitada='$CantidadSolicitada',
                                        Precio='$precio'
                  WHERE idproductocarrito='$idproductocarrito'";
                  
    if($conn->query($sql) === TRUE) {echo "editado";}
           else{echo "Error: " . $sql . "<br>" . $conn->error;}
}

                         //------------------------------------- Borrar un solo elemento del carrito 
if($comando == "BorrarElemento")
{
      $idproductocarrito=$_GET["idproductocarrito"];
      $nombre=$_GET["nombre"];
      
      $sql = "DELETE FROM CarritoVentas WHERE idproductocarrito='$idproductocarrito'";
    
    if($conn->query($sql) === TRUE) {echo "'$nombre' eliminado de su carrito";}
     else{echo "Error: " . $sql . "<br>" . $conn->error;}
}

                 //------------------------------------------------- Borrar toda el perro carrito 
if($comando == "vaciarCarrito")
{

       $correo=$_GET["correo"];
      $sql = "DELETE FROM CarritoVentas WHERE correoCliente='$correo'";
    
    if($conn->query($sql) === TRUE) {echo "Carrito vaciado con écito";}
     else{echo "Error: " . $sql . "<br>" . $conn->error;}
}
$conn->close();
?>