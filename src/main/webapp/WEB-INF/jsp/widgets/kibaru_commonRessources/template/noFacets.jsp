<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="render" uri="http://www.exalead.com/jspapi/render" %>
<%@ taglib prefix="config" uri="http://www.exalead.com/jspapi/config" %>
<%@ taglib prefix="search" uri="http://www.exalead.com/jspapi/search" %>
<%@ taglib prefix="i18n" uri="http://www.exalead.com/jspapi/i18n" %>

<config:getOption var="facetId" name="facetId"  defaultValue="" />
<config:getOption var="facetsList" name="facetsList"  defaultValue="" />
<div class="nofacets">
		<p class="alert alert-facet-info ">
			<c:if test="${not empty facetId}">
				<strong>Pas de '<i18n:message code="facet_${facetId}"/>' associée à ce client</strong>
			</c:if>
			<c:if test="${not empty facetsList}">
				<c:forEach var="facetId" items="${facetsList}">
					<strong>Pas de '<i18n:message code="facet_${facetId}"/>' associée à ce client</strong>
				</c:forEach>
			</c:if>
			<c:if test="${empty facetId && empty facetsList}">
				<strong>Pas de contrats associée à ce client</strong>
			</c:if>
		</p>						
</div>
