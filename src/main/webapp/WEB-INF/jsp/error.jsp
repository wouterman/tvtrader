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
        <h3>UH OH! ERROR!</h3>
        <table>
            <tr>
                <td><a href="/">Retry</a></td>
            </tr>
        </table>
    </body>
 
</html>