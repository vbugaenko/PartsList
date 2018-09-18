<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"              %>
<%@ taglib  uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn"    %>
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"             %>


<%@ include file="header.jsp" %>

<div class="usersBlocks">
<table>
    <tr class="tableHeader">
        <td>id              </td>
        <td>Наименование    </td>
        <td>Тип             </td>
        <td>Количество      </td>
        <td>Цена            </td>
    </tr>

    <c:forEach var="part" items="${parts}" varStatus="loopStatus" begin="${beginInt}" end="${endInt}">
    <tr style="background-color: ${loopStatus.index % 2 == 0 ? '#F7F8E0;/>' : '#D8D8D8;/>'}">
        <td align="right">${part.getId()} </td>
        <td>${part.getTitle()}</td>
        <td>${part.getType()}</td>
    </tr>
    </c:forEach>
</table>
</div>

    <br/>
    <p>Total: <b>${end}</b></p>

    <div class="usersBlocks" >
    <c:forEach var="part" items="${parts}"  step="10">
        <c:set var="countB" value="${countB+1}"/>
        <button onclick="document.getElementById('page').value = '${countB}';">${countB}</button>
    </c:forEach>
    </div>



<%@ include file="footer.jsp" %>
