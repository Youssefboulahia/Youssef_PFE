<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="render" uri="http://www.exalead.com/jspapi/render" %>
<%@ taglib prefix="widget" uri="http://www.exalead.com/jspapi/widget" %>

<widget:widget extraCss="div">
	<widget:forEachSubWidget>
		<render:widget />
	</widget:forEachSubWidget>
</widget:widget>