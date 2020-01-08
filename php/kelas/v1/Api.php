<?php 

	//getting the dboperation class
	require_once '../include/DbOperation.php';

	//function validating all the paramters are available
	//we will pass the required parameters to this function 
	function isTheseParametersAvailable($params){
		//assuming all parameters are available 
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		//if parameters are missing 
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)).' missing';
			
			//displaying error
			echo json_encode($response);
			
			//stopping further execution
			die();
		}
	}
	
	//an array to display response
	$response = array();
	
	//if it is an api call 
	//that means a get parameter named api call is set in the URL 
	//and with this parameter we are concluding that it is an api call
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			//the CREATE operation
			//if the api call value is 'createhero'
			//we will create a record in the database
			case 'createkelas':
				//first check the parameters required for this request are available or not 
				isTheseParametersAvailable(array('nama_ruangan','tanggal_pinjam','waktu_awal','waktu_akhir','nim_mahasiswa','status_pinjam','keterangan'));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createKelas(
					$_POST['nama_ruangan'],
					$_POST['tanggal_pinjam'],
					$_POST['waktu_awal'],
					$_POST['waktu_akhir'],
					$_POST['nim_mahasiswa'],
					$_POST['status_pinjam'],
					$_POST['keterangan']
				);
				

				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 

					//in message we have a success message
					$response['message'] = 'a';

					//and we are getting all the heroes from the database in the response
					$response['kelas'] = $db->getKelas($_POST['nim_mahasiswa'], $_POST['status_pinjam']);
				}else{

					//if record is not added that means there is an error 
					$response['error'] = true; 

					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}
				
			break; 
			
			//the READ operation
			//if the call is getheroes
			case 'getkelas':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'a';
				$response['kelas'] = $db->getKelas($_GET['nim_mahasiswa'], $_GET['status_pinjam']);
			break; 

			//the READ PINJAM KELASSS
			case 'getkelaspinjam':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'a';
				$response['kelas'] = $db->getKelasPinjam($_GET['status_pinjam']);
			break; 

			
			
			//the UPDATE operation
			case 'updatekelas':
				isTheseParametersAvailable(array('id_peminjaman','nama_ruangan','tanggal_pinjam','waktu_awal','waktu_akhir','nim_mahasiswa','status_pinjam','keterangan'));
				$db = new DbOperation();
				$result = $db->updateKelas(
					$_POST['id_peminjaman'],
					$_POST['nama_ruangan'],
					$_POST['tanggal_pinjam'],
					$_POST['waktu_awal'],
					$_POST['waktu_akhir'],
					$_POST['nim_mahasiswa'],
					$_POST['status_pinjam'],
					$_POST['keterangan']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Kelas updated successfully';
					$response['kelas'] = $db->getKelas($_POST['nim_mahasiswa'],$_POST['status_pinjam']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break; 
			
			//the delete operation
			case 'deletekelas':

				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['id_peminjaman'],$_GET['nim_mahasiswa'],$_GET['status_pinjam'])){
					$db = new DbOperation();
					if($db->deleteKelas($_GET['id_peminjaman'],$_GET['nim_mahasiswa'],$_GET['status_pinjam'])){
						$response['error'] = false; 
						$response['message'] = 'Kelas deleted successfully';
						$response['kelas'] = $db->getKelas($_GET['nim_mahasiswa'],$_GET['status_pinjam']);
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide an id please';
				}
			break; 

		}
		
	}else{
		//if it is not api call 
		//pushing appropriate values to response array 
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	//displaying the response in json structure 
    echo json_encode($response);
?>