<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/format" %>
<%@ attribute name="amend" required="false" type="java.lang.Boolean" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="panel panel-primary panel-list booking room-details clearfix">
	<div class="journey-wrapper">
		<div class="panel-default my-account-secondary-panel total">
			<div class="panel-body">
				<div class="col-xs-12">
					<div class="row">
						<dl class="totalview ${amend ? 'amended-total' : ''} col-xs-12">
                            <c:if test="${amend}">
                                <div class="col-xs-12 amended-includes">
                                    <div class="col-xs-12">
                                        <dt class="col-xs-9 col-lg-10">
                                            <div class="row">
                                                <spring:theme code="reservation.total.pay.label" text="Grand Total"/>
                                            </div>
                                        </dt>
                                        <dd class="col-xs-3 col-lg-2 text-right">
                                            <div class="row">
                                                <format:price priceData="${cartTotal.wasRate}"/>
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
										<format:price priceData="${cartTotal.basePrice}"/>
									</div>
								</dd>
							</div>
							<c:if test="${cartTotal.totalDiscount.value > 0}">
								<div class="col-xs-12">
									<dt class="col-xs-9 col-lg-10">
										<div class="row">
											<spring:theme code="reservation.total.discounts.label" text="Total Discounts"/>
										</div>
									</dt>
									<dd class="col-xs-3 col-lg-2 text-right">
										<div class="row">
											<format:price priceData="${cartTotal.totalDiscount}"/>
										</div>
									</dd>
								</div>
							</c:if>
							<c:if test="${cartTotal.net}">
								<div class="col-xs-12">
									<dt class="col-xs-9 col-lg-10">
										<div class="row">
											<spring:theme code="reservation.total.taxes.label" text="Total Taxes"/>
										</div>
									</dt>
									<dd class="col-xs-3 col-lg-2 text-right">
										<div class="row">
											<format:price priceData="${cartTotal.totalTax.price}"/>
										</div>
									</dd>
								</div>
							</c:if>
							<c:if test="${amend}">
                                </div>
                            </c:if>
							<div class="col-xs-12">
								<dt class="col-xs-9 col-lg-10">
									<div class="row">
                                        <c:choose>
                                            <c:when test="${amend}">
                                                <spring:theme code="reservation.amendment.total.label" text="Your Change Total for this transaction"/>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:theme code="reservation.total.pay.label" text="Grand Total"/>
                                            </c:otherwise>
                                        </c:choose>
									</div>
								</dt>
								<dd class="col-xs-3 col-lg-2 text-right">
									<div class="row">
										<format:price priceData="${cartTotal.actualRate}"/>
									</div>
								</dd>
							</div>
						</dl>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

