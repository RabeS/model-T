<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="accommodationReservation" tagdir="/WEB-INF/tags/addons/accommodationaddon/responsive/accommodationreservation"%>
<%@ attribute name="roomStay" required="true" type="de.hybris.platform.commercefacades.accommodation.ReservedRoomStayData"%>
<%@ attribute name="isAmendBookingDetails" required="false" type="java.lang.String"%>
<%@ attribute name="index" required="true" type="java.lang.Integer"%>
<%@ attribute name="b2bOrderDetailsForAccommodation" type="java.lang.Boolean"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="row accommodation-plan">
	<div class="col-xs-12">
		<div class="responsive-table booking-table">
			<table class="table">
				<tbody>
				<tr class="account-orderhistory-table-head responsive-table-head hidden-xs">
					<th class="responsive-table-cell"></th>
					<th class="hidden"></th>
					<th><spring:theme code="text.cms.accommodationbreakdown.total" text="Total" /></th>
				</tr>
				<tr class="responsive-table-item">
					<td class="responsive-table-cell">
						<c:if test="${b2bOrderDetailsForAccommodation}">
							<b>${fn:escapeXml(roomStay.roomTypes[0].name)}</b>
						</c:if>
					</td>
					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.cms.accommodationbreakdown.total" text="Total" /></td>
					<td class="status">${fn:escapeXml(roomStay.baseRate.actualRate.formattedValue)}</td>
				</tr>
				<accommodationReservation:roomServices roomStayServices="${roomStay.services}" roomStayDays="${roomStay.stayDays}"/>
				</tbody>
			</table>
			<span class="cart-total pull-right">
				<c:choose>
					<c:when test="${empty isAmendBookingDetails && !isAmendBookingDetails}">
						<spring:theme code="text.cms.accommodationbreakdown.totalPerRoom" text="Total per room:" />
					</c:when>
					<c:otherwise>
						<spring:theme code="text.cms.accommodationbreakdown.newTotalPerRoom" text="New total per room:" />
					</c:otherwise>
				</c:choose>
				${fn:escapeXml(roomStay.totalRate.actualRate.formattedValue)}
			</span>
		</div>
	</div>
</div>
<c:if test="${(empty isAmendBookingDetails && !isAmendBookingDetails) && !b2bOrderDetailsForAccommodation}">
	<div class="breakdown">
		<div id="accommodation-breakdown_${index}">
			<a role="button" data-toggle="collapse" href="#collapse-breakdown_${index}" aria-expanded="true" aria-controls="collapse-breakdown_${index}" class="">
				<i class="glyphicon glyphicon-info-sign"></i>
				<spring:theme code="text.cms.accommodationbreakdown.fullBreakdown" text="Full breakdown" />
			</a>
		</div>
		<div id="collapse-breakdown_${index}" class="panel-collapse collapse">
			<div class="responsive-table">
				<table class="table">
					<tbody>
					<tr class="account-orderhistory-table-head responsive-table-head hidden-xs inner-table-head">
						<th class="hidden"></th>
						<th><spring:theme code="text.cms.accommodationbreakdown.date" text="Date" /></th>
						<th class="hidden"></th>
						<th><spring:theme code="text.cms.accommodationbreakdown.basePrice" text="Base Price" /></th>
					</tr>
					<c:forEach items="${roomStay.ratePlans[0].roomRates}" var="roomRate">
						<tr class="responsive-table-item">
							<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.cms.accommodationbreakdown.date" text="Date" /></td>
							<td class="responsive-table-cell"><fmt:formatDate value="${roomRate.stayDateRange.startTime}" pattern="dd/MM/yyyy" /></td>
							<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.cms.accommodationbreakdown.basePrice" text="Base Price" /></td>
							<td class="responsive-table-cell">${fn:escapeXml(roomRate.rate.basePrice.formattedValue)}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:if>