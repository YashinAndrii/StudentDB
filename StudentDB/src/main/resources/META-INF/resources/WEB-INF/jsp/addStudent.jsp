<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Add student</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">
            <h1>Додати студента</h1>
            <form method="POST" action="${pageContext.request.contextPath}/add-student">
                <div class="form-group">
                    <label for="fname">Ім'я:</label>
                    <input type="text" id="fname" name="fname" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="lname">Прізвище:</label>
                    <input type="text" id="lname" name="lname" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="mname">По-батькові:</label>
                    <input type="text" id="mname" name="mname" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="faculty">Факультет:</label>
                    <input type="text" id="faculty" name="faculty" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="department">Кафедра:</label>
                    <input type="text" id="department" name="department" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="groupName">Група:</label>
                    <input type="text" id="groupName" name="groupName" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary">Додати</button>
            </form>
        </div>
    </body>
</html>