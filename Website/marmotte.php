<?php

session_start();

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
									<li><input type="submit" value="Confirm" /></li>
								</ul>
							</div>
						</div>
					</form>
				</section>
			</article>
		</body>
	</html>;';

?>
