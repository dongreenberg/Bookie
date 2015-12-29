<?php

//Allows the user on mobile to create an account. The mobile application calls an HTTP post.

if (isset($_POST["Username"]) && isset($_POST["Password"]) && isset($_POST['Firstname']) && isset($_POST['Lastname']) && isset($_POST['Bmail'])) {
	$username = $_POST["Username"];
	$password = $_POST["Password"];
	$firstname = $_POST["Firstname"];
	$lastname = $_POST["Lastname"];
	$bmail = $_POST["Bmail"];
	
	$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");
	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}
	
	$ret = 'false';
	$query = 'SELECT * FROM `user` WHERE username=\'' . $username . '\''; 
	if ($results = mysqli_query($link, $query, MYSQLI_USE_RESULT)) {
		$row = mysqli_fetch_array($results);
		if (empty($row)) {
			$insertquery = 'INSERT INTO `user`(`username`, `password`, `firstname`, `lastname`, `bmail`) VALUES (\'' . $username . '\',\'' . $password . '\',\'' . $firstname . '\',\'' . $lastname . '\',\'' . $bmail . '\')';
			mysqli_query($link, $insertquery, MYSQLI_USE_RESULT);
			$ret = 'true';
		}
	}
	mysqli_close($link);
	echo $ret;
}
?>