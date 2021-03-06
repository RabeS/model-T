<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<json:object escapeXml="false">

    <json:property name="valid" value="${addToCartResponse.valid}"/>

    <c:if test="${!addToCartResponse.valid}">
        <json:array name="errors">
            <c:forEach var="error" items="${addToCartResponse.errors}">
                <json:property><spring:theme code="${error}"/></json:property>
            </c:forEach>
            <c:if test="${not empty allowedNumberOfRooms}">
                <json:property>
                    <spring:theme code="accommodation.add.to.cart.quantity.exceeded.allowed.amount"
                                  arguments="${allowedNumberOfRooms}"/>
                </json:property>
            </c:if>
        </json:array>
    </c:if>

</json:object>
