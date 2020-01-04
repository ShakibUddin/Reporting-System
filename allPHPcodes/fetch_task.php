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
    
    $sql="SELECT * FROM issue";
    $response = mysqli_query($con,$sql);
    if(mysqli_num_rows($response)>0){
        $result = mysqli_fetch_assoc($response);
        $issue=$result['member_issue'];
        
    }

    
    $task_fetch_sql="SELECT * FROM task WHERE issue='$issue'";
    $retrieved_data = array();
    $response = mysqli_query($con,$task_fetch_sql);
    if(mysqli_num_rows($response)>0){
        while($row = mysqli_fetch_array($response)){
            
            $data=['id'=>$row["id"],'issue'=>$row["issue"],'report'=>$row["report"]];

            array_push($retrieved_data, $data);
        }
        echo json_encode(['retrieved_data'=>$retrieved_data]);
    }
    else{
        echo json_encode(['response'=>'Error']);
    }
}
if(mysqli_query($con,"DELETE FROM issue")){
            echo json_encode('Issue deleted');
}
mysqli_close($con);

?>