<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$sql = $_POST["query"];
	if(mysqli_query($con,$sql)){
		echo json_encode(array('response'=>'Thank You.'));
	}
	else{
		echo json_encode(array('response'=>'Error'));
	}
}
else{
	echo json_encode(array('response'=>'Error, Check Internet Connection'));
}
mysqli_close($con);
?>