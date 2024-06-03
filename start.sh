docker volume create --name ssadmin-maven-repo


docker run -it --rm --name ssadmin-maven \
    -v ssadmin-maven-repo:/root/.m2 \
    -v "$PWD/src/backend":/usr/src/mymaven \
    -w /usr/src/mymaven \
    jack666ie/javamaven:21396 mvn clean install package -e -Dmaven.test.skip=true

docker compose up -d
