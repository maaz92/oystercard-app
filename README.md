# City Storage Systems Hiring Challenge
## _Simulation of fulfillment of delivery orders for a kitchen_

## How To Run

The application requires JDK 1.8 and JRE 1.8 to run.

Install the dependencies and start start the application

```sh
cd citystoragesystems-challenge
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="2 orders.json"
```
`First argument is ingestion rate and second argument is path of input file`

## What does the application do when the Overflow Shelf is full?
#### Best Candidate:
1. Filters the orders in the overflow shelf for which the shelf with same temperature is vacant.
2. From the filtered orders filters those orders whose order value after maximum pickup time if left in overflow shelf is positive.
3. From the filtered orders filters those orders whose shelf with same temperature has maximum vacancy.
4. From the filtered orders picks the first.
   `The chosen order definitely survives. Also makes use of vacant shelves.So that if a new order of a random temperature comes in then it has the maximum chances of getting a position in the shelf with the same temperature`
#### Second Best Candidate:
1. Filters the orders in the overflow shelf for which the shelf with same temperature is vacant.
2. From the filtered orders filters orders whose order value after maximum pickup time if moved to shelf with the same temperature is positive.
3. From the filtered orders picks the order whose order value after maximum pickup time if moved to shelf with the same temperature is minimum.
   `The chosen order definitely survives. Also gives chance of survival to other orders in the overflow shelf`
#### Third Best Candidate:
1. Filters the orders in the overflow shelf for which the shelf with same temperature is vacant.
2. From the filtered orders filters orders whose order value after minimum pickup time if moved to shelf with the same temperature is positive.
3. From the filtered orders picks the order whose order value after minimum pickup time if moved to shelf with the same temperature is maximum.
   `The chosen order has the maximum chance of surviving`
#### Fourth Best Candidate:
1. Choses any ranodom Order
   `Any order chosen will definitely decay out`