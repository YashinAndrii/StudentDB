<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Список студентів</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">
            <h1>Список студентів</h1>
            <table class="table">
                <thead>
                <tr>
                    <th>Прізвище</th>
                    <th>Ім'я</th>
                    <th>По-батькові</th>
                    <th>Факультет</th>
                    <th>Кафедра</th>
                    <th>Група</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach items="${students}" var="student">
                        <tr>
                            <td>${student.lname}</td>
                            <td>${student.fname}</td>
                            <td>${student.mname}</td>
                            <td>${student.faculty}</td>
                            <td>${student.department}</td>
                            <td>${student.groupName}</td>
                            <td>
                                <form method="POST" action="${pageContext.request.contextPath}/generate-report?studentId=${student.id}">
                                    <button type="submit" class="btn btn-primary">Щоденник</button>
                                </form>
                            </td>
                            <td><a href="/delete?id=${student.id}" class="btn btn-warning">Видалити</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <a href="/add-student" class="btn btn-success">Додати студента</a>
        </div>
    </body>
</html>