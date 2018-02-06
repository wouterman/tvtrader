<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TvTrader</title>
</head>

<!-- Access the bootstrap Css like this,
		Spring boot will handle the resource mapping automatically -->
<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />

</head>
<body>

	<nav class="navbar navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">TvTrader</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Overview</a></li>
					<li><a href="#configuration">Configuration</a></li>
					<li><a href="#console">Console</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">

		<div class="starter-template">
			<h1>Configuration</h1>
			<form:form action="/" method="POST" modelAttribute="Configuration">
				<table>
					<tr>
						<td><h4>Intervals</h4></td>
					</tr>
					<tr>
						<td><form:label path="mailPollingInterval">Mail polling interval:</form:label></td>
						<td><form:input path="mailPollingInterval" /></td>
					</tr>
					<tr>
						<td><form:label path="stoplossInterval">Stoploss Interval:</form:label></td>
						<td><form:input path="stoplossInterval" /></td>
					</tr>
					<tr>
						<td><form:label path="openOrdersInterval">Open Orders Interval:</form:label></td>
						<td><form:input path="openOrdersInterval" /></td>
					</tr>
					<tr>
						<td><form:label path="openOrdersExpirationTime">Open Orders Expiration Time:</form:label></td>
						<td><form:input path="openOrdersExpirationTime" /></td>
					</tr>
					<tr>
						<td><form:label path="tickerRefreshRate">Asset Refresh Rate:</form:label></td>
						<td><form:input path="tickerRefreshRate" /></td>
					</tr>
					<tr>
						<td><form:label path="assetRefreshRate">Ticker Refresh Rate: </form:label></td>
						<td><form:input value="${assetRefreshRate}"
								path="assetRefreshRate" /></td>
					</tr>
					<tr>
						<td><h4>Optionals</h4></td>
					</tr>
					<tr>
						<td><form:label path="expectedSender">Expected sender: </form:label></td>
						<td><form:input path="expectedSender" /></td>
					</tr>
					<tr>
						<td><input type="submit" value="Update" /></td>
					</tr>

				</table>
			</form:form>
		</div>

	</div>

	<script type="text/javascript"
		src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>