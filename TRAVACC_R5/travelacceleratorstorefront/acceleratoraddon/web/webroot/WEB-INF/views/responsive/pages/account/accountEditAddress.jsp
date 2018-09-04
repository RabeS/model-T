<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="panel-heading">
	<h3 class="title title-collapse">
        <c:choose>
            <c:when test="${edit eq true }">
                <spring:theme code="text.account.profile.edit.address.title" text="Edit address"/>
            </c:when>
            <c:otherwise>
                <spring:theme code="text.account.profile.add.address.title" text="Add address"/>
            </c:otherwise>
        </c:choose>
	</h3>
	<button class="show-hide-button" data-toggle="collapse" data-target="#addressForm" aria-expanded="true" aria-controls="addressForm">
		<spring:theme code="text.account.personalDetails.button.collapseExpand" />
	</button>
</div>



<div class="panel-body collapse in" id="addressForm">
    <div class="clearfix">
    <div class="col-xs-12">
    <address:addressFormSelector supportedCountries="${countries}" regions="${regions}" cancelUrl="/my-account/profile" addressBook="true" />
    </div>
    </div>
</div>
