# Итоговая работа курса "Java-разработка" на платформе sky.pro (автор - Алексей Гаврин)  
## Цель работы
Написать back-end часть для проекта веб-приложения, имитирующего платформу для продажи различных товаров, реализовав требуемый функционал.  

---
### Функции, реализованные в проекте
+ Регистрация, авторизация и смена пароля пользователей
+ Редактирование профиля пользователя с возможностью добавления аватара
+ Создание, редактирование и удаление объявлений о продаже с описанием и фотографией
+ Добавление к объявлениям комментариев, их редактирование и удаление
+ Поддерживается система доступов пользователей к функционалу приложения на основе ролей (неавторизованный пользователь, авторизованный пользователь с ролью USER, авторизованный пользователь с ролью ADMIN)
+ Все API-методы в приложении реализованы на основе спецификации [openapi.yaml](https://github.com/Aleksey-Gavrin/Graduate-work/blob/dev/openapi.yaml), что обеспечивает полную интеграцию с подготовленной заранее front-end частью проекта  
---
### Проект реализован на основе следующего стека технологий и библиотек
+ Основа - __Java (v.11)__, __Spring (v.2.7.15)__
+ Система сборки - __Maven__
+ База данных - __PostgreSQL (v.16)__
+ Регистрация, авторизвция и аутентификация пользователей - __SpringSecurity__ 
+ Миграция базы данных - __Liquibase__
+ Маппинг и валидация данных - __Hibernate__
+ Тестирование - __JUnit__, __TestRestTemplate__, __Swagger__, __Postman__, __H2 InmemoryDataBase__
+ Вспомогательные библиотеки - __Lombok__
+ Документирование - __JavaDoc__, __SpringDoc__, __Swagger__
+ Для запуска front-end части и проверки взаимодействия использовался __Docker__ - контейнер
---
### Информация для запуска и развертывания приложения  
Для запуска приложения необходимо установить и развернуть базу данных [PostgreSQL](https://www.postgresql.org/download/), а также [Docker](https://docs.docker.com/get-started/get-docker/)
Команда для запуска фронт-энд части проекта с помощью Docker на порту 3000:  
<code>docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21</code>  

---
### Примеры внешнего вида интерфейса приложения и доступных функций:  

_ГЛАВНЫЙ ЭКРАН_
![alt-Главный экран](https://github.com/Aleksey-Gavrin/Graduate-work/blob/dev/demo/Main.png "Главный экран")  

---

_ЭКРАН РЕГИСТРАЦИИ НОВОГО ПОЛЬЗОВАТЕЛЯ_
![alt-Экран регистрации](https://github.com/Aleksey-Gavrin/Graduate-work/blob/dev/demo/Registration.png "Экран регистрации")  

---

_ЭКРАН ПРОФИЛЯ ПОЛЬЗОВАТЕЛЯ_
![alt-Экран профиля](https://github.com/Aleksey-Gavrin/Graduate-work/blob/dev/demo/Profile.png "Экран профиля")  

---

_ЭКРАН ОБЪЯВЛЕНИЯ_
![alt-Экран объявления](https://github.com/Aleksey-Gavrin/Graduate-work/blob/dev/demo/Ad.png "Экран объявления")
