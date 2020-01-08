<?php 
	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
					
			case 'login':
				
				if(isTheseParametersAvailable(array('nim', 'password'))){
					
					$nim = $_POST['nim'];
					$password = ($_POST['password']); 
					
					$stmt = $conn->prepare("SELECT nim, nama, fakultas , prodi FROM data_mahasiswa WHERE nim = ? AND password = ?");
					$stmt->bind_param("ss",$nim, $password);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
						$stmt->bind_result($nim, $nama, $fakultas, $prodi);
						$stmt->fetch();
						
						$user = array(
							'nim'=>$nim, 
							'nama'=>$nama,
                            'fakultas'=>$fakultas,
                            'prodi'=>$prodi
						);
						
						$response['error'] = false; 
						$response['message'] = 'Login successfull'; 
						$response['user'] = $user; 
					}else{
						$response['error'] = false; 
						$response['message'] = 'Invalid username or password';
					}
				}
			break;
						
			default: 
				$response['error'] = true; 
				$response['message'] = 'Invalid Operation Called';
		}
		
	}else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	echo json_encode($response);
	
	function isTheseParametersAvailable($params){
		
		foreach($params as $param){
			if(!isset($_POST[$param])){
				return false; 
			}
		}
		return true; 
    }
?>