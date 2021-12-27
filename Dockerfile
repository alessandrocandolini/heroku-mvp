
ARG JAVA_VERSION

# BUILDER

FROM openjdk:$JAVA_VERSION-jre-slim-buster as builder

ARG SBT_VERSION

# Install dependencies
RUN apt-get update && apt-get install -y curl bash tar ca-certificates make git procps \
       && rm -rf /var/lib/apt/lists/*

# Install sbt
RUN curl -fsL "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" | tar xfz - -C /opt \
  && ln -s /opt/sbt/bin/sbt /usr/bin/sbt \
  && chmod +x /usr/bin/sbt

# run from non-root folder
RUN mkdir -p /work
WORKDIR /work

# compile
COPY . /work
RUN sbt clean assembly

# RUNNER

FROM openjdk:$JAVA_VERSION-jre-slim-buster 

# create and run as a non-root user:
RUN useradd -m appuser
WORKDIR /home/appuser
USER appuser

COPY --from=builder --chown=appuser /work/target/scala-3.1.0/heroku-mvp.jar app.jar
EXPOSE 8080

CMD ["java","-jar","app.jar"]
