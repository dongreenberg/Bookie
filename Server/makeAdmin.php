<html>
<body>
<h1> Add Admins: <br /> </h1>
<form action="insertAdmin.php" method="post">
Username: <input type="text" name="Username"><br>
Group: 
<select name="Entity">
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
<input type="submit">
</form>

</body>
</html>