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
	$image = $_POST["image"];
	$image_name = $_POST["name"];
	$sql = "insert into data(id,issue,report,image_name,image) values('$id','$issue','$report','$image_name','$image')";
	$upload_path= "images/$image_name.png";
	

	if(mysqli_query($con,$sql)){
		file_put_contents($upload_path,base64_decode($image));
		echo json_encode(array('response'=>'Report Submitted Successfully'));
	}
	else{
		echo json_encode(array('response'=>'Report Submission Failed'));
	}
}
else{
	echo json_encode(array('response'=>'Submission Failed'));
}
mysqli_close($con);
?>