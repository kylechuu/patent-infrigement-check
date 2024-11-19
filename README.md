# Patent Infringement Check

## Overview
This project is a Spring Boot application that connects to a local MongoDB instance through MongoDB Compass. For version 1.0 now It includes one main endpoint and is configured to run with Docker Compose.

## Production application is running!
- This full stack app is deployed and now it can be accessed directly from the [patent-infiringment-check-app-link](https://valid-emlyn-patlytics-take-home-c1ac8675.koyeb.app/)
- Both frontend and backend are deployed on [Koyeb](https://www.koyeb.com/) The reason is because it's free!
- The Database is deployed on MongoDB Altas.
- The API call may be took 15sec ~ 30sec to finish, so please do not fetch the backend too frquent! (the spinging icon, styling, etc. is on the way...) 

## Prerequisites
- **Java**: Java version 21.
- **Docker** and **Docker Compose**: Version 4.22.1. Make sure Docker and Docker Compose are installed and running.
- **MongoDB Compass**: You should have MongoDB Compass installed and configured locally.
- **React**: node 16 (the README for frontend is still in TO-DO)

## Configuration
1. **Application Properties**: 
   - Creare the `application-dev.properties` file in the `src/main/resources` directory.
   - Copy all the field from `application-prod.properties` and set the `spring.profiles.active=prod` in application.properties
   - Set up the following properties: 
     - `openai.api.key`: Insert your OpenAI API key.
     - `spring.data.mongodb.uri`: Set the local MongoDB Compass URI here.
       
   **MongoDB Compass local set up**:
     1. Create a cluster and name the table properly.
     2. import the `patents.json` file into a collection named `patents`
     3. import the `company_products.json` file into a collection named `companies`
     4. Note that the Json format in `company_products.json` should be reformatted by removing 'company' as a key. Each company should have `name` and `products` attributes.
        ![screenshoot-data-setup](https://github.com/user-attachments/assets/9439ffe8-8a24-434c-8784-2a892e5a1d42)

       
2. **MongoDB Connection**:
   - MongoDB Compass should be configured to connect on the specified URI.
   - Ensure the MongoDB server is running and accessible from the Docker container.

## Docker Setup
1.   **Configure enviornment variable**:
   - Please change the backend endpoint in `App.js` for the axois endpoint to local and also the backend endpoint in `docker-compose` file before building.
2. **Build Docker Images**:
   - Navigate to the project root directory and run the following command to build the Docker images:
     ```bash
     docker-compose build
     ```

3. **Run with Docker Compose**:
   - Start the application and MongoDB using Docker Compose:
     ```bash
     docker-compose up
     ```
   - Ensure the `spring.data.mongodb.uri` value matches the SPRING_DATA_MONGODB_URI value in the compose.yaml file..


4. **Stop Containers**:
   - To stop the containers, use:
     ```bash
     docker-compose down
     ```
## FrontEnd
`http://localhost:3000/`
## Backend Endpoints
Once the application is running, you can access the following endpoints:

- **generate report GET**: `http://localhost:8080/api/v1/generate-report`
  - This endpoint will consume the Json format payload `patentId` as patent id and `companyName` as company name -> Retrieve the company info and patent info -> start prompting the score and patent features given the list of a company and then generate the assessment.
  - Currently, the API does not store the report in the database.
    
    - **Implementation detail**:
       1. Because the patent list may be too long for a single prompt within its rate limit, the API will split the patents greedily for every product from a company to generate the prompt.
       2. After sending prompt to OpenAI in Batch, the result will give a score and potential risk of patent list for each batch per product. Based on the score, the likelihood will be calculated, and the top 2 candidates will stand out.
       3. The info for each candidate will be used to prompt again to generate explanation and infringed patents.
       4. Finally the explanation from top 2 candidate will be used to prompt for the assessment.
          
  
- **retrieve report GET**: `http://localhost:8080/api/v1/retrieve-report`
  - TO-DO

## Additional Notes
- **BackEnd Endpoint**: The backend endpoint is deployed in URL `ideological-alverta-side-project-kyle-37574475.koyeb.app/` We can replace `localhost:8080/` part for testing in production if prefer.
- **Environment Variables**: Ensure sensitive information like API keys is managed securely.
- **Testing**:
  1. Configure properties setting listed above and prepare the data locally.
  2. Launch the docker compose in local for both app and db.
  3. Prepare the payload in JSON format like this:
     ```bash
     {
      "patentId":"US-RE49889-E1",
      "companyName": "Walmart Inc."
     }
     ```
     and hit the endpoint list above with GET request, you should be able to get a report response.
     Alternatively, you can use the following command to hit the endpoint:
       ```bash
       curl -X GET "http://localhost:8080/api/v1/generate-report" -H "Content-Type: application/json" -d `{"patentId":"US-RE49889-E1", "companyName": "Walmart Inc."}`
       ```

## Comment
- Despite the fact that this project is conducted in a short time (also in rush), I found the project very interesting and somewhat challenging.
- I sincerely hope for an opportunity to explore potential enhancements, both in non-functional requirements and in the prompting algorithm.
- At first, I tried prompting the AI with a large string all at once, which failed. I then developed a greedy algorithm to solve this issue.
- The API may sometimes fail due to inconsistencies in the prompt results or formatting. Usually, retrying the endpoint will improve the outcome.

## Troubleshooting
- **Connection Issues**: If the application fails to connect to MongoDB, verify the URI and MongoDB service status.
- **Docker-Related Issues**: Make sure Docker is running and that there are no conflicts on the mapped ports.

## Reference Links
- [Docker-download](https://gist.github.com/kupietools/2f9f085228d765da579f0f0702bec33c)
- [Java-JDK-21-download](https://www.oracle.com/java/technologies/downloads/)
- [MongoDB](https://www.mongodb.com/products/platform/atlas-database)

