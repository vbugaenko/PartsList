<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib              uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%@ include file="header.jsp" %>

<form action="${pageContext.request.contextPath}/admin/usersList" method="get">
    <input type="hidden" id="idForDelete" name="idForDelete"/>
    <input type="hidden" id="idForUpdate" name="idForUpdate"/>
    <input type="hidden" id="page"        name="page"/>
    <input type="hidden" id="editID"      name="editID" value="${editId}"/>
    <input type="hidden" id="activateID"  name="activateID"/>

    <div class="usersBlocks">
        <table>
            <tr class="tableHeader">
                <td width="30 ">del </td>
                <td>Наименование    </td>
                <td>Необходимость   </td>
                <td>Количество      </td>
            </tr>

            <c:forEach var="part" items="${parts}" varStatus="loopStatus" begin="${beginInt}" end="${endInt}">
                <tr style="<c:if test="${!part.isSelected()}">color: silver;</c:if>
                        background-color: ${loopStatus.index % 2 == 0 ? '#F7F8E0;/>' : '#D8D8D8;/>'}">
                <td align="left" class="whiteBG">
                        <button onclick="document.getElementById('idForDelete').value = '${part.getId()}';"
                                style="padding: 0px;">
                            <img src="https://thumbs.dreamstime.com/b/rood-kruis-12263791.jpg" width="15" height="15">
                        </button>
                    </td>
                    <td align="left">${part.getTitle()} </td>
                    <td align="center">
                        <button type="submit"
                                style="border: 0; ${loopStatus.index % 2 == 0 ? 'background-color: #F7F8E0;/>' : 'background-color: #D8D8D8;/>'}"
                                onclick="document.getElementById('activateID').value = '${part.getId()}';">
                            <c:if test="${part.isSelected()}"> <img
                                    src="https://d30y9cdsu7xlg0.cloudfront.net/png/9014-200.png" width="12"
                                    height="12"> </c:if>
                            <c:if test="${!part.isSelected()}"> <img
                                    src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9FHwUL0aVSEqDHB62nAniEUPx08jAQ6iEc7r8wjrThYz7Ufhf"
                                    width="11" height="11"> </c:if>
                        </button>

                    </td>
                    <td align="center">${part.getAmount()} </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <br/>
    <p>Total: <b>${end}</b></p>

    <div class="usersBlocks">
        <c:forEach var="part" items="${parts}" step="10">
            <c:set var="countB" value="${countB+1}"/>
            <button onclick="document.getElementById('page').value = '${countB}';">${countB}</button>
        </c:forEach>
    </div>
</form>


<%@ include file="footer.jsp" %>
