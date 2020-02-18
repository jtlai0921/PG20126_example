<jsp:useBean id="helper" scope="page" class="com.ctimn.JSPHelper" />
<jsp:setProperty name="helper" property="customerId" />
<%@page import="java.util.*"%>
<?xml version="1.0"?>
<customer id="<%= helper.getCustomerId() %>">
  <demograpics>
    <firstname><%= helper.getFirstName() %></firstname>
    <lastname><%= helper.getLastName() %></lastname>
    <phone><%= helper.getPhone() %></phone>
  </demograpics>
  <%
    Iterator it = helper.getItemsOrdered();
    while (it.hasNext()) {
  %>
      <order><%=it.next()%></order>
  <%
    }
  %>
</customer>