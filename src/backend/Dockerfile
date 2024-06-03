FROM jack666ie/javamaven:21396
RUN mkdir -p /ssadmin-server/source/picture
RUN mkdir -p /ssadmin-server/source/video
WORKDIR /ssadmin-server

COPY ./target/backend-0.0.1-SNAPSHOT.jar app.jar

RUN yum install -y python3

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENV ARGS=""

EXPOSE 28080 9000

# CMD java ${JAVA_OPTS} -jar app.jar $ARGS
# 创建一个启动脚本来同时启动Java应用和Python HTTP服务器
COPY ./start.sh /start.sh
RUN chmod +x /start.sh

CMD ["/start.sh"]