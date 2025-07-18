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
