<?php
$servername = "localhost";
$username = "id17488564_proyecto_final";
$password = "M%{A|e&$3u^3E|eV";
$dbname = "id17488564_proyectofinal";


$NombreUsuario=$_GET["NombreUsuario"];
$FotoUsuario=$_GET["FotoUsuario"]; 
$MunicipioUsuario=$_GET["MunicipioUsuario"]; 
$ColoniaUsuario=$_GET["ColoniaUsuario"];
$DomicilioUsuario=$_GET["DomicilioUsuario"];
$CorreoUsuario=$_GET["CorreoUsuario"];
$ContrasenaUsuario=$_GET["ContrasenaUsuario"]; 

$TipoUsuario=$_GET["TipoUsuario"];
$TelefonoUsuario=$_GET["TelefonoUsuario"];
$VerificacionUsuario=$_GET["VerificacionUsuario"];



$conn =  new mysqli($servername, $username, $password, $dbname);
  if($conn->connect_error)
  {
      die("Connection failed: " . $conn->connect_error);
  }

if($TipoUsuario == 'Cliente')
{
  $sql = "INSERT INTO Usuarios (NombreUsuario, FotoUsuario,  MunicipioUsuario,
                               ColoniaUsuario, DomicilioUsuario, CorreoUsuario, 
                               ContrasenaUsuario, TelefonoUsuario, 
                               TipoDeUsuario, VerificacionUsuario)

  VALUES ('$NombreUsuario', '$FotoUsuario', '$MunicipioUsuario', 
          '$ColoniaUsuario', '$DomicilioUsuario', '$CorreoUsuario', 
          '$ContrasenaUsuario', '$TelefonoUsuario',
          '$TipoUsuario', '$VerificacionUsuario')";
          
          
  if( $conn->query($sql)=== TRUE)
           {echo "Nuevo usuario registrado";}
    else {echo "Error: " . $sql . "<br>" . $conn->error;}
}


if($TipoUsuario == "Vendedor")
{ //variables del negocio

$NombreNegocio=$_GET["NombreNegocio"];
$LogoNegocio=$_GET["LogoNegocio"];
$Efectivo=$_GET["Efectivo"];
$Tarjeta=$_GET["Tarjeta"];
$PagoOxxo=$_GET["PagoOxxo"];
$OfertaProducto=$_GET["OfertaProducto"];
$OfertaServicio=$_GET["OfertaServicio"];
$CategoriaNegocio=$_GET["CategoriaNegocio"];
$MunicipioNegocio=$_GET["MunicipioNegocio"];
$ColoniaNegocio=$_GET["ColoniaNegocio"];
$DireccionNegocio=$_GET["DireccionNegocio"];

                                                                      //registro del vendedor
$sql = "INSERT INTO Usuarios (NombreUsuario, FotoUsuario,  MunicipioUsuario,
ColoniaUsuario, DomicilioUsuario, CorreoUsuario, 
ContrasenaUsuario, TelefonoUsuario, 
TipoDeUsuario, VerificacionUsuario)

VALUES ('$NombreUsuario', '$FotoUsuario', '$MunicipioUsuario', 
'$ColoniaUsuario', '$DomicilioUsuario', '$CorreoUsuario', 
'$ContrasenaUsuario', '$TelefonoUsuario',
'$TipoUsuario', '$VerificacionUsuario')";
   

                                                                         //registro del negocio del vendedor
 $sql2 = "INSERT INTO Negocios (CorreoUsuario, NombreNegocio, LogoNegocio,
                              Efectivo, Tarjeta, PagoOxxo, Oferta_producto,
                              Oferta_servicio, CategoriaNegocio, MunicipioNegocio, 
                              ColoniaNegocio, DireccionNegocio)  

            VALUES ('$CorreoUsuario', '$NombreNegocio', '$LogoNegocio',
                     '$Efectivo', '$Tarjeta', '$PagoOxxo', '$OfertaProducto',
                     '$OfertaServicio', '$CategoriaNegocio', '$MunicipioNegocio', 
                    '$ColoniaNegocio', '$DireccionNegocio')";

  if($conn->query($sql) === TRUE  && $conn->query($sql2) === TRUE)
           {
                echo "Usuario registrado con exito";
            }
    else {echo "Error: " . $sql . "<br>" . $conn->error;}    
}

if($TipoUsuario == "Negocio")
{
    $NombreNegocio=$_GET["NombreNegocio"];
$LogoNegocio=$_GET["LogoNegocio"];
$Efectivo=$_GET["Efectivo"];
$Tarjeta=$_GET["Tarjeta"];
$PagoOxxo=$_GET["PagoOxxo"];
$OfertaProducto=$_GET["OfertaProducto"];
$OfertaServicio=$_GET["OfertaServicio"];
$CategoriaNegocio=$_GET["CategoriaNegocio"];
$MunicipioNegocio=$_GET["MunicipioNegocio"];
$ColoniaNegocio=$_GET["ColoniaNegocio"];
$DireccionNegocio=$_GET["DireccionNegocio"];

   

                                                                         //registro del negocio del vendedor
 $sql2 = "INSERT INTO Negocios (CorreoUsuario, NombreNegocio, LogoNegocio,
                              Efectivo, Tarjeta, PagoOxxo, Oferta_producto,
                              Oferta_servicio, CategoriaNegocio, MunicipioNegocio, 
                              ColoniaNegocio, DireccionNegocio)  

            VALUES ('$CorreoUsuario', '$NombreNegocio', '$LogoNegocio',
                     '$Efectivo', '$Tarjeta', '$PagoOxxo', '$OfertaProducto',
                     '$OfertaServicio', '$CategoriaNegocio', '$MunicipioNegocio', 
                    '$ColoniaNegocio', '$DireccionNegocio')";

  if($conn->query($sql2) === TRUE)
           {
                echo "Negocio registrado con exito";
            }
    else {echo "Error: " . $sql . "<br>" . $conn->error;}    
}
$conn->close();
    ?>