<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form"
           uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Obsługa zapomnianego hasła</title>
    <%--<link rel="stylesheet" href="css/style.css" />--%>
    <link rel="stylesheet" href="../../resources/css/style.css" />
</head>
<body>

<%@include file="header.jsp"%>

<section class="login-page">
    <h2>Podaj nowe hasło</h2>
    <form:form method="post"  modelAttribute="userToEditPassword">
        <form:hidden path="id"/>
        <form:hidden path="name"/>
        <form:hidden path="surname"/>
        <form:hidden path="email"/>
        <div class="form-group">
            <form:input type="password" placeholder="Podaj nowe hasło"  path="password"/>
            <form:errors path="password"/>
        </div>
        <div class="form-group">
            <form:input type="password" placeholder="Powtórz hasło"  path="password2"/>
            <form:errors path="password2"/>
        </div>
        <div class="form-group form-group--buttons">
            <button type="submit" class="btn btn--without-border">Wyślij link</button>
        </div>
    </form:form>
</section>

<footer>
    <div class="contact">
        <h2>Skontaktuj się z nami</h2>
        <h3>Formularz kontaktowy</h3>
        <form>
            <div class="form-group form-group--50">
                <input type="text" name="name" placeholder="Imię" />
            </div>
            <div class="form-group form-group--50">
                <input type="text" name="surname" placeholder="Nazwisko" />
            </div>

            <div class="form-group">
                <textarea name="message" placeholder="Wiadomość" rows="1"></textarea>
            </div>

            <button class="btn" type="submit">Wyślij</button>
        </form>
    </div>
    <div class="bottom-line">
        <span class="bottom-line--copy">Copyright &copy; 2018</span>
        <div class="bottom-line--icons">
            <a href="#" class="btn btn--small"><img src="<c:url value="../../resources/images/icon-facebook.svg"/>"/></a>
            <a href="#" class="btn btn--small"><img src="<c:url value="../../resources/images/icon-instagram.svg"/>"/></a>
        </div>
    </div>
</footer>
</body>
</html>
