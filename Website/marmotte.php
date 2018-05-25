<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
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
						<h3>Is it what you are looking for ?</h3>
					</header>
					<form method="post" action="marmotte.php">
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
	</html>'; }


	
if(isset($_POST['test'])) {
$arg = "yo";
$colin = explode("{",$_SESSION['rel']);
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
						<h3>Here are the extracted relations</h3>
					</header>
							<span style="font-size:50% !important;">
							<table>
								<tr><th>N°</th>
								<th>Object</th>
								<th>Predicate</th>
								<th>Subject</th>
								<th>Relation Type</th>
								</tr>';
								for($cpt=1;$cpt<=sizeof($colin)-1;$cpt++) {
								$colin[$cpt] = str_replace("\"","",$colin[$cpt]);
								$seneron = explode(",",$colin[$cpt]);
								$pascual1 = explode(":",$seneron[0]);
								$pascual2 = explode(":",$seneron[1]);
								$pascual3 = explode(":",$seneron[2]);
								$pascual4 = explode(":",$seneron[3]);
								echo '<tr><td>' . $cpt . '</td><td>'. $pascual2[1] . '</td><td>' . str_replace("}","",$pascual4[1]) . '</td><td>' . $pascual1[1] . '</td><td>' . $pascual3[1] . '</td></tr>'; }
								echo '</table></span><br><br>
								<br><br><br>
								<center><div id="DataExtraction" style="height: 370px; width: 30%;"></div></center>
								<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
								<br><br><br>
						<div class="row">
							<div class="12u">
								<ul class="actions">
									<li><a href="http://localhost/~fpascual/SLEW/Website"><input type="submit" name="test" value="Get Back" /></a></li>
								</ul>
							</div>
						</div>
					</form>
				</section>
			</article>
		</body>
		<script>
		window.onload = function() {

				var chary = new CanvasJS.Chart("DataExtraction", {
					animationEnabled: true,
					title: {
						text: "Relation Types Insights"
					},
					data: [{
						type: "pie",
						startAngle: 240,
						yValueFormatString: "##0.00\"%\"",
						indexLabel: "{label} {y}",
						dataPoints: [';
							for($cpt=1;$cpt<=sizeof($colin)-1;$cpt++) {
								$colin[$cpt] = str_replace("\"","",$colin[$cpt]);
								$seneron = explode(",",$colin[$cpt]);
								$pascual1 = explode(":",$seneron[0]);
								$pascual2 = explode(":",$seneron[1]);
								$pascual3 = explode(":",$seneron[2]);
								$pascual4 = explode(":",$seneron[3]);
								$table[$cpt] = $pascual3[1]; }
								$thetab = array_count_values($table);
								$keys = array_keys($thetab);
							for($cp=0;$cp<=sizeof($thetab)-1;$cp++) {
								$size = sizeof($colin)-1;
								$result = $thetab[$keys[$cp]]/$size;
								echo '{y: ' . $result*100 . ', label: "' . $keys[$cp] . '"}';
								if($cp !== sizeof($thetab)-1) { echo ','; } }
						echo ']
					}]
				});
			chary.render(); }
		</script>
	</html>'; } 


?>
