<?php 
require_once 'connect.php';
$nama_ruangan = $_POST["nama_ruangan"];
$tanggal_pinjam = $_POST["tanggal_pinjam"];
$status_pinjam = $_POST["status_pinjam"];
$waktu_awal = $_POST["waktu_awal"];
$waktu_akhir = $_POST["waktu_akhir"];
$nim_mahasiswa = $_POST["nim_mahasiswa"];
$id_peminjaman = $_POST["id_peminjaman"];

$mysql_qry = "select nama_ruangan , tanggal_pinjam ,status_pinjam, waktu_awal ,waktu_akhir from peminjaman_ruangan 
where nama_ruangan like '$nama_ruangan' and tanggal_pinjam like '$tanggal_pinjam' and status_pinjam like '$status_pinjam' 
and (('$waktu_awal' BETWEEN waktu_awal and waktu_akhir) OR ('$waktu_akhir' BETWEEN waktu_awal and waktu_akhir) 
OR ('$waktu_awal' <= waktu_awal and '$waktu_akhir' >= waktu_akhir))";

$mysql_qry1 = "select nama_ruangan , tanggal_pinjam ,status_pinjam, waktu_awal ,waktu_akhir,nim_mahasiswa,id_peminjaman from peminjaman_ruangan 
where nama_ruangan like '$nama_ruangan' and tanggal_pinjam like '$tanggal_pinjam' and status_pinjam like '$status_pinjam' and nim_mahasiswa like '$nim_mahasiswa' 
and id_peminjaman like '$id_peminjaman' and (('$waktu_awal' BETWEEN waktu_awal and waktu_akhir) OR ('$waktu_akhir' BETWEEN waktu_awal and waktu_akhir) 
OR ('$waktu_awal' <= waktu_awal and '$waktu_akhir' >= waktu_akhir))";


$result = mysqli_query($connect ,$mysql_qry);
$result1 = mysqli_query($connect , $mysql_qry1);

    if((mysqli_num_rows($result) == 1 && mysqli_num_rows($result1) == 1) || (mysqli_num_rows($result) == 0 && mysqli_num_rows($result1) == 0)) {
        echo "bisa";
    }
    else {
      echo "gabisa";
    }
?>