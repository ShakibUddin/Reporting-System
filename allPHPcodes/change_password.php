<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$username= $_POST["username"];
	$input_password = $_POST["input_password"];
	$new_password = $_POST["new_password"];
	$confirm_new_password = $_POST["confirm_new_password"];

	$sql="SELECT * FROM login_data WHERE username='$username'";
	$response=mysqli_query($con,$sql);

	if(mysqli_num_rows($response)===1){
		$row = mysqli_fetch_assoc($response);
		if(password_verify($input_password,$row['password'])){
			if($new_password===$confirm_new_password){
				$hash_password=password_hash($new_password, PASSWORD_DEFAULT);
				if(mysqli_query($con,"UPDATE login_data SET password='$hash_password' WHERE username='$username'")){
					$result["message"]="1";
				}
				else{
					$result["message"]="-1";
				}
				
			}
			else{
				$result["message"]="2";
			}
		}
		else{
			$result["message"]="3";
		}
	}
	echo json_encode($result);
}
else{
	echo json_encode(array('response'=>'Check Internet Connection.'));
}
mysqli_close($con);
?>