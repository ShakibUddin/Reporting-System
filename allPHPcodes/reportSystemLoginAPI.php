<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$username = $_POST["username"];
	$password = $_POST["password"];

	$sql = "SELECT * FROM login_data WHERE username ='$username'";
	$response = mysqli_query($con,$sql);
	if(mysqli_num_rows($response)===1){//mysqli_num_rows($result) returns number of rows in a result set
		$row = mysqli_fetch_assoc($response);

		if(password_verify($password,$row['password'])){
			$result["id"]=$row['id'];
			$result["password"]=$row['password'];
			if($row['post']==='Employee'){
				$result["success"]="1";
				$result["member"]="Employee";
			}
			else{
				$result["success"]="2";
				$result["member"]=$row['post'];
			}
			
		}
		else{	
			$result["member"]="";
		}
		echo json_encode($result);
		mysql_close($con);
	}
	//attempted login is not from employee ,its from a member(Admin side)
}
else{
	echo json_encode(array('response'=>'Check your internet connection'));
}

?>