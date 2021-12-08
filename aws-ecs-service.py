import boto3
import sys

def register_task(client,BUILD_NUMBER):
    response = client.register_task_definition(
        family='jenkins',
        containerDefinitions=[
            {
                'image': 'yogeshy01/jenkins:' + BUILD_NUMBER,
                'name': 'jenkins',
                'cpu': 1024,
                'memory': 1024,
                'essential': True,
                'portMappings': [
                    {
                        'containerPort': 8080,
                        'hostPort': 80
                    }
                ]
            }
        ]
    )
    print(response)

def update_service(client,cluster_nm,service_nm):
    print("Updating the exisitng service")
    client.update_service(cluster=cluster_nm,service=service_nm,desiredCount=1,taskDefinition='jenkins')
    print("Service updated successfully")
    exit()


def create_service(client,cluster_nm,service_nm):
    print("Creating container with new image")
    response = client.create_service(
        cluster=cluster_nm,
        serviceName=service_nm,
        launchType='EC2',
        taskDefinition='jenkins',
        desiredCount=1,
        loadBalancers=[
            {
                'targetGroupArn': 'arn:aws:elasticloadbalancing:us-west-2:200189870863:targetgroup/jenkins-target-group/180a21f703c6096f',
                'containerName': 'jenkins',
                'containerPort': 8080
            },
        ],
        role='ecs-service-role1'
    )
    print(response)


def main():
    # define input variables
    BUILD_NUMBER = sys.argv[1]
    cluster_nm = "jenkins-cluster"
    service_nm = "jenkins-service"
    region = "us-west-2"
    #boto3.setup_default_session(profile_name='yogesh', region_name='us-west-2')


    #client = boto3.client('ecs')
    client = boto3.client(service_name='ecs', region_name=region)
    register_task(client,BUILD_NUMBER)
    list_services = client.list_services(cluster=cluster_nm,launchType='EC2')
    total_serices = len(list_services['serviceArns'])
    if total_serices == 0:
        create_service(client,cluster_nm,service_nm)
        exit(0)
    else:
        for each in list_services['serviceArns']:
            print(each)
            if service_nm in each:
                update_service(client,cluster_nm,service_nm)

if __name__ == "__main__":
    main()