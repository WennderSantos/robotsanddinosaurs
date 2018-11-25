[![CircleCI](https://circleci.com/gh/WennderSantos/robotsanddinosaurs/tree/master.svg?style=svg)](https://circleci.com/gh/WennderSantos/robotsanddinosaurs/tree/master)

# Robots vs Dinosaurs

## Features

- Be able to create an empty simulation space - an empty 50 x 50 grid;
- Be able to create a robot in a certain position and facing direction;
- Be able to create a dinosaur in a certain position;
- Issue instructions to a robot - a robot can turn left, turn right, move forward, move backwards, and attack;
- A robot attack destroys dinosaurs around it (in front, to the left, to the right or behind);
- No need to worry about the dinosaurs - dinosaurs don't move;
- Display the simulation's current state;
- A dinosaur and a robot cannot occupy the same position;
- Attempting to move a robot outside the simulation space is an invalid operation.

## Built With

* [Clojure](https://clojure.org/)
* [Leiningen](https://leiningen.org/)
* [Compojure-api](https://github.com/metosin/compojure-api)
  * Ring for web server
  * Compojure for routes
  * Schema for schema validation
* [Midje](https://github.com/marick/Midje)
* [Cloverage](https://github.com/cloverage/cloverage)
* [Circleci](https://circleci.com/)

## Getting Started

These instructions will get the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install to run this project

* [Java](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
* [Clojure](https://clojure.org/guides/getting_started)
* [Leiningen](https://leiningen.org/)

### Restore dependencies
```
lein deps
```

### Run the application

```
lein run
```
This command should start a server on [http://localhost:8080](http://localhost:8080).

## Running the tests

To run all the tests
```
lein midje
```

## Endpoints
You can find the docs in postman format [here](https://documenter.getpostman.com/view/3275327/RzfaqrD6). This doc also enables you to test the application in an easy way using postman.


## Deploying on Heroku

There is a CI / CD file configuration for this app using circleci and Heroku. You just need to configured a circleci project with the following build variables containing your Heroku's app information:

* $HEROKU_API_KEY
* $HEROKU_APP_NAME

After that, committing this project to you circleci account/project will start the workflow process.

## Technical decisions and further improvements

I decided to use Clojure `atom` to handle the state instead of a database.

As I am not using a database, I decided to save robots/dinosaurs using its id as a key in a map data structure. This gave me the ability to look for an specific robot/dinosaur in constant time.