<?php

//HTTP Post allowing users to login from the broswer interface. Starts a php session.

session_start();

if (isset($_POST["username"]) && isset($_POST["password"])) {
	$username = $_POST["username"];
	$password = $_POST["password"];

	require_once('queryDatabaseHelper.php');
	
	$redirect = "Location: http://localhost/login.html";
	if (validUsernameAndPassword($username, $password)) {
		$_SESSION['username'] = $username;
		$_SESSION['password'] = $password;	
		$redirect = "Location: http://localhost/home.html";
	}   
	header($redirect);
	//exit();
}

?>