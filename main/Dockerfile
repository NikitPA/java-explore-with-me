FROM --platform=linux/amd64 amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
ENV SPRING_DATASOURCE_URL=SPRING_DATASOURCE_URL \
                            POSTGRES_USER=POSTGRES_USER \
                            POSTGRES_PASSWORD=POSTGRES_PASSWORD \
                            STATS_URL=STATS_URL
ENTRYPOINT java -DSPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
                -DPOSTGRES_USER=${POSTGRES_USER} \
                -DPOSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
                -DSTATS_URL=${STATS_URL} \
                -jar app.jar