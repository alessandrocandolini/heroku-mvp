# heroku-mvp

[![Scala CI](https://github.com/alessandrocandolini/heroku-mvp/actions/workflows/scala.yml/badge.svg)](https://github.com/alessandrocandolini/heroku-mvp/actions/workflows/scala.yml)

Playground project to deploy a sbt-based scala3 server on heroku. I would like to experiment also with sbt-native-image

## Local development 

*Requirements*: `sbt` installed

### Run tests and app

To run unit tests 

```shell
sbt test
```

To run integration tests 

```shell
sbt it:test
```

To run the app through sbt 
```shell
sbt run
```

### Build fat jar 

The project uses `sbt-assembly` plugin to create a "fat" jar
```
sbt assembly
```

The plugin is configured explicitly to run tests on `assembly` task. The generated jar is `heroku-mvp.jar.jar`.

The final jar can be run 
```
java -jar target/scala-3.1.0/heroku-mvp.jar.jar
```

(add other `JAVA_OPS` if needed) 

## Docker

*Requirements*: `docker` up and running. 

For portability, the project is setup to rely directly on `Dockerfile` instead of using sbt plugins like [sbt-docker](https://github.com/marcuslonnberg/sbt-docker).
In the future, we might explore [sbt-native-image](https://github.com/scalameta/sbt-native-image)

To run docker on MAC OS X, the following can be helpful https://github.com/docker/buildx/issues/426#issuecomment-723208580

### Build final image 

[Dockerfile](Dockerfile) contains Docker [multi-stage](https://docs.docker.com/develop/develop-images/multistage-build/) instructions to assembly a lightweight image that only contains the final jar.
Intermediate steps are used to fetch sbt and build the jar. The final image is based on `jre-slim-buster`.

Assuming `docker` is up and running, the image can be built using
```
docker build -t <image tag> --build-arg JAVA_VERSION=11 --build-arg SBT_VERSION=1.6.0 -f ./Dockerfile .
```

### Run final image 

Once the final image is built, we can run it with

```
docker run -p 8080:8080 <image tag>
```

## Curl status endpoint

*Requirements*: `curl` installed

Regardless of whether the app runs locally through sbt, from a fat jar or through docker, we can  check that the service is running fineby using `curl` to call the healthcheck endpoint: 
```
curl http://localhost:8080/status

// {"status":"ok"}
```
