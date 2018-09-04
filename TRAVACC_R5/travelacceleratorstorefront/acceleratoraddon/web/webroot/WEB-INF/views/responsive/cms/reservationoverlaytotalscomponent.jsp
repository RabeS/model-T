<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/format" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:htmlEscape defaultHtmlEscape="true"/>
<div class="y_reservationOverlayTotalsComponent">
    <input type="hidden" value="${fn:escapeXml(component.uid)}" id="y_reservationOverlayTotalsComponentId"/>
    <c:if test="${not empty reservationTotal}">
        <div class="panel panel-primary panel-list booking room-details clearfix">
            <div class="journey-wrapper">
                <div class="panel-default my-account-secondary-panel total">
                    <div class="panel-body">
                        <div class="col-xs-12">
                            <div class="row">
                                <dl class="totalview ${isAmendedCart ? 'amended-total' : ''} col-xs-12">
                                    <c:if test="${isAmendedCart}">
                                        <div class="col-xs-12 amended-includes">
                                            <div class="col-xs-12">
                                                <dt class="col-xs-9 col-lg-10">
                                                    <div class="row">
                                                        <spring:theme code="reservation.total.pay.label" text="Grand Total"/>
                                                    </div>
                                                </dt>
                                                <dd class="col-xs-3 col-lg-2 text-right">
                                                    <div class="row">
                                                        <format:price priceData="${reservationTotal.wasRate}"/>
                                                    </div>
                                                </dd>
                                            </div>
                                    </c:if>
                                    <div class="col-xs-12">
                                        <dt class="col-xs-9 col-lg-10">
                                            <div class="row">
                                                <spring:theme code="reservation.total.base.price.label" text="Total Base Price"/>
                                            </div>
                                        </dt>
                                        <dd class="col-xs-3 col-lg-2 text-right">
                                            <div class="row">
                                                <format:price priceData="${reservationTotal.basePrice}"/>
                                            </div>
                                        </dd>
                                    </div>
                                        <c:if test="${reservationTotal.totalDiscount.value > 0}">
                                            <div class="col-xs-12">
                                                <dt class="col-xs-9 col-lg-10">
                                                    <div class="row">
                                                        <spring:theme code="reservation.total.discounts.label" text="Total Discounts"/>
                                                    </div>
                                                </dt>
                                                <dd class="col-xs-3 col-lg-2 text-right">
                                                    <div class="row">
                                                        <format:price priceData="${reservationTotal.totalDiscount}"/>
                                                    </div>
                                                </dd>
                                            </div>
                                        </c:if>
                                        <c:if test="${reservationTotal.net}">
                                            <div class="col-xs-12">
                                                <dt class="col-xs-9 col-lg-10">
                                                    <div class="row">
                                                        <spring:theme code="reservation.total.taxes.label" text="Total Taxes"/>
                                                    </div>
                                                </dt>
                                                <dd class="col-xs-3 col-lg-2 text-right">
                                                    <div class="row">
                                                        <format:price priceData="${reservationTotal.totalTax.price}"/>
                                                    </div>
                                                </dd>
                                            </div>
                                        </c:if>
                                        <c:if test="${isAmendedCart}">
                                            </div>
                                        </c:if>
                                        <div class="col-xs-12">
                                            <dt class="col-xs-9 col-lg-10">
                                                <div class="row">
                                                <c:choose>
                                                    <c:when test="${reservationTotal.actualRate.value < 0 }">
                                                        <spring:theme code="reservation.total.refund.label" text="Total to refund"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${isAmendedCart}">
                                                                <spring:theme code="reservation.amendment.total.label" text="Your Change Total for this transaction"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <spring:theme code="reservation.total.pay.label" text="Grand Total"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                                </div>
                                            </dt>
                                            <dd class="col-xs-3 col-lg-2 text-right">
                                                <div class="row">
                                                    <format:price priceData="${reservationTotal.actualRate}"/>
                                                </div>
                                            </dd>
                                        </div>
                                        <form action="#">
                                            <input id="y_reservationCode" type="hidden" value="${fn:escapeXml(reservationCode)}">
                                        </form>
                                    </div>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
