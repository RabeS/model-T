ACC.reservation = {

	_autoloadTracc: [
		"bindSeeFullReservationButton",
		["bindStickySideBar", $(".y_reservationSideBar").length != 0],
		"bindPageMargin",
		"mobileReservationSlideButton",
		"bindAffixContinueBar",
		"bindWindowResize",
		"bindAsmReservation"
	],

	bindPageMargin: function () {
		if ($('html').hasClass('y_isMobile')) {
			if ($('.y_nonItineraryContentArea').length) {
				var sideBarHeight = $('.y_reservationSideBar').height();
				$('main').css('padding-bottom', sideBarHeight);
			}
		}
	},

	mobileReservationSlideButton: function () {
		if ($('.y_reservationSideBarContent').find('.summary').length == 0) {
			$('.y_fareSlide').addClass('inactive');
		} else {
			$('.y_fareSlide').removeClass('inactive');
		}
	},

	refreshReservationTotalsComponent: function (componentId) {
		// call the web service to get the response HTML
		$.when(ACC.services.refreshReservationTotalsComponent(componentId)).then(
				function (response) {
					$('.y_reservationTotalsComponent').replaceWith(response);
					// refresh the sticky sidebar functionality when anything on the itinerary changes (i.e. the sidebar's height has changed).
					ACC.reservation.checkAffixSidebar();
				}
		);
	},

	refreshAccommodationSummaryComponent: function (componentId) {
		// call the web service to get the response HTML
		$.when(ACC.services.refreshAccommodationSummaryComponent(componentId)).then(
				function (response) {
					$('.y_accommodationSummaryComponent').replaceWith(response);
					// refresh the sticky sidebar functionality when anything on the itinerary changes (i.e. the sidebar's height has changed).
					ACC.reservation.checkAffixSidebar();
				}
		);
	},

	refreshTransportSummaryComponent: function (componentId) {
		// call the web service to get the response HTML
		$.when(ACC.services.refreshTransportSummaryComponent(componentId)).then(
				function (response) {
					$('.y_transportSummaryComponent').replaceWith(response);
					// refresh the sticky sidebar functionality when anything on the itinerary changes (i.e. the sidebar's height has changed).
					ACC.reservation.checkAffixSidebar();
				}
		);
	},

	getTransportReservationComponent: function (componentId) {
		$.when(ACC.services.getTransportReservationComponent(componentId)).then(
				function (response) {
					$('.y_transportReservationComponent').replaceWith(response);
				}
		);
	},

	getAccommodationReservationComponent: function (componentId) {
		$.when(ACC.services.getAccommodationReservationComponent(componentId)).then(
				function (response) {
					$('.y_accommodationReservationComponent').replaceWith(response);
				}
		);
	},

	getReservationOverlayTotalsComponent: function (componentId) {
		return $.when(ACC.services.getReservationOverlayTotalsComponent(componentId)).then(
				function (response) {
					$('.y_reservationOverlayTotalsComponent').replaceWith(response);
				}
		);
	},

	bindSeeFullReservationButton: function () {
		$(document).on("click", ".y_seeFullReservationBtn", function (e) {
			e.preventDefault();
			if ($("#y_transportReservationComponentId").val()) {
				ACC.reservation.getTransportReservationComponent($("#y_transportReservationComponentId").val());
			}
			if ($("#y_accommodationReservationComponentId").val()) {
				ACC.reservation.getAccommodationReservationComponent($("#y_accommodationReservationComponentId").val());
			}
			if ($("#y_reservationOverlayTotalsComponentId").val()) {
				$.when(ACC.reservation.getReservationOverlayTotalsComponent($("#y_reservationOverlayTotalsComponentId").val())).then(function () {
							$("#y_targetReservationCode").text(" " + $("#y_reservationCode").val());
						}
				);
			}
			$("#y_fullReservationModal").dialog('open');
			$('body').addClass('modal-open');
		});
	},

	// Sticky sidebar functionality
	checkAffixSidebar: function () {
		var $sideBar = $(".y_reservationSideBar"),
			$sideBarContent = $(".y_reservationSideBarContent"),
			isDocumentTallEnough = $sideBar.height() + 20 < $(".y_nonItineraryContentArea").height(),
        	isViewportTallEnough = $sideBar.height() + 20 < window.innerHeight;

		// add stickiness
		function addAffixOnSidebar() {
			if ($('html').hasClass('y_isMobile')) {
				if ($('.y_transportSummaryComponent #summary').hasClass('in')) {
					$('#accommodationsummary').addClass('in');
				}
			}

			if (!$('html').hasClass('y_isMobile')) {
				// if viewport and document height are both greater than height of sidebar affix sidebar 
				if (isDocumentTallEnough && isViewportTallEnough) {
					$sideBar.affix({
						offset: {
							top: $sideBar.offset().top,
							bottom: function () {
	                            if($('.y_continueBar').length > 0) {
	                                return $('.footer-wrapper').outerHeight(true) + $('.continue-bar').outerHeight(true);
	                            } else {
	                                return $('.footer-wrapper').outerHeight(true);
	                            }
							}
						}
					});
				}
			} else if ($('html').hasClass('y_isMobile')) {
				$sideBar.affix({
					offset: {
						bottom: $sideBar.offset().bottom,
						bottom: function () {
							return ($('.footer-wrapper').outerHeight(true));
						}
					}
				});
			}

		}

    	addAffixOnSidebar();

		// remove stickiness
		function removeAffixOnSidebar() {
			// delay the removal of affix due to Bootstrap affix taking some time to kick in
			$sideBar
			.removeClass("affix affix-top affix-bottom")
			.removeData("bs.affix");

		}

		var isDocumentTallEnough = $sideBar.height() + 20 < $(".y_nonItineraryContentArea").height(),
			isViewportTallEnough = $sideBar.height() + 20 < window.innerHeight;

		// on page load for fare selection isWindowTallEnough is false as full content in $(".y_nonItineraryContentArea") has not loaded

		// show the sidebar again, when in desktop view
		$sideBarContent.show();
		$sideBar.removeAttr('style');

		// if the sidebar is shorter than the content, make it sticky
		if (isDocumentTallEnough && isViewportTallEnough) {
			addAffixOnSidebar();
			$sideBar.affix('checkPosition');
			$sideBar.affix('checkPosition'); // need to call it twice due to a bug in the bootstrap function
		} else {
			removeAffixOnSidebar();
		}
	},

	// Sticky continuebar functionality only when continuebar is in dom
	bindAffixContinueBar: function () {
		if($('.y_continueBar').length > 0) {
			$('.y_continueBar').affix({
				offset: {
					bottom: function () {
						return ($('.footer-wrapper').height()) + 20;
					}
				}
			});
		}
		
	},

	bindStickySideBar: function () {
		// apply sticky sidebar on load
		ACC.reservation.checkAffixSidebar();

		// on window resize, we apply the sticky sidebar for desktop
		// NOTE: this is run only every half a second to avoid calling the method too many times during resize
		var resizeTimeout;
		$(window).resize(function () {
			clearTimeout(resizeTimeout);
			resizeTimeout = setTimeout(function () {
				ACC.reservation.checkAffixSidebar();
			}, 500);
		});

		if ($('html').hasClass('y_isMobile')) {

			$('body').on('click', '.y_fareSlide', function(){
            
                var target = $(this).data("target");

                if($('.reservation.summary').is(':hidden') && $('.reservation.summary').hasClass('collapse')){                    

                    if($(this).parents('.y_reservationSideBar').hasClass('affix-bottom')){
                        $(target).slideDown(function(){
                            $('.y_reservationSideBar').removeClass("affix affix-top affix-bottom").removeData("bs.affix");
                            ACC.reservation.checkAffixSidebar();
                            $(target).removeClass('collapse');
                        });
                    } else {
                        $(target).slideDown(function(){
                            $(target).removeClass('collapse');
                        });
                    }

                    ACC.reservation.bindPageMargin();
                    
                }

                if($('.reservation.summary').is(':visible') && !$('.reservation.summary').hasClass('collapse')){

                    if($(this).parents('.y_reservationSideBar').hasClass('affix-bottom')){
                        $('.y_reservationSideBar').removeClass("affix affix-top affix-bottom").removeData("bs.affix");
                        ACC.reservation.checkAffixSidebar();
                        $(target).slideUp(function(){
                            $('.y_reservationSideBar').removeClass("affix affix-top affix-bottom").removeData("bs.affix");
                            ACC.reservation.checkAffixSidebar();
                            $(target).addClass('collapse');
                        });
                    } else {
                        $('.y_reservationSideBar').removeClass("affix affix-top affix-bottom").removeData("bs.affix");
                        ACC.reservation.checkAffixSidebar();
                        $(target).slideUp(function(){
                            $(target).addClass('collapse');
                        });
                    }

                }

                
            });

			$('.y_reservationSideBar').on('affix.bs.affix', function () {
				$(this).css('bottom', '');
			});
		}

	},

	bindWindowResize: function () {

		var $continueBar = $('.y_continueBar');

		// on window resize, we apply the sticky continuebar for desktop
		// NOTE: this is run only every half a second to avoid calling the method too many times during resize

		$(window).resize(function () {
			$continueBar.removeClass("affix affix-top affix-bottom").removeData("bs.affix").removeAttr('style');
			ACC.reservation.bindAffixContinueBar();
			$continueBar.affix('checkPosition');
		});

	},

	bindAsmReservation: function () {
		if ($("#y_fullReservationModal").length > 0) {
			$(document).on("click", ".y_asmSeeFullReservation", function (e) {
				e.preventDefault();
				if ($("#y_transportReservationComponentId").val()) {
					ACC.reservation.getTransportReservationComponent($("#y_transportReservationComponentId").val());
				}
				if ($("#y_accommodationReservationComponentId").val()) {
					ACC.reservation.getAccommodationReservationComponent($("#y_accommodationReservationComponentId").val());
				}
				if ($("#y_reservationOverlayTotalsComponentId").val()) {
					ACC.reservation.getReservationOverlayTotalsComponent($("#y_reservationOverlayTotalsComponentId").val());
				}
				$("#y_fullReservationModal").dialog('open');
			});
		}
	}

};
