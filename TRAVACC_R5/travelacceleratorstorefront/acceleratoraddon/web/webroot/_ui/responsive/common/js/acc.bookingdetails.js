ACC.bookingdetails = {

	_autoloadTracc: [
		"bindCancelBookingButton",
		"bindCancelTransportBookingButton",
		"bindCancelAccommodationBookingButton",
		"bindConfirmCancelBookingButton",
		"enableCancelButtons",
		"bindAddRoomButton",
		"bindRoomQuantity",
		"showHideOptionsInMobileView",
		"bindEmptyAncillaryInfoRemoval"
	],

	sendCancelBookingRequest: function (cancelRequestUrl, cancelBookingUrl) {
		var orderCode = $("#bookingReference").val();
		$.when(ACC.services.cancelBookingRequest(cancelRequestUrl, orderCode)).then(
			function (data) {
				var jsonData = JSON.parse(data);
				if (jsonData.isCancelPossible) {
					$(".y_cancelBookingConfirm").html(jsonData.cancelBookingModalHtml);
					$("#y_cancelBookingUrl").attr('href', cancelBookingUrl);
					ACC.modal.bindModal();
					$("#y_cancelBookingModal").dialog('open');

					// removes modal content from an Ajax call from the dom to avoid duplicated modal.
					$("[data-dismiss='modal']").bind('click', function(){
					    $('#y_cancelBookingModal').remove();
					});
				} else {
					$(".y_cancellationResult").removeClass("alert-success");
					$(".y_cancellationResult").addClass("alert-danger");
					$(".y_cancellationResult").show();
					$(".y_cancellationResultContent").html(jsonData.errorMessage)
					$(".y_cancellationRefundResultContent").html("");
				}
				$(".y_bookingAction").removeClass("disabled");
			});
	},

	bindCancelBookingButton: function () {
		$(document).on('click', '.y_cancelBookingButton', function (event) {
			event.preventDefault();
			var cancelBookingUrl = $(this).attr('href');
			ACC.bookingdetails.sendCancelBookingRequest("/manage-booking/cancel-booking-request/", cancelBookingUrl);
		});
	},

	bindCancelTransportBookingButton: function () {
		$(document).on('click', '.y_cancelTransportBookingButton', function (event) {
			event.preventDefault();
			var cancelBookingUrl = $(this).attr('href');
			ACC.bookingdetails.sendCancelBookingRequest("/manage-booking/cancel-transport-order-request/", cancelBookingUrl);
		});
	},

	bindCancelAccommodationBookingButton: function () {
		$(document).on('click', '.y_cancelAccommodationBookingButton', function (event) {
			event.preventDefault();
			var cancelBookingUrl = $(this).attr('href');
			ACC.bookingdetails.sendCancelBookingRequest("/manage-booking/cancel-accommodation-order-request/", cancelBookingUrl);
		});
	},

	bindConfirmCancelBookingButton: function () {
		$(document).on('click', '#y_cancelBookingUrl', function (event) {
			$(this).attr('disabled', 'disabled');
		});
	},

	bindAddRoomButton: function () {
		$('.y_addRoom').on("click", function () {
			var orderCode = $("#bookingReference").val();
			$.when(ACC.services.getAddRoomOptions(orderCode)).then(
					function (response) {
						if (response.valid) {
							$('#y_addRoomModal').html(response.addAccommodationRoomHtmlContent);
							ACC.bookingdetails.bindRoomQuantity();
							ACC.bookingdetails.bindAddRoomContinueButton();
							$('#y_addRoomModal').dialog();
						} else {
							$("#y_addRoomToPackageErrorBody").html(response.addAccommodationRoomHtmlContent);
							$("#y_addRoomToPackageErrorModal").dialog();
						}
					}
			);
			return false;
		});
	},

	bindRoomQuantity: function () {
		var roomId = '#room';
		var hide = 'hidden';
		$('.y_accommodationRoomQuantity').change(function () {
			var roomQuantity = $(this).val(), i;
			for (i = 1; i <= roomQuantity; i++) {
				$(roomId + '\\[' + i + '\\]').removeClass(hide);
			}
			$(roomId + '\\[' + (i - 1) + '\\]').nextUntil('.y_roomStayCandidatesError', '.guest-types').addClass(hide);
		});
	},

	enableCancelButtons: function () {
		$(".y_bookingAction").removeClass("disabled");

		$(".y_bookingAction").on("click", function () {
			$(".y_bookingAction").addClass("disabled");
		});
	},

	bindAddRoomContinueButton: function () {
		$('#y_addRoomContinue').on("click", function () {
			var $form = $("#y_addRoomForm");
			var orderCode = $("#bookingReference").val();
			$.when(ACC.services.validateAddRoomAccommodationFinderForm($form.serialize(), orderCode)).then(
					function (response) {
						if (response.valid) {
							$form.submit();
						} else {
							$("#y_roomStayCandidatesError").html(response.errorMsg);
							$("#y_roomStayCandidatesError").show();
						}
					}
			);
		});
	},

	showHideOptionsInMobileView: function () {
		if ($('.y_options').is('div')) {
			$('.y_options a').each(function (index) {

				var options = $(this).parents('.additional-information').siblings('.options');

				$(this).on('click', function (e) {
					e.preventDefault;
					if (options.is(':visible')) {
						options.hide('fast');
						$(this).parents('.group-traveller').removeClass('selected');
						$('.y_down', this).removeClass('hidden');
						$('.y_up', this).addClass('hidden');
					} else if (options.is(':hidden')) {
						options.show('fast');
						$(this).parents('.group-traveller').addClass('selected');
						$('.y_down', this).addClass('hidden');
						$('.y_up', this).removeClass('hidden');
					}
				});
			});
		}
	},

	// function to remove empty <ul> from the DOM as no viable backend or CSS solution available
	bindEmptyAncillaryInfoRemoval: function () {
		$('.y_additionalInformationModal').on('dialogopen', function () {
			$('.traveller-details ul', this).each(function () {
				if (!$(this).has('li').length) {
					$(this).remove();
				}
			});
		})
	}
};
