    var counter = 1;
    var limit = 10;
    function addInput(divName){
         if (counter == limit)  {
              alert("You have reached the limit of adding " + counter + " inputs");
         }
         else {
              var newdiv = document.createElement('div');
              newdiv.innerHTML = "<span style='display:inline';><table><tr><td class='centerise'>" + (counter + 1) + 
		"</td><td><input type='text' name='x[]'></td><td><input type='text' name='y[]'></td><td><input type='text' name='type[]'></td><td><input type='text' name='predicate[]'></td></tr></table><span>";
              document.getElementById(divName).appendChild(newdiv);
              counter++;
         }
    }
