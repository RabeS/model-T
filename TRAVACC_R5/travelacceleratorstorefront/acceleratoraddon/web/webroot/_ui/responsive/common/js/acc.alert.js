/**
 * The module for replicating bootstrap alert behaviour.
 * @namespace
 */
 ACC.alert = {
	_autoloadTracc : [ 
		"bindAlert"
	],

	bindAlert: function() {

		$("[data-dismiss='alert']").on('click', function(){
		  $(this).parent().fadeOut(200, function(){
		  	$(this).remove();
		  });
		});
			
	}

};
