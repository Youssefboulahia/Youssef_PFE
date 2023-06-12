<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="request" uri="http://www.exalead.com/jspapi/request" %>
<%@ taglib prefix="render" uri="http://www.exalead.com/jspapi/render" %>
<%@ taglib prefix="i18n" uri="http://www.exalead.com/jspapi/i18n" %>

<div id="connect" class="searchWidget loadingBox">
		<c:url var="url" value="/login"/>
		<form class="form-signin" action="${url}" method="POST">
		  <div class="text-center mb-4">
		    <img class="mb-4" src="/mashup.supervision/resources/widgets/kibaru_commonRessources/boosted-orange/img/orange_logo.svg" alt="" width="72" height="72">
			<request:getParameterValue var="err" name="err" />
		    <c:if test="${!empty err}">
				<c:choose>
					<c:when test="${err == '2'}"><div class="alert alert-danger" role="alert"><i18n:message code="login.err.2"/></div></c:when>
					<c:when test="${err == '3'}"><div class="alert alert-danger" role="alert"><i18n:message code="login.err.3"/></div></c:when>
					<c:otherwise><div class="alert alert-danger" role="alert"><i18n:message code="login.err.${fn:replace(err, ' ', '_')}" text="${err}"/></div></c:otherwise>
				</c:choose>
		    </c:if>
		  </div>
		
		  <div class="form-group">
		    <input type="text" id="inputEmail" name="login" class="form-control" placeholder="Login" required="" autocomplete="off">
		   
		  </div>
		
		  <div class="form-group">
		    <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required="" autocomplete="off">
		    
		  </div>
		  <c:if test="${!empty continueUrl}">
			<input type="hidden" name="goto" value="${continueUrl}" />
		  </c:if>
		  <c:if test="${empty continueUrl}">
			<input type="hidden" name="goto" value="/page" />
		  </c:if>
		  <button class="btn btn-lg btn-dark btn-block" type="submit">Sign in</button>
		  <render:csrf />
		</form>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$(".alert").show();	
	});
</script>
