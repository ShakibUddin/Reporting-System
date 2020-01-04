<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
	$image = $_POST["image"];
	$name = $_POST["name"];
	$sql = "insert into data(name) values('$name')";
	$upload_path= "images/$name.png";

	if(mysqli_query($con,$sql)){
		file_put_contents($upload_path,base64_decode($image));
		echo json_encode(array('response'=>'Image Uploaded Successfully'));
	}
	else{
		echo json_encode(array('response'=>'Image name exists. Please try again'));
	}
}
else{
	echo json_encode(array('response'=>'Image Upload Failed'));
}
mysqli_close($con);
?>