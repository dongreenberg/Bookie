<?php

//HTTP Post, callable from mobile, that calls the insertDatabaseFunc function.

if (isset($_POST['Username']) && isset($_POST['Password']) && isset($_POST['QueryType']) && isset($_POST['Entity'])) {
	require_once('insertDatabaseHelper.php');
	$username = $_POST['Username'];
	$password = $_POST['Password'];
	$queryType = $_POST['QueryType'];
	$entity = $_POST['Entity'];
	insertDatabaseFunc($username, $password, $queryType, $entity);
}
?>
