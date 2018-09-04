/**
 * The module for replicating bootstrap popover behaviour.
 * @namespace
 */
ACC.popover = {
	_autoloadTracc : [ 
		"bindPopover"
	],

	bindPopover : function() {

		$('[data-toggle="popover"]').tooltip({
			disabled: true,
			items:"[data-content]",
				content: function () {
			        return $(this).data('content');
			    },
	        	position: {
	        		my: "center top+15",
	        		at: "center",
	        		collision: "fit none"
	        	},
			    create: function(){
			    	$(this).off('mouseenter, mouseover, mouseleave, mousemove, mouseout, hover');
			    },
			    classes: {
					"ui-tooltip": "bottom",
					"ui-tooltip-content": "popover-content"
			    },
			    open: function( event, ui ) {
					$('.ui-tooltip').prepend('<div class="arrow"></div>');
			    }
        });
        
      	$(document).on("click", '[data-toggle="popover"]', function(e){
			e.preventDefault();
			$(this).tooltip('open');
    	});
	}
}