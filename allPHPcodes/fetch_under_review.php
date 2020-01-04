<?php
$user_name = "root";
$user_pass="";
$host_name="localhost";
$db_name="report";


$con=mysqli_connect($host_name,$user_name,$user_pass,$db_name);

if(mysqli_connect_errno()){
    echo json_encode(array('response'=>'Failed to connect to Mysql'));
    die();
}
if($con){
    $sql="SELECT * FROM under_review";
    $retrieved_data = array();
    $response = mysqli_query($con,$sql);
    if(mysqli_num_rows($response)>0){
        while($row = mysqli_fetch_array($response)){
            
            $data=['id'=>$row["id"],'issue'=>$row["issue"],'report'=>$row["report"]];

            array_push($retrieved_data, $data);
        }
        echo json_encode(['under_review_data'=>$retrieved_data]);
    }
}
mysqli_close($con);

?>