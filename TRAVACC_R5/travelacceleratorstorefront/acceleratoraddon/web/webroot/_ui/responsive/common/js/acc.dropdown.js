/**
 * The module for replicating bootstrap dropdown behaviour.
 * @namespace
 */
ACC.dropdown = {
	_autoloadTracc : [ 
		"bindDropdown"
	],
	
	schedulerId : 0,

	bindDropdown : function() {
		$('.dropdown-toggle').on('click', function(e){
			e.preventDefault();
			e.stopPropagation();
			$(this).parent().toggleClass('open');
		  	$(this).parent().siblings().removeClass('open');
		});
	}
}
