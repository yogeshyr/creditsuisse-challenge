import boto3
import sys
import time

BUILD_NUMBER = sys.argv[1]
cluster_nm = "jenkins-cluster"
service_nm = "jenkins-service"
region = "us-west-2"
#boto3.setup_default_session(profile_name='yogesh', region_name='us-west-2')


#client = boto3.client('ecs')
client = boto3.client(service_name='ecs', region_name='us-west-2')
response = client.register_task_definition(
    family='string',
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


list_services = client.list_services(cluster=cluster_nm,launchType='EC2')
for each in list_services['serviceArns']:
    print(each)
    if service_nm in each:
        print("Deleting the exisitng service")
        delete_service = client.delete_service(cluster=cluster_nm,service=service_nm,force=True)


time.sleep(90)

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
    role='ecs-service-role1',
)


