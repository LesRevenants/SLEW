<?php 

session_start();
$arg = $_POST["name"];
echo $arg;
$locale = 'en_GB.utf-8';
setlocale(LC_ALL, $locale);
putenv('LC_ALL='.$locale);
$arg = utf8_decode($arg);
$response = exec("java -jar Untitled.jar {$arg}", $output);
//$parse = utf8_decode(urldecode($response));
//$test = utf8_decode(urldecode($output));
$_SESSION["mar"] = $output[0] . $output[1];
if(isset($_SESSION['mar'])) {
header("Location: marmotte.php", true, 301); }

?>
