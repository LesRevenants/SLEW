<?php 

session_start();

$arg = "marmotte";//str_replace(' ', '_', $_POST["name"]);
echo $arg;
$locale = 'en_GB.utf-8';
setlocale(LC_ALL, $locale);
putenv('LC_ALL='.$locale);
$arg = utf8_decode($arg);

try {
 $output = phpClient($arg);   //exec("java -jar ready.jar {$arg}", $output);
} catch (Exception $e) { echo "yolo"; }

echo "Fin php Client--" + "\n";
//print_r($output);/*
//$parse = utf8_decode(urldecode($response));
//$test = utf8_decode(urldecode($output));*/

if($output== null) { $_SESSION["mar"] = "There is no article on the french Wikipedia with that name."; }
if(substr($output[1], 0, 16) == "Géolocalisation") { $_SESSION["mar"] = $output[4]; }
else if(strlen($output[0]) < 50 || $output[0] == $output[1]) {  if(substr($output[1], -1) == ':') { $_SESSION["mar"] = $output[1] . $output[2] . $output[3] . $output[4]; } 
else { $_SESSION["mar"] = $output[1]; } }
else { $_SESSION["mar"] = $output[0] . "\n" . $output[1]; }
if(isset($_SESSION['mar'])) {
header("Location: marmotte.php", true, 301); }


function tcp_send($socket,$arg,$address,$port){

$message = $arg;

$len = strlen($message);
$sent = socket_sendto($socket, $message, $len, 0, $address, $port);
return true;

}

function tcp_recv($socket){


$buf = array();
$len = 100;
echo "Client is waiting" + "\n";
$recv = socket_recv ($socket , $buf ,  $len , null );
socket_close($socket);
return $recv;
}


function phpClient($arg) {

	$address = '0.0.0.0';
	$port = 5034;
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));

	socket_connect($socket, $address, $port);
        echo "Connection" + "\n";

	$status = tcp_send($socket,$arg,$address,$port); 
        echo "sent :" + $status + "\n";

	$status = tcp_recv($socket);
        echo "recv :" + $status + "\n";





}

?>
