<?php 
$sql = "SELECT * FROM daftar_waktu";
require_once('dbConnect.php');
$r = mysqli_query($con,$sql);
$resultJam = array();
while($row = mysqli_fetch_array($r)){
    array_push($resultJam,array('list_waktu'=>$row['list_waktu']));
}
echo json_encode(array('resultJam'=>$resultJam));
mysqli_close($con);?>