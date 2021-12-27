# heroku-mvp

[![Scala CI](https://github.com/alessandrocandolini/heroku-mvp/actions/workflows/scala.yml/badge.svg)](https://github.com/alessandrocandolini/heroku-mvp/actions/workflows/scala.yml)

Playground project to deploy a sbt-based scala3 server on heroku. I would like to experiment also with sbt-native-image

## Run the tests

```shell
sbt test
```

## Build fat jar 

The project uses `sbt-assembly` to create a "fat" jar
```
sbt assembly
```

The plugin is configured explicitly to run tests on `assembly` task. The generated jar is `heroku-mvp.jar.jar`

## Docker

`Dockerfile` contains Docker [multi-stage](https://docs.docker.com/develop/develop-images/multistage-build/) instructions to assembly a lightweight image that only contains the final jar.
Intermediate steps are used to fetch sbt and build the jar. The final image is based on `jre-slim-buster`.

Assuming `docker` is up and running, the image can be built using
```
docker build -t <image tag> --build-arg JAVA_VERSION=11 --build-arg SBT_VERSION=1.6.0 -f ./Dockerfile .
```

and can later be run using
```
docker run -p 8080:8080 <image tag>
```

For portability, the project is setup to rely directly on `Dockerfile` instead of using sbt plugins like [sbt-docker](https://github.com/marcuslonnberg/sbt-docker).
In the future, we might explore [sbt-native-image](https://github.com/scalameta/sbt-native-image)

To run docker on MAC OS X, the following can be helpful https://github.com/docker/buildx/issues/426#issuecomment-723208580
