# Patent Infringement Check

## Overview
This project is a Spring Boot application that connects to a local MongoDB instance through MongoDB Compass. For version 1.0 now It includes one main endpoint and is configured to run with Docker Compose.

## Production application is running!
- This full stack app is deployed and now it can be accessed directly from the [patent-infiringment-check-app-link](https://frontend-latest-lvpf.onrender.com)
- Frontend are deployed on [Render](https://render.com/) backend are deployed on [GCP Cloud Run](https://cloud.google.com) The reason is because it's free!
- The Database is deployed on MongoDB Altas.
- The API call may be took 15sec ~ 30sec to finish, so please do not fetch the backend too frquent! (the spinging icon, styling, etc. is on the way...) 

## Prerequisites
- **Java**: Java version 21.
- **Docker** and **Docker Compose**: Version 4.22.1. Make sure Docker and Docker Compose are installed and running.
- **MongoDB Compass**: You should have MongoDB Compass installed and configured locally.
- **React**: node 16

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
   - From the backend Dockerfile please point the enviornment to the `dev` rather than `prod` before building.
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
**Alternative**:
   - frontend build
     ```bash
     docker build -t frontend:latest .
     ```
   - backend build
     ```bash
     docker build -t backend:latest .
     ```
   - run docker
     ```bash
     docker run -p -d 8080:8080 backend
     ```
     ```bash
     docker run -p -d 80:80 frontend
     ```

4. **Stop Containers**:
   - To stop the containers, use:
     ```bash
     docker-compose down
     ```
**Alternative**:
  - List docker Info:
    `docker ps`
  - Stop docker:
    `docker stop <container ID>`
    
## FrontEnd
`[frontend-url](https://frontend-latest-lvpf.onrender.com)`
## Backend Endpoints
Once the application is running, you can access the following endpoints:

- **generate report POST**: `[backend-url](https://patent-infringement-check-17325157242.us-central1.run.app)/api/v1/generate-report`
  - This endpoint will consume the Json format payload `patentId` as patent id and `companyName` as company name -> Retrieve the company info and patent info -> start prompting the score and patent features given the list of a company and then generate the assessment.
  - Currently, the API does not store the report in the database.
    
    - **Implementation detail**:
       1. Because the patent list may be too long for a single prompt within its rate limit, the API will split the patents greedily for every product from a company to generate the prompt.
       2. After sending prompt to OpenAI in Batch, the result will give a score and potential risk of patent list for each batch per product. Based on the score, the likelihood will be calculated, and the top 2 candidates will stand out.
       3. The info for each candidate will be used to prompt again to generate explanation and infringed patents.
       4. Finally the explanation from top 2 candidate will be used to prompt for the assessment.
          
- **retrieve report GET**: `[backend-url](https://patent-infringement-check-17325157242.us-central1.run.app)/api/v1/reports`
  - This endpoint will fetch the report history that is stored priviously.
- **save report PUT**: `[backend-url](https://patent-infringement-check-17325157242.us-central1.run.app)/api/v1/report`
  - This endpoint will save the report.
- **delete report DELETE**: `[backend-url](https://patent-infringement-check-17325157242.us-central1.run.app)/api/v1/report/{id}`
  - This endpoint will delete the report by id shown on report list.

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
       curl -X POST "http://localhost:8080/api/v1/generate-report" -H "Content-Type: application/json" -d `{"patentId":"US-RE49889-E1", "companyName": "Walmart Inc."}`
       ```

## Comment
- Although this project was completed in a short time and under tight deadlines, I found it both interesting and somewhat challenging.
- I hope for the opportunity to explore potential improvements, both in the non-functional requirements and the prompting algorithm.
- Initially, I tried providing the AI with a large string all at once, but that approach failed. Later, I implemented a greedy algorithm to resolve the issue.
- The API may occasionally fail due to inconsistencies in the prompt results or formatting. Typically, retrying the endpoint will lead to a better outcome.

## Troubleshooting
- **Connection Issues**: If the application fails to connect to MongoDB, verify the URI and MongoDB service status.
- **Docker-Related Issues**: Make sure Docker is running and that there are no conflicts on the mapped ports.

## TO-DO
- **Frontend styling**: Obviously, the UI sucks.
- **Frontend components**: TO-DO
- **Prompting algorithm enhancement**: The result from OpenAI is not stable and response formation also has room to improve.
- **Exception handling**: Scenario for exceptions are not considered comprehensively.
- **Storing report and retrieving function**: TO-DO

## Reference Links
- [Docker-download](https://gist.github.com/kupietools/2f9f085228d765da579f0f0702bec33c)
- [Java-JDK-21-download](https://www.oracle.com/java/technologies/downloads/)
- [MongoDB](https://www.mongodb.com/products/platform/atlas-database)