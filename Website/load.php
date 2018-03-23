<?php 

session_start();
$arg = str_replace(' ', '_', $_POST["name"]);
echo $arg;
$locale = 'en_GB.utf-8';
setlocale(LC_ALL, $locale);
putenv('LC_ALL='.$locale);
$arg = utf8_decode($arg);
try {
$response = exec("java -jar Untitled.jar {$arg}", $output);
} catch (Exception $e) { echo "yolo"; }
print_r($output);/*
//$parse = utf8_decode(urldecode($response));
//$test = utf8_decode(urldecode($output));
if($response == null) { $_SESSION["mar"] = "There is no article on the french Wikipedia with that name."; }
if(substr($output[1], 0, 16) == "Géolocalisation") { $_SESSION["mar"] = $output[4]; }
else if(strlen($output[0]) < 50 || $output[0] == $output[1]) {  if(substr($output[1], -1) == ':') { $_SESSION["mar"] = $output[1] . $output[2] . $output[3] . $output[4]; } 
else { $_SESSION["mar"] = $output[1]; } }
else { $_SESSION["mar"] = $output[0] . "\n" . $output[1]; }
if(isset($_SESSION['mar'])) {
header("Location: marmotte.php", true, 301); }*/

?>
