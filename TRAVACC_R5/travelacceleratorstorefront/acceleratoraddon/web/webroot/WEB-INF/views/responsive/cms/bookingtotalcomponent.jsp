<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/format" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<div class="panel panel-primary panel-list booking room-details clearfix">
	<div class="panel-default my-account-secondary-panel total confirmation-total">
		<div class="panel-body">
			<div class="col-xs-12">
				<div class="row">
					<dl class="totalview col-xs-12">
						<div class="col-xs-12">
							<dt class="col-xs-9 col-lg-10">
								<div class="row">
									<spring:theme code="reservation.total.base.price.label" text="Total Base Price"/>
								</div>
							</dt>
							<dd class="col-xs-3 col-lg-2 text-right">
								<div class="row">
									<format:price priceData="${total.basePrice}"/>
								</div>
							</dd>
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
						<c:if test="${not isB2BCustomer}">
                            <c:if test="${not empty partialPaymentPaid && partialPaymentPaid.value >= 0}">
                                <div class="col-xs-12">
                                    <dt class="col-xs-9 col-lg-10">
                                        <div class="row">
                                            <spring:theme code="reservation.partial.pay.label" text="Paid"/>
                                        </div>
                                    </dt>
                                    <dd class="col-xs-3 col-lg-2 text-right">
                                        <div class="row">
                                            <format:price priceData="${partialPaymentPaid}"/>
                                        </div>
                                    </dd>
                                </div>
                            </c:if>
                            <c:if test="${not empty partialPaymentDue && partialPaymentDue.value > 0}">
                                <div class="col-xs-12">
                                    <dt class="col-xs-9 col-lg-10">
                                        <div class="row">
                                            <spring:theme code="reservation.partial.due.label" text="Due"/>
                                        </div>
                                    </dt>
                                    <dd class="col-xs-3 col-lg-2 text-right">
                                        <div class="row">
                                            <format:price priceData="${partialPaymentDue}"/>
                                        </div>
                                    </dd>
                                </div>
                            </c:if>
                        </c:if>
					</dl>
				</div>
			</div>
		</div>
	</div>
</div>


