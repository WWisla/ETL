<html>
<head>
<meta charset="UTF-8" />
</head>
<form action='etl_tabele.php' method='post'>

<?php

$product = $_POST["produkt"];

require "connect.php";

$query="select * from etl_produkty where id_produktu='$product' order by id_produktu, model;";
$result=pg_exec($con,$query);

$numRows=pg_numrows($result);
$numCols=pg_numfields($result);

echo '<h3>etl_produkty</h3>';

echo '<table border="1" frame="border" rules="all">';
for ($i=0;$i<$numCols;$i++){
	$e=pg_field_name($result,$i);
	echo "<th>$e";
}
for ($i=0;$i<$numRows;$i++){
	echo '<tr>';
	for ($j=0;$j<$numCols;$j++){
		$e=pg_result($result,$i,$j);
	echo "<td>$e</td>";
	}
}
echo '</table>';



$query="select * from etl_opinie left join etl_produkty_opinie on
(etl_opinie.id_opinii=etl_produkty_opinie.id_opinii) where id_produktu='$product' order by etl_opinie.id_opinii;";
$result=pg_exec($con,$query);

$numRows=pg_numrows($result);
$numCols=pg_numfields($result);

echo '<h3>etl_opinie</h3>';

echo '<table border="1" frame="border" rules="all">';
for ($i=0;$i<$numCols;$i++){
	$e=pg_field_name($result,$i);
	echo "<th>$e";
}
for ($i=0;$i<$numRows;$i++){
	echo '<tr>';
	for ($j=0;$j<$numCols;$j++){
		$e=pg_result($result,$i,$j);
	echo "<td>$e</td>";
	}
}
echo '</table>';



$query="select * from etl_produkty_opinie where id_produktu='$product' order by id_opinii;";
$result=pg_exec($con,$query);

$numRows=pg_numrows($result);
$numCols=pg_numfields($result);

echo '<h3>etl_podukty_opinie</h3>';

echo '<table border="1" frame="border" rules="all">';
for ($i=0;$i<$numCols;$i++){
	$e=pg_field_name($result,$i);
	echo "<th>$e";
}
for ($i=0;$i<$numRows;$i++){
	echo '<tr>';
	for ($j=0;$j<$numCols;$j++){
		$e=pg_result($result,$i,$j);
	echo "<td>$e</td>";
	}
}
echo '</table><br>';

?>

<input type="submit" id="przeslij" value="Powrót"/>
</form>
<body>
</body>
</html>
