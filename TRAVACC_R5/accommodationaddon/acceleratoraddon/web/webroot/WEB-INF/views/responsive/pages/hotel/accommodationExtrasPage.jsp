<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/template"%>
<%@ taglib prefix="guestdetails" tagdir="/WEB-INF/tags/addons/accommodationaddon/responsive/guestdetails"%>
<%@ taglib prefix="reservation" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/reservation"%>
<%@ taglib prefix="progress" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/progress"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url value="${nextPageURL}" var="nextURL" />

<template:page pageTitle="${pageTitle}">
    <progress:bookingProgressBar stage="extras" amend="${amend}" bookingJourney="${bookingJourney}" />
    <div class="container">
        <h2 class="h2">
            <spring:theme code="text.guest.extras.details.title" text="Add-ons" />
        </h2>
        <div class="row">
                <%-- Begin Main content --%>
            <div class="col-xs-12 col-sm-9 y_nonItineraryContentArea">

                    <%-- AddExtraToCartForm --%>
                <c:url value="/cart/accommodation/add-extra" var="addExtraToCartUrl" />
                <form:form id="y_addExtraToCartForm" name="addExtraToCartForm" action="${addExtraToCartUrl}" method="POST">
                    <input id="y_productCode" name="productCode" type="hidden" />
                    <input id="y_roomStayReferenceNumber" name="roomStayReferenceNumber" type="hidden" />
                    <input id="y_quantity" name="quantity" type="hidden" />
                </form:form>
                    <%-- /AddExtraToCartForm --%>

                    <%-- Guest Extras Details --%>
                <div class="panel panel-primary panel-list room-details">
                    <div class="panel-heading">
                        <h3 class="title">
                            <spring:theme code="text.guest.extras.details.subtitle" text="Add to Booking" />
                        </h3>
                    </div>
                    <div class="panel panel-default my-account-secondary-panel clearfix">
                        <c:forEach var="roomStay" items="${reservationData.roomStays}" varStatus="idx">
                            <c:set var="roomStayReferenceNumber" value="${availableServices[idx.index].roomStayRefNumber}" />
                            <c:if test="${not empty roomStay.roomPreferences and not empty roomStay.roomPreferences[0].code}">
                                <c:set var="roomStayPreferenceCode" value="${roomStay.roomPreferences[0].code}" />
                            </c:if>
                            <div class="room clearfix y_roomStayDetails">
                                <div class="panel-body">
                                    <div class="col-xs-12">
                                        <div class="row">
                                            <section class="panel panel-default">

                                                    <%-- extra services Details --%>
                                                <div class="panel-heading">
                                                    <h3 class="panel-title">
                                                            ${fn:escapeXml(roomStay.roomTypes[0].name)}
                                                    </h3>
                                                </div>
                                                <div class="panel-body collapse in y_booking-extras" id="booking-extras_${fn:escapeXml(roomStayRefNumber)}">
                                                    <ul class="list-group no-padding">
                                                        <c:forEach var="service" items="${availableServices[idx.index].services}" varStatus="serviceIndex">
                                                            <li class="list-group-item">
                                                                <div class="row input-row">
                                                                    <div class="col-xs-6 extra-item">
                                                                        <c:set var="productImageUrl" value="${service.serviceDetails.product.images[0].url}" />
                                                                        <img src="${productImageUrl}">
                                                                        <span>${fn:escapeXml(service.serviceDetails.product.name)}&nbsp;
                                                                            <span class="extra-cost">${fn:escapeXml(service.price.basePrice.formattedValue)}</span>
                                                                        </span>
                                                                    </div>
                                                                    <div class="col-xs-6 extra-item-selection">
                                                                        <c:choose>
                                                                            <c:when test="${service.serviceDetails.restriction.minQuantity == service.serviceDetails.restriction.maxQuantity}">
                                                                                <guestdetails:singleOfferExtra roomStayIndex="${idx.index}" roomStayReferenceNumber="${roomStayReferenceNumber}" service="${service}" index="${serviceIndex.index}" />
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <guestdetails:multipleOfferExtra roomStayIndex="${idx.index}" roomStayReferenceNumber="${roomStayReferenceNumber}" service="${service}" index="${serviceIndex.index}" />
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                        </c:forEach>

                                                        <c:if test="${not empty accommodationRoomPreferenceMap}">
                                                            <c:set var="roomPreferences" value="${accommodationRoomPreferenceMap[idx.index]}" />
                                                            <li class="list-group-item">
                                                                <div class="row input-row">
                                                                    <div class="col-xs-6 extra-item">
                                                                        <span>
                                                                            <spring:theme code="accommodation.room.preference.title" text="Room Preferences" />
                                                                        </span>
                                                                    </div>
                                                                    <div class="col-xs-6 extra-item-selection">
                                                                        <label for="rm_1-extra-item-04" class="sr-only">
                                                                            <spring:theme code="accommodation.room.preference.title" text="Room Preferences" />
                                                                        </label>
                                                                        <select class="form-control room-preferences y_roomPreference" data-roomstayrefnum="${fn:escapeXml(roomStayReferenceNumber)}">
                                                                            <option id="y_roomPreferenceDefaultValue" ${empty roomStayPreferenceCode ? 'selected' : ''} value="${fn:escapeXml(defaultRoomBedPreferenceCode)}">
                                                                                <spring:theme code="accommodation.room.preference.default" text="No Room Preference" />
                                                                            </option>
                                                                            <c:forEach items="${roomPreferences}" var="roomPreference">
                                                                                <c:if test="${not empty roomPreference.value }">
                                                                                    <option value="${fn:escapeXml(roomPreference.code)}" ${not empty roomStayPreferenceCode and roomStayPreferenceCode eq roomPreference.code ? 'selected' :''}>${fn:escapeXml(roomPreference.value)}</option>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                        </c:if>
                                                    </ul>
                                                </div>
                                                    <%-- /extra services Details --%>

                                            </section>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                    <%-- /Guest Extras Details --%>

                    <%-- Continue button --%>
                <div class="row bottom-row">
                    <div class="col-xs-12 col-sm-4 pull-right">
                        <a href="${nextURL}" class="btn btn-secondary col-xs-12 y_extrasContinueButton">
                            <spring:message code="text.ancillary.button.continue" text="Continue" />
                        </a>
                    </div>
                </div>
                    <%-- /Continue button --%>

            </div>
                <%-- End Main content --%>

                <%-- Itinerary --%>
            <div class="col-xs-12 col-sm-3">
                <aside id="sidebar" class="y_reservationSideBar reservation">
                    <div class="main-wrap">
                        <cms:pageSlot position="Reservation" var="feature" element="div">
                            <cms:component component="${feature}" />
                        </cms:pageSlot>
                    </div>
                    <div class="promotions hidden-xs">
                        <cms:pageSlot position="SideContent" var="feature" element="section">
                            <cms:component component="${feature}" />
                        </cms:pageSlot>
                    </div>
                </aside>
            </div>
                <%-- /Itinerary --%>

        </div>
    </div>
    <reservation:fullReservationOverlay />
</template:page>
