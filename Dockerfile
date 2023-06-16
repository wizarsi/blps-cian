# Базовый image
FROM openjdk:11
# Переменная, в которой указывается путь к jar-архиву
ARG JAR_FILE=target/blps-cian-0.0.1-SNAPSHOT.jar
# Задаётся рабочая директория, в которой будут выполняться дальнейшие команды (перемещаемся в папку app)
WORKDIR /app
# Jar-файл с локального хоста (путь до него задан в переменной JAR_FILE)
# копируется в папку app, копии задаётся имя blps-cian.jar
COPY ${JAR_FILE} blps-cian.jar
# Команда запуска приложения
ENTRYPOINT ["java","-jar","blps-cian.jar"]