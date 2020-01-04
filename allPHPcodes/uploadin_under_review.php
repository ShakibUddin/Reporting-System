<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$id = $_POST['id'];
	$issue = $_POST['issue'];
	$report = $_POST["report"];
	$sql = "insert into under_review(id,issue,report) values('$id','$issue','$report')";

	if(mysqli_query($con,$sql)){
		echo json_encode(array('response'=>'The person concerned will be notified'));
	}
	else{
		echo json_encode(array('response'=>'Error'));
	}
}
else{
	echo json_encode(array('response'=>'Submission Failed'));
}
mysqli_close($con);
?>