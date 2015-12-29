<?php

//General function to insert into the database from mobile. Allows a user to 
//subscribe to an organization, rsvp to an event, unsbscribe from an organization, or register a device.

function insertDatabaseFunc($username, $password, $queryType, $entity) {
	require_once('queryDatabaseHelper.php');

	if (validUsernameAndPassword($username, $password)) {
		$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");

		if(strcmp($queryType, 'Unsubs') == 0) {
			$query = 'DELETE FROM `subscription` WHERE `username`=\'' . $username . '\' AND `orgnumber`=\'' . $entity . '\'';
		} elseif (strcmp($queryType, 'RSVP') == 0) {
			$query = 'INSERT IGNORE INTO `rsvp`(`username`, `eventnumber`) VALUES (\'' . $username . '\',' . $entity . ')';
		} elseif (strcmp($queryType, 'Subs') == 0) {
			$query = 'INSERT INTO `subscription`(`username`, `orgnumber`) VALUES (\'' . $username . '\',\'' . $entity . '\')';
		} elseif (strcmp($queryType, 'AndroidDevice') == 0) {
			$query = 'INSERT IGNORE INTO `androiddevice`(`username`, `device_id`) VALUES (\'' . $username . '\',\'' . $entity . '\')';
		}
	
		if (mysqli_connect_errno()) {
    		printf("Connect failed: %s\n", mysqli_connect_error());
    		exit();
		}
	
		if (mysqli_query($link, $query)) {
			echo 'true';
		}
		else {
			echo 'false';
		}
		mysqli_close($link);
	}
	else {
		echo 'false';
	}
}
?>