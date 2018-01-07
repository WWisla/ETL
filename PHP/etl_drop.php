<?php

require "connect.php";

$query = "drop table if exists etl_produkty;
drop table if exists etl_opinie;
drop table if exists etl_produkty_opinie;

create table if not exists etl_produkty(
id_produktu int4 primary key,
rodzaj_produktu varchar(255),
marka varchar(255),
model varchar(255),
dodatkowe_uwagi varchar(255));

create table if not exists etl_opinie(
id_opinii int4 primary key,
autor_opinii varchar(255),
data_wystawienia date,
godzina_wystawienia time,
ocena float,
rekomendacja varchar(255),
tresc_opinii text,
zalety_produktu text,
wady_produktu text,
przydatna_opinia int4,
nieprzydatna_opinia int4);

create table if not exists etl_produkty_opinie(
id_produktu int4,
id_opinii int4,
primary key(id_produktu, id_opinii));";

$result = pg_exec($con, $query);


if($result){
echo "Baza danych pusta :)";
}
else{
echo "Eh znowu :(";
}

?>
