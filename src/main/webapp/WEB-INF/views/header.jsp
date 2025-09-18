<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav>
  <c:choose>
    <c:when test="${not empty sessionScope.user}">
      Hello, ${sessionScope.user.fullName}! |
      <a href="${pageContext.request.contextPath}/profile">My Profile</a>
      <a href="logout">Logout</a>
      <c:if test="${sessionScope.user.role == 'ADMIN'}">
        | <a href="admin/dashboard">Admin</a>
      </c:if>
    </c:when>
    <c:otherwise>
      <a href="login">Login</a> | <a href="register">Register</a>
    </c:otherwise>
  </c:choose>
</nav>
<hr/>