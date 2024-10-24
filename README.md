Зависимости:
<br>MemberStore memberStore: Объект, который хранит список текущих участников чата. В нем хранятся пользователи, которые присоединились к чату.
<br>SimpMessagingTemplate simpMessagingTemplate: Шаблон для отправки сообщений через WebSocket с использованием STOMP. Это основной способ отправки сообщений клиентам.

Метод getUsers(@MessageMapping("/user"))
<br>Аннотация @MessageMapping("/user"): Этот метод обрабатывает WebSocket сообщения, отправленные на маршрут /app/user (из-за префикса /app, установленного в конфигурации WebSocket).

Параметры:
<br>User user: Пользователь, отправивший сообщение, представленный в виде объекта User.
<br>SimpMessageHeaderAccessor headerAccessor: Объект для работы с заголовками WebSocket-сессий, который предоставляет доступ к атрибутам сессии.

Функционал:
<br>Создается новый объект пользователя на основе полученного в сообщении.
<br>Пользователь сохраняется в сессионные атрибуты WebSocket соединения и добавляется в хранилище участников чата (memberStore).
<br>Если в хранилище есть участники, они отправляются всем подписанным пользователям через канал /topic/users.
<br>Отправляется уведомление о том, что новый пользователь присоединился к чату, через канал /topic/messages.
  
Методы для обработки событий подключения и отключения:

Метод handleSessionConnectEvent(@EventListener)
<br>Аннотация @EventListener: Метод слушает события подключения к WebSocket. В данном случае он просто выводит сообщение в консоль, когда происходит подключение клиента.
<br>SessionConnectEvent event: Событие, которое возникает при подключении новой сессии.

Метод handleSessionDisconnectEvent(@EventListener)
<br>SessionDisconnectEvent event: Событие, которое возникает при отключении сессии.
<br>Логика:
<br>Получение заголовков и атрибутов сессии из события.
<br>Проверка, существует ли сессионный атрибут пользователя.
<br>Если пользователь найден, он удаляется из хранилища участников (memberStore).
<br>Обновленный список пользователей отправляется через канал /topic/users.
<br>Отправляется сообщение о том, что пользователь покинул чат через канал /topic/messages.

Использование STOMP и WebSocket
<br>В этой архитектуре сообщения обрабатываются через WebSocket с помощью STOMP, что упрощает взаимодействие между клиентами и сервером.
   
Каналы:
<br>/topic/users: Канал, по которому отправляются обновления о текущих участниках чата.
<br>/topic/messages: Канал, по которому отправляются сообщения о действиях пользователей (например, присоединение или выход из чата).
   
Основные моменты:
<br>STOMP маршруты и каналы: Используются для маршрутизации сообщений к клиентам.
<br>Асинхронные события: Используются для отслеживания подключения и отключения пользователей в реальном времени.
<br>Шаблон сообщений (SimpMessagingTemplate): Упрощает отправку сообщений клиентам по WebSocket.