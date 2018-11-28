FROM base/wildfly
LABEL maintainer="Jens Papenhagen <jens.papenhagen@web.de>" description="Catalog as a Service Mircoservice for an ITECH Project 5 base on pure JavaEE "
ADD target/caas.war ${DEPLOYMENT_DIR}
#exposing only the port for the app
EXPOSE 8080