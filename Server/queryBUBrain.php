<?php 

//HTTP Post to query BUBrain from mobile.

if (isset($_POST["Username"]) && isset($_POST["Password"]) && isset($_POST["Term"])) {
	$username = $_POST["Username"];
	$password = $_POST["Password"];
	$term = $_POST["Term"];
	exec("java -jar C:\\Users\\Steven\\workspace\\BUBrainScheduler\\bin\\BUBrainScheduler.jar $username $password $term", $output); 
#	print_r($output);
	echo $output[0];
}

?>