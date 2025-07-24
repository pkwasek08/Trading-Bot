# Trading-Bot
The project involves the development of an application for automated trading decision-making in the stock market. The application uses statistical indicators to open long or short positions on selected financial instruments. By leveraging historical market data, it enables the simulation of trading strategies and the optimization of input parameters to maximize profitability for specific assets.

This module represents the backend component of the Trading Bot application. It contains the core business logic and all algorithmic implementations responsible for automated trading decisions.

The backend leverages statistical indicators to determine optimal entry points for opening long or short positions on selected financial instruments. By using historical market data, the system can simulate trading strategies and optimize input parameters to maximize profitability for specific assets.

Through a set of RESTful API endpoints, the frontend or external clients can:

* Trigger new trading bot simulations

* Pass custom configuration and strategy parameters

* Access the results of simulations, which are persisted in the database

* Review trade logs, strategy performance, and detailed statistics

All simulation outcomes, including trades, profit/loss summaries, and strategy performance metrics, are stored for later analysis and visualization.

## *Tech Stack*

- *Java 21*
- *Spring Boot 3*
- *Maven 3.6+*
- *Docker*
- *SQL database PostgreSQL*

## *API Endpoints*

| *Method* | *Path*                           | *Description*                       |
| -------- | -------------------------------- | ----------------------------------- |
| *POST*   | *`/api/simulations`*             | *Start a new simulation*            |
| *GET*    | *`/api/simulations`*             | *List all simulations*              |
| *GET*    | *`/api/simulations/{id}`*        | *Get details of a simulation*       |
| *GET*    | *`/api/simulations/{id}/trades`* | *Get trades for a simulation*       |
| *GET*    | *`/api/strategies`*              | *List all available strategies*     |
| *POST*   | *`/api/strategies`*              | *Create a new custom strategy*      |
| *GET*    | *`/api/indicators`*              | *List supported trading indicators* |
| *GET*    | *`/actuator/health`*             | *Health check of the application*   |

## *Configuration*

*Adjust configuration*

```properties
# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/trading
spring.datasource.username=your_user
spring.datasource.password=your_password

# API authentication token
trading.bot.auth.token=YOUR_SECRET_TOKEN
```

## *Running the App*

### *Locally*

```bash
mvn spring-boot:run
```

### *With Docker*

1. *Build the image:*
   ```bash
   docker build -t trading-bot .
   ```
2. *Run container:*
   ```bash
   docker run -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/trading \
     -e SPRING_DATASOURCE_USERNAME=your_user \
     -e SPRING_DATASOURCE_PASSWORD=your_password \
     -e TRADING_BOT_AUTH_TOKEN=YOUR_SECRET_TOKEN \
     trading-bot
   ```

### *Docker Compose*

```bash
docker-compose up -d
```

