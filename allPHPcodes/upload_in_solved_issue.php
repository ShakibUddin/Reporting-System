<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$id = $_POST["id"];
	$issue = $_POST["issue"];
	$report = $_POST["report"];
	$sql = "insert into solved_issue(id,issue,report) values('$id','$issue','$report')";

	if(mysqli_query($con,$sql)){
		echo json_encode(array('response'=>'This issue is solved'));
	}
	else{
		echo json_encode(array('response'=>'Error'));
	}
}
else{
	echo json_encode(array('response'=>'Check Internet Connection'));
}
mysqli_close($con);
?>