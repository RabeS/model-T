ACC.tabbedfindercomponent = {

	_autoloadTracc : [ 
	      "bindTabActions",
	      "loadFinder"
	],
	
	actions : {
			"flight" : ACC.config.contextPath + "/view/FareFinderComponentController/load", 
			"hotel" : ACC.config.contextPath + "/view/AccommodationFinderComponentController/load",
			"flight-hotel" : ACC.config.contextPath + "/view/TravelFinderComponentController/load",
			"package" : ACC.config.contextPath + "/view/PackageFinderComponentController/load"
	},
	
	loadFinder: function(){
		if ( $('#y_finderContainer').is('div'))
	    {
			context=ACC.tabbedfindercomponent.actions["flight"];
			ACC.tabbedfindercomponent.refreshTravelFinderComponent(context,"FareFinderComponent");
	    }
  },

  bindTabActions: function(){
	$(".componentSelector").off().on('click', function(e){
	  	e.preventDefault();

		var transition,
		tab = $(this).parent(),
		activeTab = tab.hasClass('active'),
		activeTabSiblings = tab.siblings().hasClass('active'),
		panelContent = $('.fieldset-inner-wrapper'),
		hiddenPanel = panelContent.is(":hidden"),
		visiblePanel = panelContent.is(":visible"),
		switchTabs = tab.addClass('active').siblings().removeClass('active');

		if(!activeTab && !activeTabSiblings && hiddenPanel) {
			transition = "slideDown";
			switchTabs;
		}
		else if(!activeTab && activeTabSiblings && visiblePanel) {
			transition = "switch";
			switchTabs;
		}
		else if(activeTab && !activeTabSiblings && visiblePanel) {
			transition = "slideUp";
			if($(".panel-modify").length > 0){
				tab.toggleClass('active');
			}

		}
	      
		var context=ACC.tabbedfindercomponent.actions[$(this).attr('aria-controls')];
		var selectedComponent = $(this).attr("component-uid");
		$.when(ACC.tabbedfindercomponent.refreshTravelFinderComponent(context,selectedComponent)).then(function() {

			var panelContentParent = $(".panel-modify");

			if(transition === "slideDown"){
				panelContentParent.slideDown(function(){
					panelContentParent.removeClass('collapse');
				});
			}
			else if(transition === "switch"){
				panelContentParent.removeClass('collapse');
			}
			else if(transition === "slideUp") {
				panelContentParent.removeClass('collapse');
				panelContentParent.slideUp(function(){
					panelContentParent.addClass('collapse');
				});
			}

		  });
	  });
  },
  
  refreshTravelFinderComponent: function(context, selectedComponent){
	  if(selectedComponent) {
		  return $.when(ACC.services.refreshTravelFinderComponent(context, selectedComponent)).then(
				  function(response){
					  $('#y_finderContainer').html(response);
					  ACC.tabbedfindercomponent.loadJs();
					  if(selectedComponent=='PackageFinderComponent' ||selectedComponent=='TravelFinderComponent'){
						  ACC.farefinder.minRelativeReturnDate=1;
					  }else{
						  ACC.farefinder.minRelativeReturnDate=0;
					  }
				  }
		  );
	  }
  },
  
  loadJs: function(){
	  ACC.farefinder.init();
	  ACC.accommodationfinder.init();
	  ACC.travelfinder.init();
	  ACC.travelfinder.initializeDatePicker();
  }
  	
};