<?php

require "connect.php";

$id_produktu = $_POST["id_produktu"];
$id_opinii = $_POST["id_opinii"];


$query = "insert into etl_produkty_opinie (id_produktu, id_opinii) values ('$id_produktu','$id_opinii');";

$result = pg_exec($con, $query);

if($result){
echo "Brawo :)";
}
else{
echo "Eh znowu :(";
}

?>
