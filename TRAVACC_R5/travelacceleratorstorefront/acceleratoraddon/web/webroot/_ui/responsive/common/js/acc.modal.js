/**
 * The module for replicating bootstrap modal behaviour.
 * @namespace
 */
ACC.modal = {
	_autoloadTracc: [
		"bindResponsive",
		"bindModal"
	],

	schedulerId: 0,

	bindResponsive: function () {
		// add new options with default values
		$.ui.dialog.prototype.options.clickOut = true;
		$.ui.dialog.prototype.options.responsive = true;
		$.ui.dialog.prototype.options.scaleH = 1;
		$.ui.dialog.prototype.options.scaleW = 1;

		// extend _init
		var _init = $.ui.dialog.prototype._init;
		$.ui.dialog.prototype._init = function () {
			var self = this;

			// apply original arguments
			_init.apply(this, arguments);

		};
		// end _init


		// extend open function
		var _open = $.ui.dialog.prototype.open;
		$.ui.dialog.prototype.open = function () {
			var self = this;

			// apply original arguments
			_open.apply(this, arguments);

			// get dialog original size on open
			var oHeight = self.element.parent().outerHeight(),
				oWidth = self.element.parent().outerWidth(),
				isTouch = $("html").hasClass("y_isMobile");

			// responsive width & height
			var resize = function () {

				//check if responsive
				// dependent on modernizr for device detection / html.touch
				if (self.options.responsive === true || (self.options.responsive === "touch" && isTouch)) {
					var elem = self.element,
							wHeight = $(window).height(),
							wWidth = $(window).width(),
							dHeight = elem.parent().outerHeight(),
							dWidth = elem.parent().outerWidth(),
							setHeight = Math.min(wHeight * self.options.scaleH, oHeight),
							setWidth = Math.min(wWidth * self.options.scaleW, oWidth);

					if ((oHeight + 100) > wHeight || elem.hasClass("resizedH")) {
						elem.dialog("option", "height", setHeight).parent().css("max-height", setHeight);
						elem.addClass("resizedH");
					}
					if ((oWidth + 100) > wWidth || elem.hasClass("resizedW")) {
						elem.dialog("option", "width", setWidth).parent().css("max-width", setWidth);
						elem.addClass("resizedW");
					}

					// only recenter & add overflow if dialog has been resized
					if (elem.hasClass("resizedH") || elem.hasClass("resizedW")) {
						elem.dialog("option", "position", "center");
						if (isTouch && $('#y_seatmapModal').length > 0) {
							// prevent dialog window scroll, allowing content inside dialog
							// to scroll as seatpicker dialog fills full mobile screen
							elem.css("overflow", "hidden");
						}
						else {
							elem.css("overflow", "auto");
						}
					}
				}

				// add webkit scrolling to all dialogs for touch devices
				if (isTouch) {
					elem.css("-webkit-overflow-scrolling", "touch");
				}
			};

			// call resize()
			resize();

			// resize on window resize
			$(window).on("resize", function () {
				resize();
			});

			// resize on orientation change
			window.addEventListener("orientationchange", function () {
				resize();
			});
		};
		//end open
	},

	bindModal: function () {
		$('.modal').not('.modal#y_processingModal').dialog({
			width: 'auto',
			autoOpen: false,
			modal: true,
			responsive: true,
			draggable: false,
			resizable: false,
			showTitleBar: false,
			closeOnEscape: false,
			maxHeight: 600,
			minHeight: 'auto',
			scaleW: 1,
			scaleW: 1,

			hide: {effect: "fade", duration: 300},
			collision: "none",
			position: {my: "center top+75", at: "center top", of: window},			

			create: function (event, ui) {
				$(event.target).parent().css('position', 'fixed');
				// remove classes that jQuery UI are adding which is overriding the Bootstrap styling
				$(this).removeClass('ui-widget ui-widget-content');
				$(this).parent().removeClass('ui-widget ui-widget-content ui-corner-all');
				// remove classes end

				// remove jQuery UI dialog titlebar
				$('.ui-dialog-titlebar').remove();

				$(this).removeClass('fade');
			}
		});

		// modal position adjustment for when in ASM view
		if($('#_asm').length > 0){
			$('#y_fullReservationModal').dialog({
				position: {my: "center top-100", at: "center top", of: window}
			});
		}

		$('.seatmap-modal').dialog({
			width: '100%'
		});

		if ($('html').hasClass('y_isMobile')) {
			$('.seatmap-modal').dialog("option", "maxHeight", 'auto');
			$('#y_seatInfoModal').dialog({
				width: 300,
				height: 'auto'
			});
		}

		$('.seatmap-modal').on("dialogopen", function () {
			$('div', this).first().addClass('modal-dialog modal-l');
			$('div div', this).first().addClass('modal-content tab-wrapper');
		});


		$(document).on('click', "[data-dismiss='modal']:not('.y_selectSeatBtn')", function () {
			var modal = $(this).closest('.modal').prop('id');
			$('#' + modal).dialog('close');
			if (!$(this).parents('#y_seatInfoModal').length) {
				$('body').removeClass('modal-open');
			}
		});

		$('#y_processingModal').dialog({
			autoOpen: false,
			draggable: false,
			modal: true,
			resizable: false,
			create: function () {
				$(this).parent().addClass('processing-modal');
				$(this).addClass('in');
				$(this).parent().removeClass('ui-dialog');
				$(this).parent().removeClass('ui-widget ui-widget-content');
				$('.ui-dialog-titlebar').remove();
			}
		});
		
		$(document).on("click", "[data-toggle='modal']", function (e) {
			e.preventDefault();
			var dataTarget = $(this).attr('data-target');
			var dataTargetChar = dataTarget.charAt(0);

			// allows for inclusion or exclusion of # on data-target value
			if (dataTargetChar == "#") {
				$(dataTarget).dialog('open');
			} else {
				$('#' + dataTarget).dialog('open');
			}
			$('body').addClass('modal-open');

			// removes colorbox related divs that are not required
			$('#colorbox, #cboxOverlay').remove();
		});

		// event to close dialog when clicked outside of dialog window.
		// add .y_disableOverlayClose to .modal if modal needs functionality turned off.
		$(document).on("click", ".ui-widget-overlay", function () {
			if(!$('.modal:visible').hasClass('y_disableOverlayClose')){
				$("div:ui-dialog:visible").dialog("close");
				$('body').removeClass('modal-open');
			}
		});

	}
}
