<?php

//General purpose HTTP post to validate a username and password from the database.

if (isset($_POST["Username"]) && isset($_POST["Password"])) {
	require_once('queryDatabaseHelper.php');
	$username = $_POST["Username"];
	$password = $_POST["Password"];
	if (validUsernameAndPassword($username, $password)) {
		echo 'true';
	}
	else {
		echo 'false';
	}
}
?>