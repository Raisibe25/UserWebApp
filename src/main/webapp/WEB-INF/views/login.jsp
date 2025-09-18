<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login</title>
</head>
<body>
  <h1>Log In</h1>

  <% if ("true".equals(request.getParameter("registered"))) { %>
    <p style="color:green">Registration successful. Please log in.</p>
  <% } %>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
    <p style="color:red"><%= error %></p>
  <% } %>

  <form action="<%= request.getContextPath() %>/login" method="post">
    <label>
      Username:<br/>
      <input type="text" name="username"
             value="<%= request.getAttribute("username") == null
                       ? ""
                       : request.getAttribute("username") %>"/>
    </label><br/><br/>

    <label>
      Password:<br/>
      <input type="password" name="password"/>
    </label><br/><br/>

    <button type="submit">Log In</button>
  </form>

  <p>
    Don’t have an account?
    <a href="<%= request.getContextPath() %>/register">Register here</a>.
  </p>
</body>
</html>