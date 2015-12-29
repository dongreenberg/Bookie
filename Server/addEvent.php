<?php

//Script to insert an event into the database. The online organization interface calls a post, including the necessary event details, and this script will insert the event in the database. 

session_start();

if (isset($_POST["Eventname"]) && isset($_POST["OrgNumber"]) && isset($_POST["Location"]) && isset($_POST["Days"]) && isset($_POST["Description"]) && isset($_POST["StartYear"]) && isset($_POST["StartMonth"]) && isset($_POST["StartDayOfMonth"]) && isset($_POST["EndYear"]) && isset($_POST["EndMonth"]) && isset($_POST["EndDayOfMonth"]) && isset($_POST["StartHour"]) && isset($_POST["StartMinute"]) && isset($_POST["EndHour"]) && isset($_POST["EndMinute"])) {
	
	$eventname = $_POST["Eventname"];
	$orgnumber = $_POST["OrgNumber"];
	$location = $_POST["Location"];
	$days = $_POST["Days"];
	$description = $_POST["Description"];
	$startyear = $_POST["StartYear"];
	$startmonth = $_POST["StartMonth"];
	$startdayofmonth = $_POST["StartDayOfMonth"];
	$endyear = $_POST["EndYear"];
	$endmonth = $_POST["EndMonth"];
	$enddayofmonth = $_POST["EndDayOfMonth"];	
	$starthour = $_POST["StartHour"];
	$startminute = $_POST["StartMinute"];
	$endhour = $_POST["EndHour"];
	$endminute = $_POST["EndMinute"];

	$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");

	/* check connection */
	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}

	$query = 'INSERT INTO `event`(`eventname`, `orgnumber`, `location`, `days`, `description`, `startyear`, `startmonth`, `startdayofmonth`, `endyear`, `endmonth`, `enddayofmonth`, `starthour`, `startminute`, `endhour`, `endminute`) VALUES (\'' . $eventname .'\','.$orgnumber.', \'' . $location .'\', \'' . $days .'\', \'' . $description .'\','.$startyear.','.$startmonth.','.$startdayofmonth.','. $endyear.','.$endmonth.','.$enddayofmonth.','.$starthour.','.$startminute.',' . $endhour .',' . $endminute .')';

	//Database query
	if (mysqli_query($link, $query)) {
		require_once('sendAndroidNotification.php');
		require_once('queryDatabaseHelper.php');
		$getEventQuery = 'SELECT * FROM `event` WHERE `eventname`=\'' . $eventname .'\' AND `orgnumber`='.$orgnumber.' AND `location`=\'' . $location . '\' AND `days`=\'' . $days .'\' AND `description`=\'' . $description .'\' AND `startyear`='.$startyear. ' AND `startmonth`='.$startmonth.' AND `startdayofmonth`='.$startdayofmonth.' AND `endyear`='. $endyear.' AND `endmonth`='.$endmonth.' AND `enddayofmonth`='.$enddayofmonth.' AND `starthour`='.$starthour.' AND `startminute`='.$startminute.' AND `endhour`=' . $endhour .' AND `endminute`=' . $endminute;
		if ($res = mysqli_query($link, $getEventQuery)) {
			$eventarr = array();
			$tmprow = mysqli_fetch_array($res);
			$num = $tmprow['eventnumber'];
			array_push($eventarr, $tmprow);
			$convertedevents = convertEventRows($eventarr);
			$registrationIDs = array();
//			$usernames = array();
			$subquery = 'SELECT `username` FROM `subscription` WHERE `orgnumber`=' . $orgnumber;
			if ($subres = mysqli_query($link, $subquery)) {
				while ($subrow = mysqli_fetch_array($subres)) {
					$devicequery = 'SELECT * FROM `androiddevice`
										INNER JOIN `subscription`
										ON AndroidDevice.username=Subscription.username
									WHERE \'' . $subrow['username'] . '\'=Subscription.username AND ' . $orgnumber . '=Subscription.orgnumber';
					if ($regres = mysqli_query($link, $devicequery)) {
						while ($regrow = mysqli_fetch_array($regres)) {
							array_push($registrationIDs, $regrow['device_id']);
//							array_push($usernames, $regrow['username']);
							$insquery = 'INSERT IGNORE INTO `rsvp`(`username`, `eventnumber`) VALUES (\'' . $regrow['username'] . '\',' . $num . ')';
							echo $insquery;
							mysqli_query($link, $insquery);
						}
					}
				}
				
				sendAndroidNotification($registrationIDs, $convertedevents[0]);
				
				header('Location: http://localhost/home.html');
				mysqli_close($link);
				exit();
			}
		}		
	} else {
		echo 'Error adding event with sql: ' . $query;
	}
}

?>