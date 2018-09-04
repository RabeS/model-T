<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="panel-heading">
	<h3 class="title title-collapse">
		<spring:theme code="text.account.close.account.title" />
	</h3>
	<button class="show-hide-button" data-toggle="collapse" data-target="#close-account" aria-expanded="true" aria-controls="close-account">
		<spring:theme code="text.account.close.account.button.collapseExpand" />
	</button>
</div>
<div class="panel-body collapse in" id="close-account" data-consent-management-url="${consentManagementUrl}">
	<jsp:include page="../../../../../responsive/pages/account/accountCloseAccountPage.jsp"/>
</div>