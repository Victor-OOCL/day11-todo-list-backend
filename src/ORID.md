## 1. **Objective**
- I completed the frontend and backend integration for the Todolist application.
- I learned about React interceptors and the CORS (Cross-Origin Resource Sharing) issue.
- I understood the cause of CORS errors and learned how to solve them.
## 2. **Reflective**
- While connecting the frontend and backend, I encountered a CORS issue where the browser blocked cross-origin requests. This made me realize that cross-origin issues are common in modern web applications.
- Using React interceptors allows us to perform actions before sending requests, such as adding authentication information or handling errors.
- When solving the CORS issue, I learned how to configure the server to allow cross-origin requests or use the `proxy` method to handle CORS in the frontend development environment.
## 3. **Interpretive**
- The CORS issue occurs because of the browser's security policy, which blocks requests between different origins (domains). To solve this, we can configure the server to include response headers (like `Access-Control-Allow-Origin`) that allow cross-origin requests.
- React interceptors can intercept requests and responses, which allows us to manage headers, error handling, etc., in a unified manner and improves code maintainability.
## 4. **Decisional**
- In future projects, I will continue to use interceptors to handle API requests, as they help centralize request and response handling, making the code simpler and easier to maintain.
- I will pay more attention to CORS configuration issues and ensure both in the development and production phases that cross-origin issues are addressed to avoid request failures.