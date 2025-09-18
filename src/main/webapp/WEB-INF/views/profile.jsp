<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.userwebapp.model.User" %>
<%
  User user = (User) request.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Your Profile</title>
</head>
<body>
  <h1>Your Profile</h1>

  <% if ("true".equals(request.getParameter("updated"))) { %>
    <p style="color:green">Profile updated successfully.</p>
  <% } %>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
    <p style="color:red"><%= error %></p>
  <% } %>

  <form action="<%= request.getContextPath() %>/profile" method="post">
    <p>
      <strong>Username:</strong>
      <%= user.getUsername() %>
    </p>

    <label>
      Full Name:<br/>
      <input type="text" name="fullName" value="<%= user.getFullName() %>"/>
    </label><br/><br/>

    <label>
      Email:<br/>
      <input type="email" name="email" value="<%= user.getEmail() %>"/>
    </label><br/><br/>

    <label>
      New Password (leave blank to keep current):<br/>
      <input type="password" name="newPassword"/>
    </label><br/><br/>

    <button type="submit">Update Profile</button>
  </form>

  <p>
    <a href="<%= request.getContextPath() %>/logout">Log out</a>
  </p>
</body>
</html>