<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="template/localHeader.jsp" %>

<h2>
    <openmrs:message code="oauth2.run.smart"/>
</h2>

<style>
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }

    td, th {
        border: 10px solid #dddddd;
        text-align: left;
        padding: 8px;
    }

    tr:nth-child(even) {
        background-color: #dddddd;
    }
</style>
<form id="runForm" method="post">
    <div class="box">
        <h1><span class="boxHeader">Registered SMART Apps</span></h1>
        <div class="searchWidgetContainer">

            <table>
                <tr>
                    <th>S.No.</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>LaunchUrl</th>
                    <th>Run</th>
                </tr>
                <% int sNo = 0; %>

                <c:forEach items="${smartApps}" var="smartApp" varStatus="i">
                    <% sNo++;%>

                    <tr>
                        <td><c:out value="<%= sNo%>"/></td>
                        <td><c:out value="${smartApp.client.name}"/></td>
                        <td><c:out value="${smartApp.client.description}"/></td>
                        <td>${smartApp.launchUrl}</td>
                        <td><button type="submit" name="run" value="${smartApp.smartId}">Run</button></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
    <br/>
    <br/>
    <!--input type="submit" value="Run"-->
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
