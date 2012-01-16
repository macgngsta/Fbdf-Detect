$(document).ready(function() {
		$("#activityTable").tablesorter();
		
		$("#updateButton").click(function(){
			var resultStatus = $("#resultStatus");
			processUpdate(resultStatus);
			return false;
		});
		
		$("#initialLoad").load(function() {
			processUpdate($(this));
		});
	});
	
	$.ajaxSetup ({  
        cache: false  
    }); 
	
    var ajax_load = "<img src='/static/gfxs/ajax-loader.gif' alt='loading...' /> updating...";
    
    function processUpdate(selector)
    {
		//create the graphic
    	selector.html(ajax_load);
		//disable the button
		$(this).disabled = true;
		var loadUrl = "/fb/update";
    	
		$.ajax ({
			url: loadUrl,
			dataType: "text",
			timeout: "20000",
			error: function () {
				selector.html("update failed.");
				return; 
			},
			success: function(data) {
				if(data == "SUCCESS")
				{
					//reload
					location.reload();
				}
				else
				{
					selector.html("update failed.");
					return;
				}
			}, 
			complete: function() {
				//dont care about complete
			}			
		});
    }