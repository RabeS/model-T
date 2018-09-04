ACC.managebookings = {

    _autoloadTracc : [
        "bindManageBookingButton",
        "bindManageBookingModal",
        "bindManageBookingFormSubmit",
        "bindManageBookingsValidation"
    ],

    bindManageBookingButton: function () {
        $(document).ready(function() {
            $(".y_manageBookingSubmit").prop("disabled", false);
        });
    },

    bindManageBookingModal: function() {
        $("#y_manageBookingModal").on("click", ".y_manageBookingSubmit", function(ev) {
            var manageBookingsForm = $("#y_additionalSecurityForm");
            if(!manageBookingsForm.valid()){
                return false;
            }
            ev.preventDefault();
            $.when( ACC.services.manageBookingLogin(manageBookingsForm.serialize())).then(
                // success
                function( data ) {
                    if(data.loginStatus === 'OK') {
                        window.location.href= ACC.config.contextPath + "/manage-booking/booking-details/" + data.bookingReference;
                        return false;
                    }
                    if(data.loginStatus === 'ADDITIONAL_SECURITY' || data.loginStatus === 'ERROR') {
                    	var manageBookingModal = $("#y_manageBookingModal");
                    	manageBookingModal.find('#y_modalContent').html(data.modal);
                    	manageBookingModal.dialog('open');
                        ACC.popover.bindPopover();
                        ACC.managebookings.bindManageBookingModalValidation();
                    }
                }
            );
            return false;
        });
    },

    bindManageBookingFormSubmit: function() {
        $(".y_manageBookingSubmit").click(function (ev) {
            ev.preventDefault();
            var manageBookingsForm = $("#y_manageBookingsForm");
            if(!manageBookingsForm.valid()){
                return false;
            }
            $.when( ACC.services.manageBookingLogin(manageBookingsForm.serialize())).then(
                // success
                function( data ) {
                    if(data.loginStatus === 'OK') {
                        window.location.href= ACC.config.contextPath + "/manage-booking/booking-details/" + data.bookingReference;
                        return false;
                    }
                    if(data.loginStatus === 'ADDITIONAL_SECURITY' || data.loginStatus === 'ERROR') {
                        var manageBookingModal = $("#y_manageBookingModal");
                        manageBookingModal.find('#y_modalContent').html(data.modal);
                        manageBookingModal.dialog('open');
                        ACC.popover.bindPopover();
                        ACC.managebookings.bindManageBookingModalValidation();
                    }
                }
            );
            return false;
        });
    },

    bindManageBookingModalValidation: function() {
        $("#y_additionalSecurityForm").validate({
            errorElement: "span",
            errorClass: "fe-error",
            onfocusout: function(element) { $(element).valid(); },
            rules: {
                passengerReference: {
                    required: true,
                    minlength: 8,
                    maxlength: 11
                }
            }
        });
    },

    bindManageBookingsValidation: function() {
        $("#y_manageBookingsForm").validate({
            errorElement: "span",
            errorClass: "fe-error",
            onfocusout: function(element) { $(element).valid(); },
            rules: {
                bookingReference: "required",
                lastName: "required"
            },
            messages: {
                bookingReference: ACC.addons.travelacceleratorstorefront['error.managemybooking.booking.reference'],
                lastName: ACC.addons.travelacceleratorstorefront['error.managemybooking.last.name']
            }
        });
    }

};
