# Read Me

## Test Steps

 1. Clone project from repository
	    

> git clone https://github.com/digitiqman/centricgateway-seerbit-task.git


2. Clean and Package a Jar file 

> mvn clean package


3. Start the docker container

> docker-compose up -d

  4. With any HTTP client, navigate the below endpoints

>   POST:- http://localhost:8084/transaction    
>   GET:- http://localhost:8084/transaction    
>   DELETE:- http://localhost:8084/transaction

5. Stop the container

> docker-compose down

