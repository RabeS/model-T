<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="panel-heading">
	<h3 class="title title-collapse">
		<spring:theme code="text.account.consent.title" />
	</h3>
	<button class="show-hide-button" data-toggle="collapse" data-target="#consent-management-form" aria-expanded="true" aria-controls="consent-management-form">
		<spring:theme code="text.account.consent.button.collapseExpand" />
	</button>
</div>
<div class="panel-body collapse in" id="consent-management-form" data-consent-management-url="${consentManagementUrl}">
	<jsp:include page="../../../../../responsive/pages/account/accountConsentManagementPage.jsp"/>
</div>