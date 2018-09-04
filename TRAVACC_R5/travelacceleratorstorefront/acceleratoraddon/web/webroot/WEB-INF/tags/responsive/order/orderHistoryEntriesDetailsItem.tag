<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="orderHistoryEntries" required="true" type="java.util.List"%>
<%@ attribute name="itemHolderTitleKey" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="panel panel-primary panel-list account-list">
	<div class="panel-heading ">
		<h3 class="title">
			<spring:theme code="${itemHolderTitleKey}" />
		</h3>
	</div>
	<div class="panel-body collapse in" id="orderApproval-details">
		<c:choose>
			<c:when test="${not empty orderHistoryEntries}">
				<div class="account-overview-table">
					<div class="responsive-table">
						<table class="table">
							<tbody>
								<tr class="account-orderhistory-table-head responsive-table-head hidden-xs inner-table-head">
									<th><spring:theme code="text.account.order.permission.title" /></th>
									<th><spring:theme code="text.account.order.approver.title" /></th>
									<th><spring:theme code="text.account.order.status.title" /></th>
									<th><spring:theme code="text.account.order.permission.notes" /></th>
								</tr>
								<c:forEach items="${orderHistoryEntries}" var="orderHistoryEntryData">
									<c:forEach items="${orderHistoryEntryData.previousOrderVersionData.b2bPermissionResult}" var="b2bPermissionResult">
										<tr class="responsive-table-item">
											<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.order.permission.title" /></td>
											<td class="responsive-table-cell">${fn:escapeXml(b2bPermissionResult.permissionTypeData.name)}</td>
											<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.order.approver.title" /></td>
											<td class="responsive-table-cell">${fn:escapeXml(orderHistoryEntryData.ownerData.name)}</td>
											<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.order.status.title" /></td>
											<td class="responsive-table-cell"><c:if test="${not empty orderHistoryEntryData.previousOrderVersionData.statusDisplay}">
													<spring:theme code="text.account.order.status.display.${orderHistoryEntryData.previousOrderVersionData.statusDisplay}" />
												</c:if></td>
											<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.order.permission.notes" /></td>
											<td class="responsive-table-cell"><c:choose>
													<c:when test="${empty orderHistoryEntryData.description}">
														<span>
															<spring:theme code="text.notAvailable" />
														</span>
													</c:when>
													<c:otherwise>
                                            ${fn:escapeXml(orderHistoryEntryData.description)}
                                        </c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<span>
					<spring:theme code="text.company.noentries" />
				</span>
			</c:otherwise>
		</c:choose>
	</div>
</div>