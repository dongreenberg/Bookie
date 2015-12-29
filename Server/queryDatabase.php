<?php

//HTTP Post allowing mobile and browser clients to call the queries specified in queryDatabaseFunc. 

if (isset($_POST["Username"]) && isset($_POST["Password"]) && isset($_POST['QueryType'])) {
	require_once('queryDatabaseHelper.php');
	$username = $_POST["Username"];
	$password = $_POST["Password"];
	$queryType = $_POST["QueryType"];
	$valid = validUsernameAndPassword($username, $password);
	if ($valid === True) {
		$json_to_send = array();
		$results = queryDatabaseFunc($username, $password, $queryType);
		$subscribed = false;
		if((strcmp($queryType, "myOrgs") == 0) || (strcmp($queryType, "browseOrgs") == 0)){
			if (strcmp($queryType, "myOrgs") == 0) {
				$subscribed = true;
			}
			$link = mysqli_connect("localhost", "Tommy", "pickles", "Model");
			if (mysqli_connect_errno()) {
    				printf("Connect failed: %s\n", mysqli_connect_error());
    				exit();
			}
			$orglist = array();
			
			$orgnumberlist = array();
			
			foreach($results as $orgrow) {
				$found = false;
				for ($x = 0; $x < count($orgnumberlist); $x++) {
					if ($orgnumberlist[$x] === $orgrow['orgnumber']) {
						$found = true;
						break;
					}
				}
				if ($found) {
					continue;
				}
				array_push($orgnumberlist, $orgrow['orgnumber']);
			
				$eventlist = array();
				$eventrowarray = array();
				$eventQuery = 'SELECT * FROM `event` WHERE `orgnumber`=' . $orgrow['orgnumber'];
			 	if ($eventresults = mysqli_query($link, $eventQuery, MYSQLI_USE_RESULT)) {
					while ($eventrow = mysqli_fetch_array($eventresults)) {
						array_push($eventrowarray, $eventrow);
					}
				}
				$eventlist = convertEventRows($eventrowarray);
				$tmporg = array('name' => $orgrow['orgname'], 'orgNumber' => (int)$orgrow['orgnumber'], 'subscribed' => $subscribed, 'description' => $orgrow['description'], 'contact' => $orgrow['contact'], 'eventList' => $eventlist);
				array_push($orglist, $tmporg);
    		}
			mysqli_close($link);
			$json_to_send = $orglist;
		} elseif (strcmp($queryType, "myEvents") == 0) {
			$json_to_send = convertEventRows($results);
		} elseif (strcmp($queryType, "terms") == 0) {
			$termlist = array();
			foreach($results as $termrow) {
				$tmpterm = array('termID' => $termrow['termID']);
				array_push($termlist, $tmpterm);
			}
			$json_to_send = $termlist;
		}
		echo json_encode($json_to_send);
	}
	else {
		echo 'Error';
	}
}
?>