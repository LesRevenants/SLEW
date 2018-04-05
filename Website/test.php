<?php 

 error_reporting(E_ALL);
ini_set('display_errors', true);
$json = fopen('/auto_home/fpascual/public_html/SLEW/Website/json/handwritten_relations.json', 'w+');
$js = fopen('json/handwritten_relations.json', 'c+');
fwrite($json, "salut");
fwrite($js, "yo");
fclose($json);
fclose($js);

?>
