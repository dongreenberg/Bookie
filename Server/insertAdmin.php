<?php

//Inserts an admin to an organization, allowing multiple admins to an organization. Only admins may add events for an organization.

if (isset($_POST['Username']) && isset($_POST['Entity'])) {
	$username = $_POST['Username'];
	$entity= $_POST['Entity'];

	$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");

	$query = 'INSERT INTO `admin`(`username`, `orgnumber`) VALUES (\'' . $username . '\',\'' . $entity . '\')';
	
	/* check connection */
	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}
	/* Select queries return a resultset */
	if (mysqli_query($link, $query)) {
		header('Location: http://localhost/home.html');
		exit();
	} else {
		echo '<br><br>error assigning admin';
	}
	mysqli_close($link);
}
?>