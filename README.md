[![CircleCI](https://circleci.com/gh/WennderSantos/robotsanddinosaurs/tree/master.svg?style=svg)](https://circleci.com/gh/WennderSantos/robotsanddinosaurs/tree/master)

# Robots vs Dinosaurs

## Features required

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
  * Swagger for documentation
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
lein ring server
```
This command should start a server on [localhost:3000](http://localhost:3000) and you will see a web page with a "Hello World!" and a link to the api docs.

## Running the tests

To run all the tests
```
lein midje
```
To run only the handler tests
```
TODO
```
### Code coverage
```
lein cloverage --runner :midje
```

## Api infos

Technical information on how to use and parameter samples are described in swagger format in the following link [api docs](http://localhost:3000/api-docs/index.html). **Aplication must be up and running** You can also see the api tests in `test/robotsandinosaurs/api/handler_test.clj`

A few important conceptual things to point here are:

`200` - will return the current state of simulation space

`400` - for invalid movements (e.g. move outside space or in a coordination already occupied) or invalid parameters

`404` - for a movement or attack with a robot that does not exist in simulation space space

To refer to a robot in a movement or in an attack, use its coordination in space.

In a movement, a robot can move to all its sides (left, right, upwards and backwards). The coordination resulting in a movement will depend on the robot face direction. Following an example:

> A robot in cordination x:10 y:10 with its face looking upwards, when ordered to `turn-left` and start to look to right will result in a robot in coordination x:9 y:10 with its face looking to the right.

The example above is to make clear that all movement instructions `turn-lef`, `turn-rigth`, `move-back` and `move-forward` will result in a robot movement in space.

Valid :movement-instruction are `"turn-left"`, `"turn-right"`, `"move-forward"` or `"move-back"`

Valid :face-direction and :new-face-direction are `"upwards"`, `"backwards"`, `"left"` or `"right"`

Valid coord are from `{:x 0 :y 0}` to `{:x 49 :y 49}`

## Deploying on Heroku

There is a CI / CD configured for this app using circleci and Heroku. You just need to configured a circleci project with the following build variables containing your Heroku's app information:

* $HEROKU_API_KEY
* $HEROKU_APP_NAME

After that, committing this project to you circleci account/project will start the workflow process.

## Technical decisions and further improvements

The project is not using a database. As this is a simple test, I decided to use a Clojure `atom` to handle the state when necessary.

During the movements, the state is not changed, just incremented. Every time a new movement is made in space, a copy of the current space is created containing the new moment. I thought this could be good for gaming log or to show the history of the current game.

The implementation is not using ids to refer to a specific robot or a specific dinosaur. When I started to code the test, I did not see any advantage to use ids. As robots and dinosaurs can't occupy the same position in space and a coordination x:y is unique in space, I just use the coordination when I need to refer to a robot. For dinosaurs is much easier, 'cause I don't ever need to refer to specify one. With this design in mind, I could use a flat data structure to represent robots and dinosaur in a space.

I am also using Clojure `defmulti` when I need to handle an action with both robots and dinosaur. The power of the dispatch function is amazing.

Error messages in the API are not so human readable. I decided to use `schema` for schema validations, this helps me a lot to handle with user inputs, but in the other side, the error messages are not that good. It is something I would put on the backlog to improve.