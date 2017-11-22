RSS Feed Reader

This application is for reading RSS feeds and storing changes in MySQL database.

To set this application you need to:
 
 1. Clone the source code.
 
 2. Navigate to the root of the application (location of the pom.xml file)
    
    2.1 In resources/application.properties set the MySQL url, username,
    and password with appropriate values for the MySQL instance that you want to provide to the application  
    
    2.1 Crete database schema in MySQL database with name "rss_feed" or execute the schema-mysql.sql
    that is located in the resources folder
    
 3. Open cmd or terminal, navigate to the root of the application 
    (location of the pom.xml file) and run the command "mvn spring-boot:run"
    or start from your IDE 
    
    Note : This application is developed with InteliJ IDEA
    
 

