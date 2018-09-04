<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="account" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/account"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="panel-heading">
	<h3 class="title title-collapse">
		<spring:theme code="text.account.personalDetails.title" text="My Personal Details" />
	</h3>
	<button class="show-hide-button" type="button" data-toggle="collapse" data-target="#personal-details" aria-expanded="true" aria-controls="personal-details">
		<spring:theme code="text.account.personalDetails.button.collapseExpand" text="Collapse / Expand" />
	</button>
</div>
<div class="panel-body collapse in" id="personal-details">
	<%-- List group --%>
	<ul class="list-group">
		<li class="list-group-item row">
			<div class="col-xs-12 col-sm-8">${fn:escapeXml(title.name)}&nbsp;${fn:escapeXml(customerData.firstName)}&nbsp;${fn:escapeXml(customerData.lastName)}</div>
			<div class="col-xs-12 col-sm-4">
				<a href="update-profile" class="btn btn-primary btn-block">
					<spring:theme code="text.account.profile.updatePersonalDetails" text="Update personal details" />
				</a>
			</div>
		</li>
		<li class="list-group-item row">
			<div class="col-xs-12 col-sm-4">${fn:escapeXml(customerData.displayUid)}</div>
			<div class="col-xs-12 col-sm-4">
				<a class="btn btn-primary btn-block" href="update-email">
					<spring:theme code="text.account.profile.updateEmail" text="Update email" />
				</a>
			</div>	
			<div class="col-xs-12 col-sm-4">
				<a class="btn btn-primary btn-block" href="update-password">
					<spring:theme code="text.account.profile.changePassword" text="Change password" />
				</a>
			</div>		
		</li>
        <li class="list-group-item row">
            <div class="col-xs-12 col-sm-4">
                <dl>
                    <dt><spring:theme code="text.account.profile.homeAddress" text="Home Address" /></dt>
                    <c:if test="${customerData.defaultShippingAddress ne null}">
                    <dd>${fn:escapeXml(customerData.defaultShippingAddress.line1)}</dd>
                    <dd>${fn:escapeXml(customerData.defaultShippingAddress.town)}&nbsp;${fn:escapeXml(customerData.defaultShippingAddress.region.isocodeShort)}</dd>
                    <dd>${fn:escapeXml(customerData.defaultShippingAddress.country.name)}&nbsp;${fn:escapeXml(customerData.defaultShippingAddress.postalCode)}</dd>
                    </c:if>
                </dl>
            </div>
            <c:if test="${customerData.defaultShippingAddress ne null}">
            <div class="col-xs-12 col-sm-4">
                <button class="btn btn-secondary btn-block bottom-align" data-toggle="modal" data-target="remove-homeaddress-modal">
                    <spring:theme code="text.account.profile.homeAddress.remove" text="Remove" />
                </button>
            </div>
            </c:if>
            <c:choose>
                <c:when test="${customerData.defaultShippingAddress eq null}">
                    <div class="col-xs-12 col-sm-4">
                        <a class="btn btn-primary btn-block" href="add-address">
                            <spring:theme code="text.account.profile.homeAddress.add" text="Add home address" />
                        </a>
                    </div>
                </c:when>
                <c:when test="${customerData.defaultShippingAddress ne null}">
                    <div class="col-xs-12 col-sm-4">
                        <a class="btn btn-primary btn-block" href="edit-address/${customerData.defaultShippingAddress.id}">
                            <spring:theme code="text.account.profile.homeAddress.update" text="Update home address" />
                        </a>
                    </div>
                </c:when>
            </c:choose>
        </li>
	</ul>
</div>
<div class="modal fade" id="remove-homeaddress-modal" tabindex="-1" role="dialog" aria-labelledby="remove-homeaddress-modal-title" aria-hidden="true" style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="remove-homeaddress-modal-title">
                    <spring:theme code="text.account.profile.homeaddress.remove.modal.title" text="Remove" />
                </h4>
            </div>
            <div class="modal-body">
                <div class="container">
                    <spring:theme code="text.account.profile.homeaddress.delete.following" text="The following address will be deleted" />
                    <br />
                    <strong>
				        <br>${fn:escapeXml(customerData.defaultShippingAddress.line1)}
		                <br>${fn:escapeXml(customerData.defaultShippingAddress.town)}&nbsp;${fn:escapeXml(customerData.defaultShippingAddress.region.isocodeShort)}
		                <br>${fn:escapeXml(customerData.defaultShippingAddress.country.name)}&nbsp;${fn:escapeXml(customerData.defaultShippingAddress.postalCode)}
		            </strong>
                </div>
            </div>
            <div class="modal-footer">
                <div class="col-xs-12 button-inline">
                    <c:url value="/my-account/remove-account-address" var="removeHomeAddressActionUrl" />
                    <form:form id="removeHomeAddressDetails${fn:escapeXml(customerData.defaultShippingAddress.id)}" action="${removeHomeAddressActionUrl}" method="post">
                        <input type="hidden" name="addressCode" value="${fn:escapeXml(customerData.defaultShippingAddress.id)}" />
                        <div class="col-xs-6">
                            <button type="submit" class="btn btn-primary homeAddressDeleteBtn col-xs-12">
                                <spring:theme code="text.account.profile.homeaddress.delete" text="Delete" />
                            </button>
                        </div>
                        <div class="col-xs-6">
                            <a class="btn btn-secondary homeAddressDeleteBtn col-xs-12" data-payment-id="${fn:escapeXml(customerData.defaultShippingAddress.id)}" data-dismiss="modal">
                                <spring:theme code="text.button.cancel" text="Cancel" />
                            </a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
