<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<c:if test="${isPackageInOrder}">
	<div class="panel panel-default total">
		<div class="panel-body">
			<div class="col-xs-12">
				<div class="row">
					<dl class="totalview col-xs-12">
	                    <div class="col-xs-12">
                            <div class="row">
                                <dt class="col-xs-9 col-lg-10">
                                    <spring:theme code="text.cms.booking.transport.total.title" text="Transport Booking Total" />
                                </dt>
                                <dd class="col-xs-3 col-lg-2 text-right">
                                    <format:price priceData="${transportBookingTotalAmount}" />
                                </dd>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="row">
                                <dt class="col-xs-9 col-lg-10">
                                    <spring:theme code="text.cms.booking.accommodation.total.title" text="Accommodation Booking Total" />
                                </dt>
                                <dd class="col-xs-3 col-lg-2 text-right">
                                    <format:price priceData="${accommodationBookingTotalAmount}" />
                                </dd>
                            </div>
                        </div>
						<div class="col-xs-12">
                            <div class="row">
                                <dt class="col-xs-9 col-lg-10">
                                    <spring:theme code="reservation.total.base.price.label" text="Total Base Price"/>
                                </dt>
                                <dd class="col-xs-3 col-lg-2 text-right"><format:price priceData="${total.basePrice}"/></dd>
                            </div>
                        </div>
						<c:if test="${total.totalDiscount.value > 0}">
							<div class="col-xs-12">
	                            <dt class="col-xs-9 col-lg-10">
	                                <div class="row">
										<spring:theme code="reservation.total.discounts.label" text="Total Discounts"/>
									</div>
								</dt>
								<dd class="col-xs-3 col-lg-2 text-right">
		                            <div class="row">
			                            <format:price priceData="${total.totalDiscount}"/>
									</div>
	                            </dd>
							</div>
						</c:if>
						<c:if test="${total.net}">
							<div class="col-xs-12">
	                            <dt class="col-xs-9 col-lg-10">
	                                <div class="row">
										<spring:theme code="reservation.total.taxes.label" text="Total Taxes"/>
									</div>
								</dt>
								<dd class="col-xs-3 col-lg-2 text-right">
		                            <div class="row">
			                            <format:price priceData="${total.totalTax.price}"/>
		                            </div>
	                            </dd>
							</div>
						</c:if>
						<div class="col-xs-12">
                            <dt class="col-xs-9 col-lg-10">
                                <div class="row">
                                    <spring:theme code="reservation.total.pay.label" text="Grand Total"/>
								</div>
							</dt>
							<dd class="col-xs-3 col-lg-2 text-right">
	                            <div class="row">
		                            <format:price priceData="${total.actualRate}"/>
	                            </div>
                            </dd>
						</div>
						<c:if test="${partialPaymentPaid.value >= 0 }">
							<div class="col-xs-12">
	                            <dt class="col-xs-9 col-lg-10">
	                                <div class="row">
										<spring:theme code="reservation.partial.pay.label" text="Paid" />
									</div>
								</dt>
								<dd class="col-xs-3 col-lg-2 text-right">
		                            <div class="row">
										<format:price priceData="${partialPaymentPaid}" />
									</div>
								</dd>
								<c:if test="${notRefundable.value gt 0}">
		                    		<dd class="text-right">
			                    		(<spring:theme code="reservation.not.refundable.paid.label" text="Included not refundable:" /><format:price priceData="${notRefundable}"/>)
		                    		</dd>
		                    	</c:if>
							</div>
						</c:if>
						<c:if test="${partialPaymentDue ne null && partialPaymentDue.value > 0 }">
							<div class="col-xs-12">
	                            <dt class="col-xs-9 col-lg-10">
	                                <div class="row">
										<spring:theme code="reservation.partial.due.label" text="Due" />
									</div>
								</dt>
								<dd class="col-xs-3 col-lg-2 text-right">
		                            <div class="row">
										<format:price priceData="${partialPaymentDue}" />
									<dd>
								</div>
							</div>
						</c:if>
					</dl>
				</div>
			</div>
		</div>
	</div>
</c:if>
