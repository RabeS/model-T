ACC.transportmanagebookings = {

    _autoloadTracc : [
        "enableRemoveTravellerButton",
        "bindRemoveTravellerClick",
        "bindRemoveTravellerModalClick",
        "bindRemoveTravellerModalDismiss"
    ],

    bindRemoveTravellerClick: function() {
        $(document).on('click', ".y_removeTraveller", function(event){
            $(".y_removeTraveller").css({ 'pointer-events': 'none'});
            event.preventDefault();
            var travellerUid = $(this).closest(".form-group").find("input[name=travellerUid]").val();
            var orderCode = $("input[name=bookingReference]").val();
            var cancelTravellerUrl = $(this).attr('href');
            if(travellerUid != '' && orderCode != ''){
                $.when( ACC.services.cancelTraveller(orderCode,travellerUid) ).then(
                    function(data) {
                        if (data.isCancelPossible) {
                            $(".y_cancelTravellerConfirm").html(data.cancelTravellerModalHtml);
                            $("#y_cancelTravellerUrl").attr('href', cancelTravellerUrl);
                            ACC.modal.bindModal();
                            $('#y_cancelTravellerModal').dialog('open');
                        } else {
                            $(".y_cancellationResult").removeClass("alert-success");
                            $(".y_cancellationResult").addClass("alert-danger");
                            $(".y_cancellationResult").show();
                            $(".y_cancellationResultContent").html(data.errorMessage);
                            $(".y_cancellationRefundResultContent").html("");
                        }
                    }
                );
            }
			  $('.y_bookingAction').removeClass("disabled");
        });
    },

    enableRemoveTravellerButton : function() {
        $(".y_removeTraveller").css({ 'pointer-events': ''});
    },

    bindRemoveTravellerModalClick : function() {
        $("#y_cancelTravellerUrl").on('click', function(event){
            $(this).attr('disabled', 'disabled');
        });
    },

    bindRemoveTravellerModalDismiss : function() {
        $(document).on('dialogclose', "#y_cancelTravellerModal", function(){
            ACC.transportmanagebookings.enableRemoveTravellerButton();
        });
    }

};
