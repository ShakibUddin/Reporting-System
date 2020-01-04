<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$name_sql = $_POST["name_query"];
	$post = $_POST["post"];
	$id_sql = $_POST["id_query"];
	if(mysqli_query($con,$name_sql)){
		if($post!=="Employee"){
			if(mysqli_query($con,$id_sql)){
				$result["message"]="1";
			}
			else{
				$result["message"]="0";
			}
		}
		else{
			$result["message"]="1";
		}
	}
	else{
		$result["message"]="0";
	}
	echo json_encode($result);
}
else{
	echo json_encode(array('response'=>'Check Internet Connection.'));
}
mysqli_close($con);
?>