<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Welcome to UserWebApp</title>
  <!-- Use contextPath so CSS loads under any context name -->
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
  <h1>UserWebApp</h1>
  <p>Please choose an option:</p>
  <ul>
    <li>
      <a href="${pageContext.request.contextPath}/login">
        Log In
      </a>
    </li>
    <li>
      <a href="${pageContext.request.contextPath}/register">
        Sign Up
      </a>
    </li>
  </ul>
</body>
</html>