<?php 
error_reporting(E_ALL);
session_start();
$content = "\n\t[\n\t\t";
foreach($_POST['x'] as $k => $v){
	++$i;
}
for($o = 0; $o < $i; $o++) {

	$content .= '{'."\n\t\t\t".'"article_name":"';
	$content .= $_POST["article"]."\",";
	$content .= "\n\t\t\t".'"x":"';
	$content .= $_POST["x"][$o] . '",';
	$content .= "\n\t\t\t"."\"y\":\""; 
	$content .= $_POST["y"][$o] . '",';
	$content .= "\n\t\t\t"."\"type\":\""; 
	$content .= $_POST['type'][$o] . '",';
	$content .= "\n\t\t\t"."\"predicate\":\""; 
	if($o == $i-1) { $content .= $_POST['predicate'][$o] . "\"\n\t\t}\n\t]"; }
	else { 	$content .= $_POST["predicate"][$o] . "\"\n\t\t},\n\n\t"; }

	}
echo $content;
$json = fopen('/auto_home/fpascual/public_html/SLEW/Website/json/handwritten_relations.json', 'w+');
$js = fopen('json/handwritten_relations.json', 'c+');
fwrite($json, $content);
fwrite($js, $content);
fclose($json);
fclose($js);

/*function writejson($json, $string) {

	 for ($written = 0; $written < strlen($string); $written += $fwrite) {
        $fwrite = fwrite($json, substr($string, $written));
        if ($fwrite === false) {
            return $fwrite;
        }
    }
    return $written;
}*/



/*
$_SESSION['article_name'] = $arg;
header("Location: manual_injecting.php", true, 301); */

?>

