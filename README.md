# skyeng_post
Test task for skyeng vacancy.

# Перед запуском
Нужно создать БД с названием maildb и пользователя, например, с именем postgres и паролем 123 с правами суперпользователя и can login. 
Далее, нужно создать конфигурацию для приложения.
В правом верхнем углу, в Intellij IDEA, есть меню конфигураций. Нажимаем на кнопку "Edit configurations".
Нажимаем на + и выбираем "Application". В поле Main class выбираем класс MailApp.
В поле Working directory указываем путь от корня до папки skyeng.
В поле Environment variables нужно добавить 4 следующих переменных:
  1) PG_HOST=localhost
  2) PG_PASSWORD=123
  3) PG_PORT=6432
  4) PG_USER=postgres
Нажимаем ОК и запускаем данную конфигурацию.

# Остальное
На сервере стоит Swagger, который на основе кода генерирует API. Посмотреть его можно в браузере, введя URL: http://localhost:8080/swagger-ui/index.html#



