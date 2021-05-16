# EmpikPalindrome
Simple spring boot app that determines whether a given string is a palindrome.

# Windows

## Stage 2
Download Jenkins and run as a docker image on your local machine. Write step by step how to do it.
It is assumed that Docker is already installed on a development machine and Docker CLI is available. As a first step, create a bridge network:
``` docker network create jenkins ```

It will be necessary to run Docker commands inside Jenkins container, so it is required to download and run Docker daemon:
```
docker run --name jenkins-empik --rm --detach --network jenkins --env DOCKER_HOST=tcp://docker:2376 --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 --volume jenkins-data:/var/jenkins_home --volume jenkins-docker-certs:/certs/client:ro --volume "%HOMEDRIVE%%HOMEPATH%":/home --publish 8080:8080 --publish 50000:50000 myjenkins-empik:1.1
```

In the next step a customised version of Jenkins Docker image will be run. To do so, create Dockerfile with the following content:
```
FROM jenkins/jenkins:2.277.4-lts-jdk11
USER root
RUN apt-get update && apt-get install -y apt-transport-https \
       ca-certificates curl gnupg2 \
       software-properties-common
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) stable"
RUN apt-get update && apt-get install -y docker-ce-cli
RUN usermod -aG docker jenkins
USER jenkins
RUN jenkins-plugin-cli --plugins "blueocean:1.24.6 docker-workflow:1.26"
```
Ensure that Dockerfile has no extension (especially that it is not saved as Dockerfile.txt). Then, using a command line, navigate to the folder where the file is stored and build an image:
```
docker build -t myjenkins-empik:1.1 .
```

Finally, run the image with the following ``` docker run ``` command:
```
docker run --name jenkins-empik --rm --detach --network jenkins --env DOCKER_HOST=tcp://docker:2376 --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 --volume jenkins-data:/var/jenkins_home --volume jenkins-docker-certs:/certs/client:ro --volume "%HOMEDRIVE%%HOMEPATH%":/home --publish 8080:8080 --publish 50000:50000 myjenkins-empik:1.1
```

Before accessing Jenkins, there a few steps that are necessary in order to proceed further. Open a browser and paste the following url: ``` http://localhost:8080 ```. "Unlock Jenkins" page should appear. Display the Jenkins console log: ``` docker logs jenkins-empik ```, copy the automatically generated password (between the two sets of asterisks) and paste it. After clicking "continue" button, "Customize Jenkins" page should appear. Select option "install suggested plugins". In the last step, create first admin user and click "save and finish". 

To be able to manage your java application, add a Maven installation in Jenkins. To do so, navigate to dashboard and select "manage jenkins" and then "global tool configuration". Scroll down to "Maven installations", click "Add Maven", provide name "maven-3.8.1" and choose version ``` 3.8.1 ```. Remember to save new settings.

## Stage 3 and 4
Connect Jenkins to github repository. Create a Jenkins Pipeline that builds your spring boot application when 
changes are pushed to Github.

It is assumed that a github repository is already created. Navigate to Jenkins dashboard and select "New Item" from the left side. Choose "pipeline" project, provide a valid name for the newly created project and click "ok". In "general" tab provide a brief description. Underneath, select "GitHub project" option and paste a url of the github repository. In "build triggers" section select "GitHub hook trigger for GITScm polling". Then, in "pipeline" tab select definition as "Pipeline script from SCM", SCM as "Git" and paste repository URL. In "branch specifier" field type ``` */main ```. 
Jenkins pipeline project has now been configured. Now, the github repository needs to be configured, so that it notifies Jenkins project each time a new commit is pushed. Go to github page and select a repository that will be connected. Open "settings" tab, choose "webhooks" option and click "Add webhook". Provide ip address and port that your Jenkins instance operates on, e.g ''' http://192.168.0.20:8080/github-webhook/ '''. Select content type as "application/json", then click "let me select individual events" and ensure that push and pull request events are enabled. Finally click "Add webhook".
Now Jenkins project is connected to a given github repository. After a pushed commit, Jenkins will automatically run commands specified in Jenkinsfile.
Now you can proceed and create the following Jenkinsfile:
```
pipeline {
    agent any

    tools {
        maven "maven-3.8.1"
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -f springboot-first-app/pom.xml clean package'
            }
        }
        stage('Test')
        {
            steps{
                sh 'mvn -f springboot-first-app/pom.xml test'
            }
        }
        stage('Deliver') { 
            steps {
                sh "chmod +x -R ${env.WORKSPACE}"
                sh 'springboot-first-app/jenkins/scripts/deliver.sh'
            }
        }
    }
}
```
This Jenkinsfile builds, runs tests and finally launches your Springboot application. Check that application is running: go to the browser and paste the following link: ``` http://localhost:2223/welcome?word=AnnA ```. A page with text "AnnA is a palindrome." should appear.

# Stage 5
Wrap your application in a Docker image, building it in Jenkins. Check that your container is running.

To wrap you application in a Docker image, create the following Dockerfile:
```
FROM maven:3.8.1-openjdk-11-slim AS MAVEN_BUILD1

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:11.0.11-9-jre-slim-buster

WORKDIR /app

COPY --from=MAVEN_BUILD1 /build/target/springboot-first-app-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "springboot-first-app-0.0.1-SNAPSHOT.jar"]
```

It is possible to access Jenkins container: ``` docker exec -it jenkins-empik bash  ```. Then you may navigate to the folder where files relevant to particular projects are stored: ``` cd var/jenkins_home/workspace ```. Then ``` cd EmpikPalindrome/springboot-first-app ``` to your pipeline project and run the following commands:
```
docker image build -t springboot-first-app-docker .
docker container run -p 2223:2223 springboot-first-app-docker
curl -XGET 'http://localhost:2223/welcome?word=AnnA'
```

Text "Anna is a palindrome" should be returned.
