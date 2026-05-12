# AGENTS.md: Guía Estricta de Desarrollo para GirAPI

## 1. Identidad del Proyecto
* **Nombre:** GirAPI
* **Propósito:** Monolito Modular de e-commerce local.
* **Arquitectura:** Hexagonal Estricta (Ports & Adapters) + Domain-Driven Design (DDD) + CQRS Pattern.
* **Stack:** Java 21, Spring Boot 3.5.x, PostgreSQL, DynamoDB, AWS SNS/SQS (Outbox Pattern), MapStruct, Springdoc OpenAPI, Lombok.

## 2. La Estructura de Directorios (INNEGOCIABLE)
Toda nueva feature debe encajar exactamente en este árbol dentro de su respectivo módulo (ej. `identity`, `orders`, `catalog`). Fíjate especialmente en la estructura de `application`, que sigue un patrón estricto de Commands/Queries/Results.

```text
src/main/java/com/girlocal/girapi/{module}/
├── domain/                               # 🟢 NÚCLEO PURO (Sin dependencias de Spring/Infra)
│   ├── model/                            # Records, POJOs, AggregateRoots (ej. User.java). El estado suele mutar mediante métodos y generar DomainEvents.
│   │   ├── enums/                        # Enumeraciones propias del dominio.
│   │   └── valueobject/                  # Objetos de Valor (Inmutables, identificados por sus atributos).
│   ├── event/                            # Records que implementan DomainEvent (de `shared`) (ej. UserRegisteredEvent).
│   └── exception/                        # DomainException (Reglas de negocio rotas, validaciones internas).
│
├── application/                          # 🟡 ORQUESTACIÓN (Casos de Uso y Contratos)
│   ├── command/                          # Records para entrada de mutación (ej. RegisterUserCommand.java).
│   ├── query/                            # Records para entrada de lectura (ej. GetUserQuery.java).
│   ├── result/                           # Records para salida de los UseCases (ej. UserInfoResult.java).
│   ├── port/in/                          # Interfaces UseCase de entrada (ej. RegisterUserUseCase.java). Único punto de entrada a la aplicación.
│   ├── port/out/                         # Contratos de Salida (Interfaces para hablar con Infra, ej. UserPort.java).
│   └── service/                          # Implementación de los puertos de entrada (Clases con @Service que terminan en Impl, ej. RegisterUserUseCaseImpl.java).
│
└── infrastructure/                       # 🔴 DETALLES TÉCNICOS (Spring, BD, REST, AWS)
    ├── adapter/in/web/                   # Controladores REST (@RestController)
    │   ├── dto/                          # DTOs para Requests y Responses (Records).
    │   └── mapper/                       # Interfaces MapStruct para mapear (Web DTO <-> UseCase Command/Query/Result).
    ├── adapter/out/persistence/postgres/ # Persistencia SQL (PostgreSQL)
    │   ├── adapter/                      # Implementaciones de los `port/out` (ej. UserPersistenceAdapter).
    │   ├── entity/                       # Clases @Entity de JPA.
    │   ├── repository/                   # Spring Data JpaRepository.
    │   └── mapper/                       # Interfaces MapStruct (Domain Model <-> Entity).
    ├── adapter/out/persistence/dynamodb/ # Adaptadores NoSQL (Opcional según módulo).
    └── adapter/out/messaging/            # Adaptadores de Red/Mensajería (ej. SnsPublisherPort, Outbox).
```

### El Módulo `shared` (Excepción de Regla)
El proyecto contiene un módulo especial llamado `shared` (Kernel Compartido).
*   **Propósito:** Contener interfaces base, utilidades transversales, seguridad (JWT/Security Config), manejo global de excepciones (`GlobalExceptionHandler`), configuración de la BD/Outbox y la infraestructura transversal de mensajería.
*   **Uso:** Los demás módulos (`identity`, `orders`, etc.) **SÍ** pueden depender de clases exportadas por `shared` (por ejemplo, `DomainEvent`, `CurrentUserPort`, `SessionValidatorPort`, anotaciones custom, excepciones como `ResourceNotFoundException`, `ValidationException`).

## 3. Reglas de la Arquitectura (La "Ley")
Si una instrucción del usuario rompe alguna de estas reglas, **detente, explica el error y propón la solución correcta.**

1.  **Regla de Dependencia Estricta:**
    *   `domain` **NO DEPENDE DE NADA** (ni de `application`, ni de `infrastructure`, ni de dependencias de Spring como `@Component`, `@Autowired`, ni librerías de terceros excepto quizás validaciones puras de Java).
    *   `application` solo depende de `domain` y librerías transversales estrictamente necesarias. NO sabe si hay REST, BD o AWS.
    *   `infrastructure` depende de `application` y `domain`. Conoce todos los detalles técnicos y librerías externas.
2.  **Muro Anti-Corrupción (Web):** NUNCA expongas un objeto de `domain/model` o un `@Entity` hacia afuera a través de un Controlador REST. Los Controladores SIEMPRE reciben y devuelven `DTOs` (Data Transfer Objects). Usa los mappers (MapStruct) ubicados en `adapter/in/web/mapper` para la traducción entre `DTOs` y los objetos `Command`/`Query`/`Result` de la capa `application`.
3.  **Muro Anti-Corrupción (Persistencia):** Los Casos de Uso (`application/service`) NUNCA acceden a repositorios de Spring Data. Deben usar interfaces definidas en `application/port/out`. El Adaptador en infraestructura (ej. `adapter/out/persistence/postgres/adapter/UserPersistenceAdapter`) inyecta el `JpaRepository`, usa los mappers (MapStruct en `.../postgres/mapper`) para convertir el Modelo de Dominio a un `@Entity` para guardarlo, y viceversa para devolverlo al caso de uso.
4.  **Inyección de Dependencias:** PROHIBIDO usar `@Autowired` en campos (field injection). SIEMPRE usa **inyección por constructor** para componentes gestionados por Spring (`@Service`, `@RestController`, `@Component`, `@Repository`, `Adapter`). La práctica estándar en este proyecto es usar la anotación `@RequiredArgsConstructor` de Lombok y declarar las dependencias inyectables como `private final`.
5.  **Aislamiento de Módulos (Bounded Contexts):** Un módulo funcional (ej. `orders`) **NO PUEDE** importar clases directamente de las carpetas de otro módulo funcional (ej. `identity`). Si necesitan comunicarse o compartir conceptos, se debe hacer mediante:
    *   Eventos de Dominio/Integración (publicados a través del Outbox/Message Broker).
    *   Clases compartidas ubicadas explícitamente en el módulo `shared`.
    *   Llamadas API REST (si estuvieran desplegados separados).
6.  **Manejo de Excepciones:** Usa el `GlobalExceptionHandler` configurado en `shared/infrastructure/adapter/in/web` para capturar excepciones de dominio (`ValidationException`, `ResourceNotFoundException`) y devolver respuestas HTTP 4xx limpias. No ensucies los controladores con try-catch genéricos.

## 4. Patrón Outbox & Manejo de Eventos de Dominio
*   **Origen:** Los eventos representan cosas que ya pasaron. Son Inmutables (`record`) y DEBEN implementar la interfaz `DomainEvent` (ubicada en `shared/domain/event/DomainEvent.java`). Los eventos se crean y acumulan internamente en las clases del modelo de dominio (Agregados).
*   **Prohibición Estricta:** NO uses `@TransactionalEventListener` de Spring.
*   **Flujo:**
    1. El caso de uso llama a un método del Dominio. El Dominio altera su estado y registra el evento internamente.
    2. El caso de uso llama al `Port/Out` para guardar el Agregado.
    3. El `Adapter` de persistencia (ej. JPA) extrae los eventos manualmente del Agregado (ej. `aggregate.pullDomainEvents()`).
    4. El adaptador guarda la entidad JPA y, en la MISMA TRANSACCIÓN SQL, inserta los eventos en una tabla `outbox` mediante el `OutboxPublisherPort` (definido en `shared`).
*   El procesamiento del Outbox es asíncrono (gestionado por infraestructura en `shared`) y envía los mensajes al broker (AWS SNS/SQS).

## 5. Documentación de API (Swagger/OpenAPI)
*   El proyecto utiliza `springdoc-openapi-starter-webmvc-ui`.
*   Toda nueva ruta REST debe estar documentada explícitamente.
*   Usa `@Operation(summary = "...", description = "...")` en los métodos del `@RestController`.
*   Usa `@ApiResponses` y `@ApiResponse` para documentar explícitamente todos los posibles códigos de estado (200, 201, 400, 404, etc.) y los tipos de retorno DTO esperados.

## 6. Proceso de Trabajo (Spec-Driven Workflow)
Al implementar un nuevo requerimiento, el flujo OBLIGATORIO es de adentro hacia afuera:

1.  **Analizar e Identificar:** ¿En qué módulo recae la responsabilidad?
2.  **Modelar el Dominio (`domain`):** Crea/Modifica Entidades, Objetos de Valor (Value Objects), Records, Enumeraciones y Excepciones. Define cómo muta el estado y qué `DomainEvents` se generan. NADA de Spring aquí.
3.  **Definir los DTOs de Aplicación (`application/command`, `application/query`, `application/result`):** Crea los `Records` inmutables que servirán de entrada y salida para el caso de uso. Aquí se aplican validaciones de entrada (`ValidationException`).
4.  **Definir el Contrato de Entrada (`application/port/in`):** Escribe la interfaz del Caso de Uso (ej. `XUseCase.java`) usando los Commands/Queries/Results definidos.
5.  **Definir el Contrato de Salida (`application/port/out`):** Define las interfaces que el caso de uso necesita de la infraestructura (ej. `XPort.java`).
6.  **Implementar la Orquestación (`application/service`):** Crea la clase `XUseCaseImpl` anotada con `@Service` que implementa la interfaz del punto 4, orquestando las llamadas al Dominio y a los Puertos de Salida.
7.  **Implementar la Infraestructura (`infrastructure`):**
    *   **Persistencia:** Crea las `@Entity` JPA, los Repositorios de Spring Data, los mappers de MapStruct (`EntityMapper`), y finalmente la clase `Adapter` que implementa el `Port/Out` y enlaza todo. Asegura el manejo de eventos si aplica.
    *   **Web:** Crea los DTOs de Request/Response, los mappers de MapStruct (`WebMapper` - Mapeando DTOs a Commands/Results) y el `@RestController` documentado con OpenAPI.

*(Asegúrate de revisar y entender estas reglas antes de escribir cualquier línea de código. La consistencia arquitectónica es la máxima prioridad en GirAPI).*