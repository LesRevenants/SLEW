<?php

session_start();

if(!isset($_POST['test'])) {

echo '<!DOCTYPE HTML>
<!--
	Overflow by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>SLEW</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<!--[if lte IE 8]><script src="assets/js/ie/html5shiv.js"></script><![endif]-->
		<link rel="stylesheet" href="assets/css/main.css" />
		<!--[if lte IE 8]><link rel="stylesheet" href="assets/css/ie8.css" /><![endif]-->
	</head>
	<body>

<article class="container box style3">
				<section>
					<header>
						<h3>Is it what you were looking for ?</h3>
					</header>
					<form method="post" action="load.php">
						<div class="middler">
							<div class="middler">
								<center>'. $_SESSION['mar'] .'</center>
							</div>
						</div>
						<div class="row">
							<div class="12u">
								<ul class="actions">
									<li><input type="submit" name="test" value="Confirm" /></li>
								</ul>
							</div>
						</div>
					</form>
				</section>
			</article>
		</body>
	</html>;'; }


	
if(isset($_POST['test'])) {
$arg = "yo";
$relations = phpClient($arg);
echo '<!DOCTYPE HTML>
<!--
	Overflow by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>SLEW</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<!--[if lte IE 8]><script src="assets/js/ie/html5shiv.js"></script><![endif]-->
		<link rel="stylesheet" href="assets/css/main.css" />
		<!--[if lte IE 8]><link rel="stylesheet" href="assets/css/ie8.css" /><![endif]-->
	</head>
	<body>

<article class="container box style3">
				<section>
					<header>
						<h3>Is it what you were looking for ?</h3>
					</header>
					<form method="post" action="load.php">
						<div class="middler">
							<div class="middler">
								<center>'. $relations .'</center>
							</div>
						</div>
						<div class="row">
							<div class="12u">
								<ul class="actions">
									<li><input type="submit" name="test" value="Confirm" /></li>
								</ul>
							</div>
						</div>
					</form>
				</section>
			</article>
		</body>
	</html>;'; }

function phpClient($arg) {

 $PORT = 20233; //the port on which we are connecting to the "remote" machine
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
