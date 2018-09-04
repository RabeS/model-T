ACC.ancillaryextras = {
	_autoloadTracc : [
		"bindRoomPreferenceSelection",
		"bindSelectUpgradeBundleOptions"
	],
	previousSelectedRoomPreferenceCode:'',

	bindRoomPreferenceSelection : function() {
		$('.y_booking-extras').on('click', '.y_roomPreference', function() {
			ACC.ancillaryextras.previousSelectedRoomPreferenceCode = $(this).val();
		}).on('change', '.y_roomPreference', function(e) {
			var productCode = $(this).val();
			var roomStayReferenceNumber = $(this).data("roomstayrefnum");
			$.when(ACC.ancillaryextras.addRoomPreference(roomStayReferenceNumber, productCode)).then(function(success){
                if(success) {
                    ACC.ancillaryextras.previousSelectedRoomPreferenceCode = productCode;
                }else{
                    $(this).val(ACC.ancillaryextras.previousSelectedRoomPreferenceCode);
                }
			});
		});
	},
	
	addRoomPreference:function(roomStayReferenceNumber, productCode){
		var addToCartResult=false;
        return $.when(ACC.services.addRoomPreference(roomStayReferenceNumber, productCode)).then(
			function(response) {
				var jsonData = JSON.parse(response);
				addToCartResult = jsonData.valid;
				if(!jsonData.valid){
					var output = [];
					jsonData.errors.forEach(function(error) {
						output.push("<p>" + error + "</p>");
					});
					$("#y_addProductToCartErrorModal .y_addProductToCartErrorBody").html(output.join(""));
                    $("#y_addProductToCartErrorModal").dialog('open');
				}
				return addToCartResult;
			});
	},

    bindSelectUpgradeBundleOptions : function() {
        var $continueBar = $('.y_continueBar');

        $('#y_packageUpgradeBundleOptionsButton').on("click", function() {
            if (!$('#y_panel-upgrade').is(":visible")) {
                $("#y_selectUpgradeSpan").hide();
                $("#y_hideUpgradeSpan").show();
                ACC.ancillaryextras.getUpgradeBundleOptions();

            } else {
                $("#y_selectUpgradeSpan").show();
                $("#y_hideUpgradeSpan").hide();
            }
            $continueBar
                .removeClass("affix affix-top affix-bottom")
                .removeData("bs.affix")
                .removeAttr('style');

                ACC.reservation.bindAffixContinueBar();
                $continueBar.affix('checkPosition');
        });
    },

    getUpgradeBundleOptions : function() {
        $.when( ACC.services.getPackageUpgradeBundleOptions()).then(
            function(response){
                if(response.isUpgradeOptionAvailable){
                    var htmlContent = response.htmlContent;
                    $('#y_panel-upgrade').html(htmlContent);
                    ACC.ancillary.bindSelectUpgradeBundle();
                }else{
                    $("#y_noUpgradeAvailableModal").dialog('open');
                    $("#y_selectUpgradeSpan").show();
                    $("#y_hideUpgradeSpan").hide();
                }
            });
    }
};
