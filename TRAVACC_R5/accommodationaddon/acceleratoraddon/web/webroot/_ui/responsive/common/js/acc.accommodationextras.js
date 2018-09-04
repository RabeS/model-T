ACC.accommodationextras = {
	_autoloadTracc : [
		"bindRoomPreferenceSelection"
	],
	previousSelectedRoomPreferenceCode:'',

	bindRoomPreferenceSelection : function() {
		$('.y_booking-extras').on('click', '.y_roomPreference', function() {
			ACC.accommodationextras.previousSelectedRoomPreferenceCode = $(this).val();
		}).on('change', '.y_roomPreference', function(e) {
			var productCode = $(this).val();
			var roomStayReferenceNumber = $(this).data("roomstayrefnum");
			$.when(ACC.accommodationextras.addRoomPreference(roomStayReferenceNumber, productCode)).then(function(success){
                if(success) {
                    ACC.accommodationextras.previousSelectedRoomPreferenceCode = productCode;
                }else{
                    $(this).val(ACC.accommodationextras.previousSelectedRoomPreferenceCode);
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
	}

};