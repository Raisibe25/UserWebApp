<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Register</title>
</head>
<body>
  <h1>Sign Up</h1>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
    <p style="color:red"><%= error %></p>
  <% } %>

  <form action="<%= request.getContextPath() %>/register" method="post">
    <label>
      Username:<br/>
      <input type="text" name="username"
             value="<%= request.getAttribute("username") == null ? "" : request.getAttribute("username") %>"/>
    </label><br/><br/>

    <label>
      Password:<br/>
      <input type="password" name="password"/>
    </label><br/><br/>

    <label>
      Full Name:<br/>
      <input type="text" name="fullName"
             value="<%= request.getAttribute("fullName") == null ? "" : request.getAttribute("fullName") %>"/>
    </label><br/><br/>

    <label>
      Email:<br/>
      <input type="email" name="email"
             value="<%= request.getAttribute("email") == null ? "" : request.getAttribute("email") %>"/>
    </label><br/><br/>

    <button type="submit">Register</button>
  </form>

  <p>
    Already registered?
    <a href="<%= request.getContextPath() %>/login">Log in here</a>.
  </p>
</body>
</html>