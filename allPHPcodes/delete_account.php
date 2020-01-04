<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$sql = $_POST["query"];
	if(mysqli_query($con,$sql)){
		echo json_encode(array('response'=>'Account Deleted.'));
	}
	else{
		echo json_encode(array('response'=>'Account could not be deleted.'));
	}
}
else{
	echo json_encode(array('response'=>'Check Internet Connection.'));
}
mysqli_close($con);
?>