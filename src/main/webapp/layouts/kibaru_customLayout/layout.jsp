<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="config" uri="http://www.exalead.com/jspapi/config" %>
<%@ taglib prefix="render" uri="http://www.exalead.com/jspapi/render" %>
<%@ taglib prefix="widget" uri="http://www.exalead.com/jspapi/widget" %>
<%@ taglib prefix="widget" uri="http://www.exalead.com/jspapi/widget" %>

	<render:import parameters="layout,parentFeed,parentEntry" />
	<render:import parameters="prefix,tableCssClass,wrapperCssClass" ignore="true" />

	<c:set var="widthFormat" value="${layout.widthFormat}" />
	<c:if test="${widthFormat == null || widthFormat == ''}">
		<c:set var="widthFormat" value="%" />
	</c:if>

	<c:set var="rowIdx" value="0" />

	<div class=" table-layout-wrapper<c:if test="${wrapperCssClass != null && wrapperCssClass != ''}"> ${wrapperCssClass}</c:if>">
	<c:forEach var="table" items="${layout.tables}">
		<div class="table-layout<c:if test="${tableCssClass != null && tableCssClass != ''}"> ${tableCssClass}</c:if>"<c:if test="${widthFormat == '%'}"> </c:if>>
		<%-- cols config --%>
		<div>
		<c:forEach var="colConfig" items="${table.colsConfig}">
			
		</c:forEach>
		</div>

		<%-- rows --%>
		<c:forEach var="row" items="${table.rows}">
			<config:getOption var="rowId" name="cssId" component="${row}" defaultValue="${prefix}row_${rowIdx}" />
			<config:getOption var="rowCssClass" name="cssClass" component="${row}" defaultValue="" />
			<div id="${rowId}" class="<c:if test="${rowCssClass != null && rowCssClass != ''}"> ${rowCssClass}</c:if>">
			<c:forEach varStatus="statusCell" var="cell" items="${row.cells}">
				<config:getOption var="cellId" name="cssId" component="${cell}" defaultValue="${rowId}_cell_${statusCell.index}" />
				<config:getOption var="cellCssClass" name="cssClass" component="${cell}" defaultValue="" />
				<config:getOption var="cellValign" name="valign" component="${cell}" defaultValue="top" />
				<config:getOption var="cellAlign" name="align" component="${cell}" defaultValue="left" />
				<div id="${cellId}" colspan="${cell.colSpan}" rowspan="${cell.rowSpan}" class="cell<c:if test="${cellCssClass != null && cellCssClass != ''}"> ${cellCssClass}</c:if>" valign="${cellValign}" align="${cellAlign}">
				<div class="row-wrapper">
				<widget:forEachSubWidget widgetContainer="${cell}" feed="${parentFeed}" entry="${parentEntry}">
					<render:widget />
				</widget:forEachSubWidget>
				</div>
				</div>
			</c:forEach>
			</div>
			<c:set var="rowIdx" value="${rowIdx + 1}" />
		</c:forEach>
		</div>
	</c:forEach>
	</div>