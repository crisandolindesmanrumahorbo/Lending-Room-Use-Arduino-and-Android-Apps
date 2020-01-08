<?php
 
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
	}
	

	
	/*
	* The create operation
	* When this method is called a new record is created in the database
	*/
	function createKelas($nama_ruangan, $tanggal_pinjam, $waktu_awal, $waktu_akhir,$nim_mahasiswa,$status_pinjam,$keterangan){
		$stmt = $this->con->prepare("INSERT INTO peminjaman_ruangan (nama_ruangan, tanggal_pinjam, waktu_awal, waktu_akhir,nim_mahasiswa,status_pinjam,keterangan) VALUES (?, ?, ?, ?,?, ?,?)");
		$stmt->bind_param("sssssss", $nama_ruangan, $tanggal_pinjam, $waktu_awal, $waktu_akhir,$nim_mahasiswa,$status_pinjam,$keterangan);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The read operation
	* When this method is called it is returning all the existing record of the database
	*/
	function getKelas($nim_mahasiswa, $status_pinjam){
		$stmt = $this->con->prepare("SELECT id_peminjaman, nama_ruangan, tanggal_pinjam, waktu_awal, waktu_akhir,nim_mahasiswa,status_pinjam,keterangan FROM peminjaman_ruangan WHERE nim_mahasiswa= ? and status_pinjam = ? ");
		$stmt->bind_param("ss", $nim_mahasiswa, $status_pinjam);
		$stmt->execute();
		$stmt->bind_result($id_peminjaman, $nama_ruangan, $tanggal_pinjam, $waktu_awal, $waktu_akhir,$nim_mahasiswa,$status_pinjam,$keterangan);
		
		$kelas = array(); 
		
		while($stmt->fetch()){
			$kela  = array();
			$kela['id_peminjaman'] = $id_peminjaman; 
			$kela['nama_ruangan'] = $nama_ruangan; 
			$kela['tanggal_pinjam'] = $tanggal_pinjam; 
			$kela['waktu_awal'] = $waktu_awal; 
			$kela['waktu_akhir'] = $waktu_akhir; 
			$kela['nim_mahasiswa'] = $nim_mahasiswa;
			$kela['status_pinjam'] = $status_pinjam;
			$kela['keterangan'] = $keterangan;
			
			array_push($kelas, $kela); 
		}
		
		return $kelas; 
	}


	//ALL PINJAM
	function getKelasPinjam($status_pinjam){
		$stmt = $this->con->prepare("SELECT id_peminjaman, nama_ruangan, tanggal_pinjam, waktu_awal, waktu_akhir,nim_mahasiswa,status_pinjam,keterangan FROM peminjaman_ruangan WHERE status_pinjam = ? ");
		$stmt->bind_param("s", $status_pinjam);
		$stmt->execute();
		$stmt->bind_result($id_peminjaman, $nama_ruangan, $tanggal_pinjam, $waktu_awal, $waktu_akhir,$nim_mahasiswa,$status_pinjam,$keterangan);
		
		$kelas = array(); 
		
		while($stmt->fetch()){
			$kela  = array();
			$kela['id_peminjaman'] = $id_peminjaman; 
			$kela['nama_ruangan'] = $nama_ruangan; 
			$kela['tanggal_pinjam'] = $tanggal_pinjam; 
			$kela['waktu_awal'] = $waktu_awal; 
			$kela['waktu_akhir'] = $waktu_akhir; 
			$kela['nim_mahasiswa'] = $nim_mahasiswa;
			$kela['status_pinjam'] = $status_pinjam;
			$kela['keterangan'] = $keterangan;
			
			array_push($kelas, $kela); 
		}
		
		return $kelas; 
	}
	
	/*
	* The update operation
	* When this method is called the record with the given id is updated with the new given values
	*/
	function updateKelas($id_peminjaman, $nama_ruangan, $tanggal_pinjam, $waktu_awal, $waktu_akhir, $nim_mahasiswa, $status_pinjam, $keterangan){
		$stmt = $this->con->prepare("UPDATE peminjaman_ruangan SET nama_ruangan = ?, tanggal_pinjam = ?, waktu_awal = ?, waktu_akhir = ? , nim_mahasiswa =? , status_pinjam = ? , keterangan = ? WHERE id_peminjaman = ? ");
		$stmt->bind_param("sssssssi", $nama_ruangan, $tanggal_pinjam, $waktu_awal, $waktu_akhir,$nim_mahasiswa,$status_pinjam,$keterangan ,$id_peminjaman);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	/*
	* The delete operation
	* When this method is called record is deleted for the given id 
	*/
	function deleteKelas($id_peminjaman,$nim_mahasiswa,$status_pinjam){
		$stmt = $this->con->prepare("DELETE FROM peminjaman_ruangan WHERE id_peminjaman = ? and nim_mahasiswa = ? and status_pinjam = ? ");
		$stmt->bind_param("iss", $id_peminjaman,$nim_mahasiswa,$status_pinjam);
		if($stmt->execute())
			return true; 
		
		return false; 
	}

}
?>