<?php 
require_once 'connect.php';
$nama_ruangan = $_POST["nama_ruangan"];
$tanggal_pinjam = $_POST["tanggal_pinjam"];
$status_pinjam = $_POST["status_pinjam"];
$waktu_awal = $_POST["waktu_awal"];
$waktu_akhir = $_POST["waktu_akhir"];
// $mysql_qry = "select jamMulai and jamAkhir from kelas where nim like '$nim' and password like '$password';";
$mysql_qry = "select nama_ruangan , tanggal_pinjam ,status_pinjam, waktu_awal ,waktu_akhir from peminjaman_ruangan 
where nama_ruangan like '$nama_ruangan' and tanggal_pinjam like '$tanggal_pinjam' 
and status_pinjam like '$status_pinjam' and (('$waktu_awal' BETWEEN waktu_awal and waktu_akhir) OR ('$waktu_akhir' BETWEEN waktu_awal and waktu_akhir) 
OR ('$waktu_awal' <= waktu_awal and '$waktu_akhir' >= waktu_akhir))";

$result = mysqli_query($connect ,$mysql_qry);
    if(mysqli_num_rows($result) > 0) {
        echo "gabisa";
    }
    else {
      echo "bisa";
    }
?>