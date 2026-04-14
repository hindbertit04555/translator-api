FROM payara/server-full:6.2024.6

COPY target/translator-api.war $DEPLOY_DIR

EXPOSE 8080