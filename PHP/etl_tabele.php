<html>
<head>
<meta charset="UTF-8" />
</head>
<form action='etl_filtr.php' method='post'>

<?php

require "connect.php";

$query="select * from etl_produkty order by id_produktu, model";
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



$query="select * from etl_opinie order by id_opinii, autor_opinii";
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

$query="select * from etl_produkty_opinie order by id_produktu, id_opinii";
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

$query="select * from etl_produkty order by id_produktu, model";
$result=pg_exec($con,$query);

$numRows=pg_numrows($result);
$numCols=pg_numfields($result);

echo '<select name=produkt>';
for ($i=0;$i<$numRows;$i++){
	$e=pg_result($result,$i,0);
	$rodzaj=pg_result($result,$i,1);
	$model=pg_result($result,$i,3);
	echo "<option value=$e /> $e $rodzaj $model";
}
echo '</select>';


?>

<input type="submit" id="przeslij" value="Pokaż opinie produktu"/>
</form>
<body>
</body>
</html>
