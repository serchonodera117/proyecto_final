<?php
header("Content-type: application/json; charset=utf-8");

$servername = "localhost";
$username = "id17488564_proyecto_final";
$password = "M%{A|e&$3u^3E|eV";
$dbname = "id17488564_proyectofinal";


$comando=$_GET["comando"];


$conn =  new mysqli($servername, $username, $password, $dbname);
  if($conn->connect_error)
  {
      die("Connection failed: " . $conn->connect_error);
  }
  
  
if($comando=="cancelarcompra"){
    $idpedido=$_GET["idpedido"];
    $idproducto=$_GET["idproducto"];
    $CantidadCancelada=$_GET["CantidadCancelada"];
    $NombreNegocio=$_GET["NombreNegocio"];
    $ImagenProducto=$_GET["ImagenProducto"];
    $correo=$_GET["correo"];
    
    $sql = "UPDATE Productos Set ExistenciaProducto=ExistenciaProducto+'$CantidadCancelada'
                            WHERE idproducto='$idproducto'";
                            
    $sql2 = "UPDATE CarritoVentas Set ExistenciaProducto=ExistenciaProducto+'$CantidadCancelada'
                            WHERE idproducto='$idproducto'";   
                            
    $sql3 = "UPDATE Pedidos Set ImagenProducto='$ImagenProducto',
                                NombreNegocio='$NombreNegocio'
                            WHERE idpedido='$idpedido'";
    
    $sql4 = "UPDATE PedidosCliente Set ImagenProducto='$ImagenProducto',
                                NombreNegocio='$NombreNegocio'
                            WHERE idpedido='$idpedido'";
                            
if($conn->query($sql)=== TRUE && $conn->query($sql2)=== TRUE &&
   $conn->query($sql3)=== TRUE && $conn->query($sql4)=== TRUE){echo "Pedido cancelado";}
  else {echo '{"error: "' . $sql . ' ' . $conn->error.'"}';}    
}  

$conn->close();
?>  