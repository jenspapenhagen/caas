FROM base/app
ENV PROMETHEUSVERSION 2.4.3
ENV ARCHIVE prometheus-${PROMETHEUSVERSION}.linux-amd64
ENV INSTALL_DIR .
ENV SERVER_HOME ${INSTALL_DIR}/${ARCHIVE}
RUN curl -o ${SERVER_HOME}.tar.gz -L https://github.com/prometheus/prometheus/releases/download/v${PROMETHEUSVERSION}/${ARCHIVE}.tar.gz \
            && tar xvfz ${SERVER_HOME}.tar.gz
WORKDIR ./${ARCHIVE}
COPY prometheus.yml .
ENTRYPOINT ./prometheus --config.file=prometheus.yml