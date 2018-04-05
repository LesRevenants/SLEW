<!DOCTYPE html>
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
    <script src="js/addInput.js" language="Javascript" type="text/javascript"></script>
<!DOCTYPE HTML>
<article class="container box style3">
				<section>
					<header>
						<h3>Article: <?php session_start(); echo $_SESSION['article_name'];?></h3>
					</header>
<form method="POST" action="injection.php">
	<table>
         
	<tr>
		<th> N° </th>
		<th> Relation x </th>
		<th> Relation y </th>
		<th> Relation Type </th>
		<th> Predicate </th>
	</tr><br>
	<tr>
		<td class="centerise"> 1 </td>
		<td><input type="text" name="x[]"></td>
		<td><input type="text" name="y[]"></td>
		<td><input type="text" name="type[]"></td>
		<td><input type="text" name="predicate[]"></td>
	</tr>

	</table>

	<div id="dynamicInput">         </div>
         <span style="width:50%;"><input type="add" value="Add another text input" onClick="addInput('dynamicInput');"></span>
	<input type="hidden" name="article" value=<?php echo $_SESSION['article_name'];?>>
	<br>
						<ul class="actions">
																		<center><li><input type="submit" value="Submit" /></li>
								</center>
								</ul>
							
					</form>
				</section>
			</article>
		</body>
	</html>
    


