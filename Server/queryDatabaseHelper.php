<?php

//Provides functionality for several queries from the database:
//    1. Getting all organizations for a given user.
//    2. Getting all organizations in the system, to browse.
//    3. Getting all organizations for which a given user is admin.
//    4. Getting all events for a given user.
//    5. Getting the semester terms (i.e. start and end dates) for a given semester.

function queryDatabaseFunc($username, $password, $queryType) {
	$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");

	if(strcmp($queryType, "myOrgs") == 0) {
		$query = 'SELECT Organization.orgnumber, `orgname`, `description`, `contact` FROM Organization
			INNER JOIN Subscription
			ON Subscription.orgnumber=Organization.orgnumber
			WHERE \'' . $username . '\'=Subscription.username';
	} elseif (strcmp($queryType, "browseOrgs") == 0) {
		$query = 'SELECT Organization.orgnumber, `orgname`, `description`, `contact` FROM Organization WHERE `orgnumber` NOT IN (SELECT Organization.orgnumber FROM Organization
			INNER JOIN Subscription
			ON Subscription.orgnumber=Organization.orgnumber
			WHERE \'' .  $username . '\'=Subscription.username)';
	} elseif (strcmp($queryType, "myAdmins") == 0) {
		$query = 'SELECT * FROM Organization
			INNER JOIN Admin
			ON Admin.orgnumber=Organization.orgnumber
			WHERE \'' . $username . '\'=Admin.username';
	} elseif (strcmp($queryType, "myEvents") == 0) {
		$query = 'SELECT * FROM Event
			INNER JOIN RSVP
			ON RSVP.eventnumber=Event.eventnumber
			WHERE \'' . $username . '\'=RSVP.username';	
	} elseif (strcmp($queryType, "terms") == 0) {
		$query = 'SELECT * FROM `term`';
	}
	/* check connection */
	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}
	/* Select queries return a resultset */
	$return = array();
	if ($results = mysqli_query($link, $query, MYSQLI_USE_RESULT)) {
		while ($row = mysqli_fetch_array($results)) { 
			array_push($return, $row);
		}
	}
	mysqli_close($link);
	return $return;
}

function convertEventRows($eventrowarray) {
	$eventlist = array();
	foreach($eventrowarray as $eventrow) {
		$startDate = array('year' => (int)$eventrow['startyear'], 'month' => (int)$eventrow['startmonth'], 'dayOfMonth' => (int)$eventrow['startdayofmonth'], 'hourOfDay' => 0, 'minute' => 0, 'second' => 0);
		$endDate = array('year' => (int)$eventrow['endyear'], 'month' => (int)$eventrow['endmonth'], 'dayOfMonth' => (int)$eventrow['enddayofmonth'], 'hourOfDay' => 0, 'minute' => 0, 'second' => 0);
		$startTime = array('year' => 0, 'month' => 0, 'dayOfMonth' => 0, 'hourOfDay' => (int)$eventrow['starthour'], 'minute' => (int)$eventrow['startminute'], 'second' => 0);
		$endTime = array('year' => 0, 'month' => 0, 'dayOfMonth' => 0, 'hourOfDay' => (int)$eventrow['endhour'], 'minute' => (int)$eventrow['endminute'], 'second' => 0);
						
		$tmpevent = array('Name' => $eventrow['eventname'], 'EventNumber' => (int)$eventrow['eventnumber'], 'Location' => $eventrow['location'], 'Days' => $eventrow['days'], 'Description' => $eventrow['description'], 'StartDate' => $startDate, 'EndDate' => $endDate, 'StartTime' => $startTime, 'EndTime' => $endTime);
		
		array_push($eventlist, $tmpevent);
	}
	return $eventlist;
}

function validUsernameAndPassword($username, $password) {
	$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");
	$query = 'SELECT `password` FROM `user` WHERE `username`=\''.$username.'\''; 
	
	/* check connection */
	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}
	/* Select queries return a resultset */
	$retval = false;
	if ($res = mysqli_query($link, $query, MYSQLI_STORE_RESULT)) {
		$tmp = mysqli_fetch_array($res);
		if (strcmp($tmp['password'], $password) == 0) {
			$retval = true;
		}
	}
	mysqli_close($link);
	return $retval;
}
?>