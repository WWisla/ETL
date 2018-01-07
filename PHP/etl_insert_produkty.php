<?php

require "connect.php";

$id_produktu = $_POST["id_produktu"];
$rodzaj = $_POST["rodzaj"];
$marka = $_POST["marka"];
$model = $_POST["model"];
$uwagi = $_POST["uwagi"];


$query = "insert into etl_produkty (id_produktu, rodzaj_produktu, marka, model, dodatkowe_uwagi) values ('$id_produktu','$rodzaj','$marka','$model','$uwagi');";

$result = pg_exec($con, $query);

if($result){
echo "Brawo :)";
}
else{
echo "Eh znowu :(";
}

?>
