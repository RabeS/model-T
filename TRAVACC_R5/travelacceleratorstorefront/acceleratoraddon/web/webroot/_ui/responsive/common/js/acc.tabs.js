/**
 * The module for replicating bootstrap tabs behaviour.
 */

ACC.tabs = {

	_autoloadTracc : [ 
	    "bindTabActions",
	],
	
	bindTabActions: function(){

		$(document).on('click', '.nav-tabs:not(.modify-travel) a', function(e) {
			if(!$(this).parent().hasClass('y_tab')){
				e.preventDefault();
				var currentTab = $(this).parent();
				var currentTabId = $(this).attr('href');
				var currentDiv = $(currentTabId);
				currentTab
				.addClass('active')
				.siblings()
				.removeClass('active');
				currentDiv.addClass('active in')
				.siblings()
				.removeClass('active in');
			}
	  	});
	}
}