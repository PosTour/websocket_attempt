Первый класс: WebSocketConfig с WebSocketConfigurer
<br>Этот класс реализует базовую настройку WebSocket, когда вы работаете напрямую с WebSocket API, без использования STOMP (Simple Text Oriented Messaging Protocol).

Основные моменты:
<br>Аннотация @EnableWebSocket: Включает поддержку WebSocket на уровне Spring.
<br>Интерфейс WebSocketConfigurer: Этот интерфейс предоставляет метод registerWebSocketHandlers(), который используется для регистрации WebSocket обработчиков.

Метод registerWebSocketHandlers():
<br>Здесь регистрируется обработчик tutorialHandler(), который будет обслуживать WebSocket соединения по URL /tutorial.
<br>setAllowedOrigins("*") позволяет запросы с любого домена (разрешает кросс-доменные запросы).

Метод tutorialHandler(): Возвращает экземпляр класса TutorialHandler, который является реализацией интерфейса WebSocketHandler. Этот класс будет управлять жизненным циклом WebSocket соединения, такими как открытие, закрытие и обработка сообщений.

Когда используется:
<br>Этот подход используется, когда требуется прямое взаимодействие с WebSocket протоколом и контроль над обработкой WebSocket сообщений на низком уровне.