# creditsuisse-challenge

Pre-requisite:
  we require below tools to run the pipeline on host machine.
  1. Jenkins with all the default plugins.
  2. Git
  3. AWS CLI
  4. docker
  
  Jenkins configuration:
  1. Install cloudbees AWS credential plugin
  2. configure AWS credendials (accessKeyID and secretAccessKey)
  3. configure Git and docker hub credentials.
  4. Create jenkins job with below configuration.
  
  ![image](https://user-images.githubusercontent.com/11476219/145175273-73a7151e-22f8-4d85-a531-f2e572e020a8.png)


  AWS setup for deployment:
  1. Create ECS cluster with the EC2 instance.
  2. Create Load balancer and target group to send traffic to backend server.
  This will be manual setup.
 
How to run program:
  1. Build a custom Jenkins Docker image:
      - I have installed all the plugins using jenkins CLI so that if we needed to add more plugins we can do that in future.
        plugins.txt -> list of all plugins
        create-user.groovy -> create jenkins user
        role.groovy -> create roles.
      - To create user and roles in jenkins I used groovy scriting. 
      - Below are the commands to build the image and upload it in docker hub.
        docker build -t yogeshyr/jenkins:<version> .  -> to build the image
        docker login -u <username> -p <passowrd>      -> login into docker hub
        docker push yogeshy01/jenkins:<version>       -> push docker image into dockerhub.
      - dockerhub link where i pushed the customise jenkins image.
        https://hub.docker.com/repository/docker/yogeshy01/jenkins
  
  2. Create a pipeline to automate and publish the above image after testing the relevant parts.
      - Used Jenkins tool to automate the task to build and push image into docker hub.
      - I didnt get that what testing needs to be done on this image but as per my understanding and knowledge we can do structure testing on docker images.
        and here I am using base image of jenkins and not doing any structure changes. So i have not added any testing part.
        If we want to do the container level testing then we can run the created image on local host first. Check for any issues with container and then we can               execute the next stage according to results.
  
  3. Run image on hosted Docker service.
      - I used AWS ECS to deploy jenkins image.
      - AWS ECS is easy to use and we dont need to manage underlying infrastructure. 
      - Best service to host your small containerized applications and test it.
      - I have created python script to deploy the jenkins image to AWS ECS.
        aws-ecs-service.py 
      - Script will check first if the container service is aleredy running or not.
        If its already there then the container will be recreated with new image
         or else it will create new one.
        Note: In this script, all the variables are static because we have once ECS cluster and only one service to deploy on cluster.
        In real world/project, we have to pass the parmeters to the script so acording to that we have to made changes in script.
      - Jenkins URL: http://34.222.117.173/
  
I have created one single jenkins pipeline for all three parts mentioned in problem statement. 
stages of Jenkinsfile.
  1. Validate pipeline  -> validate the Jenkinsfile
  2. Build image        -> Build the image from Dockerfile.
  3. Push image         -> Push image to docker hub.
  4. Deploy to AWS ECS  -> Deploy jenkins image to AWS ECS cluster.


