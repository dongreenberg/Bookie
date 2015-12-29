<?php

//Allows a user on the browser interface to create a new organization, of which they are an admin.
session_start();

if (isset($_POST["Org_Name"]) && isset($_POST["Description"]) && isset($_POST["Contact"])) {
	$orgname = $_POST["Org_Name"];
	$description = $_POST["Description"];
	$contact = $_POST["Contact"];
	
	$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");

	/* check connection */
	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}

	$insertOrgQuery = 'INSERT INTO `organization`(`orgname`, `description`, `contact`) VALUES 
	(\'' . $orgname . '\',\'' . $description . '\',\'' . $contact . '\'); 
	INSERT INTO `admin`(`username`, `orgnumber`) VALUES (\'' . $_SESSION['username'] . '\', ( 
	SELECT `orgnumber` FROM `organization` WHERE `orgname`=\'' . $orgname . '\'));';

	if (mysqli_multi_query($link, $insertOrgQuery)) {
		header('Location: http://localhost/home.html');
		exit();
	}
	
	mysqli_close($link);
}


?>