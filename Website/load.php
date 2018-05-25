<?php 

session_start();

$arg = str_replace(' ', '_', $_POST["name"]);
echo $arg;
$locale = 'fr_FR.utf-16';
setlocale(LC_ALL, $locale);
putenv('LC_ALL='.$locale);
//$arg = utf8_decode($arg);

try {
 $out = phpClient($arg);   //exec("java -jar ready.jar {$arg}", $output);
} catch (Exception $e) { echo "yolo"; }

$outpute = explode("@", $out);
$output = explode("\n", $outpute[0]);
$other = $outpute[1];

//echo "Fin php Client--" + "\n";

/*if($output== null) { $_SESSION["mar"] = "There is no article on the french Wikipedia with that name."; }
if(substr($output[1], 0, 16) == "Géolocalisation") { $_SESSION["mar"] = $output[4]; }
else if(strlen($output[0]) < 50 || $output[0] == $output[1]) {  if(substr($output[1], -1) == ':') { $_SESSION["mar"] = $output[1] . $output[2] . $output[3] . $output[4]; } 
else { $_SESSION["mar"] = $output[1]; } }
else { $_SESSION["mar"] = $output[0] . "\n" . $output[1]; }*/

$_SESSION["mar"] = $output[1];
$_SESSION["rel"] = $other;
$_SESSION["total"] = $out;
if(isset($_SESSION['mar'])) {
header("Location: marmotte.php", true, 301); } 

function phpClient($arg) {

 $PORT = 20242; //the port on which we are connecting to the "remote" machine
 $HOST = "localhost"; //the ip of the remote machine (in this case it's the same machine)
 
 $sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
         or die("error: could not create socket\n");
 
 $succ = socket_connect($sock, $HOST, $PORT) //Connecting to to server using that socket
         or die("error: could not connect to host\n");
 
$text = $arg; //the text we want to send to the server

socket_write($sock, $text . "\n", strlen($text) + 1) //Writing the text to the socket
       or die("error: failed to write to socket\n");

$reply = socket_read($sock, 100000) //Reading the reply from socket
        or die("error: failed to read from socket\n");
  return $reply; }

?>