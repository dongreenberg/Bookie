<html>
<body>
<h1> Create Event: <br /> </h1>
<form action="addEvent.php" method="post">
Event Name: <input type="text" name="Eventname"><br>
Group: 
<select name="OrgNumber">
<?php
    require_once('queryDatabaseHelper.php');
    session_start();
   	$results = queryDatabaseFunc($_SESSION['username'], $_SESSION['password'], 'myAdmins');
    foreach($results as $row) 
    { 
	   echo '<option value="'. $row['orgnumber'] .'">'. $row['orgname'] .'</option>';
    }
?>
</select>
Location: <input type="text" name="Location"><br>
Days: <input type="text" name="Days"><br>
Description: <input type="text" name="Description"><br>
Start Year: <input type="text" name="StartYear"><br>
Start Month: <input type="text" name="StartMonth"><br>
Start Day of Month: <input type="text" name="StartDayOfMonth"><br>
End Year: <input type="text" name="EndYear"><br>
End Month: <input type="text" name="EndMonth"><br>
End Day of Month: <input type="text" name="EndDayOfMonth"><br>
Start Hour (Military Time): <input type="text" name="StartHour"><br>
Start Minute: <input type="text" name="StartMinute"><br>
End Hour (Military Time): <input type="text" name="EndHour"><br>
End Minute: <input type="text" name="EndMinute"><br>
<input type="submit">
</form>

</body>
</html>