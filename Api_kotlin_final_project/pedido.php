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
                                //------------------------------------- realizar venta
if($comando == "realizarVenta")
{
    $idusuario=$_GET["idusuario"];
    $correoVendedor=$_GET["correoVendedor"];
    $NombreUsuario=$_GET["NombreUsuario"];
    $MunicipioUsuario=$_GET["MunicipioUsuario"];
    $DireccionUsuario=$_GET["DireccionUsuario"];
    $idnegocio=$_GET["idnegocio"];
    $NombreNegocio=$_GET["NombreNegocio"];
    $idproducto=$_GET["idproducto"];
    $NombreProducto=$_GET["NombreProducto"];
    $DescripcionProducto=$_GET["DescripcionProducto"];
    $SeccionProducto=$_GET["SeccionProducto"];
    $ImagenProducto=$_GET["ImagenProducto"];
    $CantidadProducto=$_GET["CantidadProducto"];
    $PrecioFinal=$_GET["PrecioFinal"];
    $TipoPedido=$_GET["TipoPedido"];
    $FechaPedido=$_GET["FechaPedido"]; //---------------------------insertar la info en ambas tablas
    $restar=$_GET["restar"];


    $sql = "INSERT INTO Pedidos (idusuario,	correoVendedor, NombreUsuario, MunicipioUsuario, DireccionUsuario,
            idnegocio, NombreNegocio, idproducto, NombreProducto, DescripcionProducto, SeccionProducto,
            ImagenProducto, CantidadProducto, PrecioFinal, TipoPedido, FechaPedido)

     VALUES('$idusuario', '$correoVendedor','$NombreUsuario', '$MunicipioUsuario', '$DireccionUsuario',
     '$idnegocio', '$NombreNegocio', '$idproducto', '$NombreProducto', '$DescripcionProducto', '$SeccionProducto',
     '$ImagenProducto', '$CantidadProducto', '$PrecioFinal', '$TipoPedido', '$FechaPedido')";

    $sql2 =   "INSERT INTO PedidosCliente (idusuario, correoVendedor, NombreUsuario, MunicipioUsuario, DireccionUsuario,
    idnegocio, NombreNegocio, idproducto, NombreProducto, DescripcionProducto, SeccionProducto,
    ImagenProducto, CantidadProducto, PrecioFinal, TipoPedido, FechaPedido)

VALUES('$idusuario', '$correoVendedor','$NombreUsuario', '$MunicipioUsuario', '$DireccionUsuario',
'$idnegocio', '$NombreNegocio', '$idproducto', '$NombreProducto', '$DescripcionProducto', '$SeccionProducto',
'$ImagenProducto', '$CantidadProducto', '$PrecioFinal', '$TipoPedido', '$FechaPedido')";
    
    
    //--------------------------------------Actualizar las existencias en el carro y lista normal
    
    $sql3 = "UPDATE Productos SET ExistenciaProducto='$restar'
                                  WHERE idproducto='$idproducto'";
                                  
    $sql4 = "UPDATE CarritoVentas SET ExistenciaProducto='$restar'
                                  WHERE idproducto='$idproducto'";

    if($conn->query($sql)===true && $conn->query($sql2)===true && $conn->query($sql3)===true
    &&  $conn->query($sql4)===true){echo "Compra realizada con éxito";}
    else { echo "Error: " . $sql . "<br>" . $conn->error;}
}

if($comando == "listarVentaVendedor")
{
     $correoVendedor=$_GET["correoVendedor"];
     
        $sql = "SELECT * FROM Pedidos WHERE correoVendedor='$correoVendedor'";
        
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"pedidosVendedor":'.json_encode($registros).'}';
    } else {
        echo '{"pedidosVendedor":[]}';
     }
}

                            //--------------------------listar los pedidos del usuario cliente
if($comando == "listarCliente")
{
   $idusuario=$_GET["idusuario"];
     
        $sql = "SELECT * FROM PedidosCliente WHERE idusuario='$idusuario'";
        
  $result = $conn->query($sql);

   if ($result->num_rows > 0) {
   $registros=array();
   $i=0;
     while($row = $result->fetch_assoc()) {
      $registros[$i]=$row;
      $i++;
     }
     echo  '{"pedidoscliente":'.json_encode($registros).'}';
    } else {
        echo '{"pedidoscliente":[]}';
     }
}
                              //---------------editar el estatus 
if($comando == "EditarEstatus")
{
    $idpedido=$_GET["idpedido"];
    $Estatus=$_GET["Estatus"];
    
    $sql = "UPDATE Pedidos SET EstadoProducto='$Estatus',
                               EstadoServicio='$Estatus'
                               
                               WHERE idpedido='$idpedido'";
    
    $sql2 = "UPDATE PedidosCliente SET EstadoProducto='$Estatus',
                               EstadoServicio='$Estatus'
                               
                               WHERE idpedido='$idpedido'";
                               
 if( $conn->query($sql)=== TRUE && $conn->query($sql2)=== TRUE ) {echo "Estatus del pedido actualizado :D";}
  else {echo "Error: " . $sql . "<br>" . $conn->error;}  
}
$conn->close();
?>