<?php 

session_start();
$arg = str_replace(' ', '_', $_POST["name"]);
echo $arg;
$_SESSION['article_name'] = $arg;
header("Location: manual_injecting.php", true, 301); 

?>
