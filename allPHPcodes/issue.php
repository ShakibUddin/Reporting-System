<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";

$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if($con){
    $issue=$_POST["issue"];
    $sql = "insert into issue(member_issue) values('$issue')";
    
    if(mysqli_query($con,$sql)){
        echo json_encode(array('response'=>'issue uploaded'));
    }
    else{
        echo json_encode(array('response'=>'issue upload Error'));
    }
}
else{
    echo json_encode(array('response'=>'Error'));
}
mysqli_close($con);
?>

    