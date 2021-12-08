FROM jenkins/jenkins:2.323
LABEL Author="yryadav95@gmail.com"
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/plugins.txt
ENV JENKINS_USER admin
ENV JENKINS_PASS admin
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
COPY create-user.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY role.groovy /usr/share/jenkins/ref/init.groovy.d/ 
