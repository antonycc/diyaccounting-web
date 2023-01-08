FROM tomcat:10-jdk17-openjdk

MAINTAINER DIY Accounting Limited <admin@diyaccounting.co.uk>

ARG WAR_FILE
COPY target/${WAR_FILE} /usr/local/tomcat/webapps/ROOT.war

ENV JAVA_OPTS=""

CMD ["catalina.sh", "run"]
