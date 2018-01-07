<?php

require "connect.php";

$id_opinii = $_POST["id_opinii"];
$autor = $_POST["autor"];
$data = $_POST["data"];
$godzina = $_POST["godzina"];
$ocena = $_POST["ocena"];
$rekomendacja = $_POST["rekomendacja"];
$tresc = $_POST["tresc"];
$zalety = $_POST["zalety"];
$wady = $_POST["wady"];
$przydatna = $_POST["przydatna"];
$nieprzydatna = $_POST["nieprzydatna"];


$query = "insert into etl_opinie (
id_opinii, autor_opinii, data_wystawienia, godzina_wystawienia,
ocena, rekomendacja, tresc_opinii, zalety_produktu, 
wady_produktu, przydatna_opinia, nieprzydatna_opinia) 
values ('$id_opinii','$autor','$data','$godzina','$ocena',
'$rekomendacja','$tresc','$zalety',
'$wady','$przydatna','$nieprzydatna');";

$result = pg_exec($con, $query);

if($result){
echo "Brawo :)";
}
else{
echo "Eh znowu :(";
}

?>
