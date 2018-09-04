/**
 * The module for replicating bootstrap collapse behaviour.
 * @namespace
 */
 ACC.collapse = {
	_autoloadTracc : [ 
		"bindCollapse"
	],

	bindCollapse : function() {

		$('body').on('click', "[data-toggle='collapse']:not('.y_fareSlide')", function(e){

			// if the data-toggle element requires it's default behaviour, eg input, radio then apply y_default
			if(!$(this).hasClass('y_default')){
				e.preventDefault();
			}

			var $continueBar = $('.y_continueBar'),
				$sideBar = $('.y_reservationSideBar'),
				$footer = $('.footer-wrapper'),
				dataTarget = $(this).attr('data-target'),
				href = $(this).attr('href');

			if($(this).parents('.modal').length > 0){
				$(dataTarget + ',' + href).slideToggle('slow', function(){

					// if modal content needs to scroll after panel is expanded apply .modal-scroll to .modal
					var scroll = '.modal-scroll',
						$target = $(this).parents(scroll),
						targetHeight = $(this).outerHeight();

					if($(this).parents(scroll).length > 0){
						if($(this).is(':visible')) {
							$target.scrollTop($target.scrollTop() + targetHeight);
						} else {
							$(scroll).scrollTop(0);
						}
					}
				});
			} else {

				ACC.affixposition.bindBarChecks();

				function continueBarAffix() {
	                $continueBar.removeAttr('style');
	                $footer.removeAttr('style');
	                
	                $('body').prop("style").removeProperty("margin-bottom");
	                $('main').prop("style").removeProperty("margin-bottom");

	                ACC.reservation.bindAffixContinueBar();
                    $continueBar.affix('checkPosition');
                    
				}

				$(dataTarget + ',' + href).slideToggle(function() {
					if($('.y_reservationSideBar').length > 0){
						continueBarAffix();
						ACC.reservation.bindStickySideBar();						
					}

					var roomButtonParent = $('.y_roomOptionsCollapse'),
		                flightButtonParent = $('.y_flightOptionsCollapse'),
		                roomOptionsCollapse = $('#roomOptionsCollapse');

	                // text change for room & flights buttons on package details
	                if($(dataTarget).is(':visible') && dataTarget.includes("room")) {
                        roomButtonParent.find('.show-text').addClass('hidden');
                        roomButtonParent.find('.hide-text').removeClass('hidden');
                    } else if($(dataTarget).is(':hidden') && dataTarget.includes("room")) {
                        roomButtonParent.find('.show-text').removeClass('hidden');
                        roomButtonParent.find('.hide-text').addClass('hidden');
                    } else if($(dataTarget).is(':visible') && dataTarget.includes("flight")) {
                        flightButtonParent.find('.show-text').addClass('hidden');
                        flightButtonParent.find('.hide-text').removeClass('hidden');
                    } else if($(dataTarget).is(':hidden') && dataTarget.includes("flight")) {
                        flightButtonParent.find('.show-text').removeClass('hidden');
                        flightButtonParent.find('.hide-text').addClass('hidden');
                    }
				});
				
			}

			$(this).toggleClass('collapsed');
		});
			
	}

};
