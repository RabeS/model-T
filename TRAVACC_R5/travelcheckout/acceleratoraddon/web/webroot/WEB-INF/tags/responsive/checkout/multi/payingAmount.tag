<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/addons/travelacceleratorstorefront/responsive/format"%>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty paying && paying.value >= 0 }">
	<div class="panel panel-primary panel-list booking room-details clearfix">
		<div class="journey-wrapper">
			<div class="panel-default my-account-secondary-panel total">
				<div class="panel-body">
					<div class="col-xs-12">
						<div class="row">
							<dl class="clearfix">
								<div class="col-xs-12">
									<dt class="col-xs-9 col-lg-10">
										<spring:theme code="reservation.partial.paying.label" text="Paying" />
									</dt>
									<dd class="col-xs-3 col-lg-2 text-right">
										<format:price priceData="${paying}" />
									</dd>
								</div>
							</dl>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</c:if>
