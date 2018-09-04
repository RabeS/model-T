/**
 * The module for replicating bootstrap collapse behaviour.
 * @namespace
 */
 ACC.affixposition = {
	_autoloadTracc : [ 
		"bindBarChecks"
	],

	bindBarChecks : function() {

		var $continueBar = $('.y_continueBar'),
			$sideBar = $('.y_reservationSideBar'),
			$footer = $('.footer-wrapper');

		$("[data-toggle='collapse']:not('.y_fareSlide'), [data-toggle='collapse']:not('#y_upgradeBundleOptionsButton'), .y_amenities-list a").bind('click', function(event) {

			if($footer.length > 0 && $continueBar.length > 0) {
				if($continueBar.hasClass('affix-bottom')) {

					$continueBar
					.removeClass("affix affix-top affix-bottom")
					.removeData("bs.affix")
					.removeAttr('style');

					$footer.css('position','static');

					$continueBar
					.removeAttr('style')
					.css('position','static');					

					$('main').css('margin-bottom', '0');
					$('body').css('margin-bottom', '0');
				}
				
			}
		});		

	}

};
