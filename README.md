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
