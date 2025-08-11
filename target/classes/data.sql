
-- 1. Insert Categories
-- (Original Data)
INSERT INTO category (name, description, status) VALUES
('Frontend Development', 'Courses covering client-side technologies like HTML, CSS, JavaScript, and React.', 'ACTIVE'),
('Backend Development', 'Courses covering server-side technologies like Node.js, Python, Java, and databases.', 'ACTIVE'),
('DevOps', 'Courses on CI/CD, containerization, and orchestration.', 'ACTIVE');
-- (New Data)
INSERT INTO category (name, description, status) VALUES
('Software Testing & QA', 'Learn manual and automated testing techniques.', 'ACTIVE');

-- 2. Insert Mentor Details
-- (Original Data)
INSERT INTO mentor_detail (age, experience, qualification, profession, profile_pic) VALUES
(35, 10, 'M.Sc. Computer Science', 'Senior Frontend Engineer', 'profilepic_1.jpg'),
(40, 15, 'Ph.D. Software Engineering', 'Principal Backend Engineer', 'profilepic_2.jpg'),
(30, 7, 'Cloud Certified Engineer', 'DevOps Consultant', 'profilepic_3.jpg');
-- (New Data)
INSERT INTO mentor_detail (age, experience, qualification, profession, profile_pic) VALUES
(42, 16, 'ISTQB Advanced Level', 'QA Automation Manager', 'profilepic_4.jpg');

-- 3. Insert Users
-- (Original Data with Mentor Amounts Updated for New Enrollments)
INSERT INTO users (first_name, last_name, email, password, phone_no, role, status, mentor_detail_id, amount) VALUES
('Brad', 'Traversy', 'brad@traversymedia.com', 'password_Brad', '111-222-3333', 'MENTOR', 'ACTIVE', 1, 609.93),
('Hussein', 'Nasser', 'hussein@nasser.com', 'password_Hussein', '444-555-6666', 'MENTOR', 'ACTIVE', 2, 179.98),
('Alice', 'Smith', 'alice.smith@example.com', 'password_Alice', '777-888-9999', 'LEARNER', 'ACTIVE', NULL, 0.00),
('Bob', 'Johnson', 'bob.johnson@example.com', 'password_Bob', '123-456-7890', 'LEARNER', 'ACTIVE', NULL, 0.00),
('Admin', 'User', 'admin@lms.com', 'password_Admin', '000-000-0000', 'ADMIN', 'ACTIVE', NULL, 0.00),
('Charlie', 'Davis', 'charlie.davis@example.com', 'password_Charlie', '234-567-8901', 'LEARNER', 'ACTIVE', NULL, 0.00),
('Diana', 'Miller', 'diana.miller@example.com', 'password_Diana', '345-678-9012', 'LEARNER', 'ACTIVE', NULL, 0.00),
('Eve', 'Wilson', 'eve.wilson@example.com', 'password_Eve', '456-789-0123', 'LEARNER', 'ACTIVE', NULL, 0.00),
('Nana', 'Janashia', 'nana@techworld.com', 'password_Nana', '567-890-1234', 'MENTOR', 'ACTIVE', 3, 440.96);
-- ('Sarath', 'Admin', 'admin@gmail.com', 'admin', '567-890-1234', 'ADMIN', 'ACTIVE', NULL, 0.00),
-- ('Sarath', 'Mentor', 'mentor@gmail.com', 'mentor', '567-890-1234', 'MENTOR', 'ACTIVE', NULL, 0.00),
-- ('Sarath', 'Learner', 'learner@gmail.com', 'learner', '567-890-1234', 'LEARNER', 'ACTIVE', NULL, 0.00);


-- (New Data)
INSERT INTO users (first_name, last_name, email, password, phone_no, role, status, mentor_detail_id, amount) VALUES
('James', 'Bach', 'james.bach@example.com', 'password_James', '678-901-2345', 'MENTOR', 'ACTIVE', 4, 0.00);

-- 4. Insert Courses
-- (Original Data)
INSERT INTO course (name, description, prerequisite, author_course_note, price, discount_in_percent, type, thumbnail, status, category_id, mentor_id) VALUES
('React.js Development', 'Master the most popular frontend library for building user interfaces.', 'JavaScript Fundamentals', 'This course covers Hooks, Context API, Redux, and React Router.', 99.99, 10, 'PAID', 'react.png', 'ACTIVE', 1, 1),
('Node.js & Express', 'Build fast, scalable, and secure backend APIs with Node.js and the Express framework.', 'Core JavaScript', 'Learn to build RESTful services, handle authentication with JWT, and test your API.', 49.99, 100, 'FREE', 'node.png', 'ACTIVE', 2, 2),
('Docker & Kubernetes: A Practical Guide', 'Learn to containerize applications with Docker and orchestrate them with Kubernetes.', 'Basic Linux command line knowledge', 'Hands-on labs to get you production-ready.', 129.99, 15, 'PAID', 'dockerr.jpg', 'ACTIVE', 3, 9);
-- (New Data)
INSERT INTO course (name, description, prerequisite, author_course_note, price, discount_in_percent, type, thumbnail, status, category_id, mentor_id) VALUES
('Advanced CSS & Animations', 'Take your CSS skills to the next level with advanced animations, transitions, and layouts.', 'Intermediate CSS', 'Create stunning, professional-grade web animations.', 79.99, 0, 'PAID', 'cssanimations.jpg', 'ACTIVE', 1, 1),
('JavaScript for Beginners', 'A complete introduction to the most popular programming language on the web.', 'None', 'Start your web development journey here.', 0.00, 100, 'FREE', 'javascript.png', 'ACTIVE', 1, 1),
('Python with Django', 'Build powerful web applications with the Django framework for Python.', 'Basic Python', 'Go from zero to a deployed web app.', 89.99, 0, 'PAID', 'pythondjango.png', 'ACTIVE', 2, 2),
('Database Fundamentals', 'Learn the basics of SQL and NoSQL databases.', 'None', 'Understand how data is stored and managed.', 0.00, 100, 'FREE', 'database.jpg', 'ACTIVE', 2, 2),
('AWS Certified Cloud Practitioner', 'Prepare for and pass the AWS Certified Cloud Practitioner exam.', 'None', 'Your first step into the AWS cloud.', 109.99, 0, 'PAID', 'aws.png', 'ACTIVE', 3, 9),
('Introduction to CI/CD', 'Learn the concepts of Continuous Integration and Continuous Deployment.', 'Basic Git knowledge', 'Automate your software delivery pipeline.', 0.00, 100, 'FREE', 'cicd.png', 'ACTIVE', 3, 9),
('Selenium WebDriver with Java', 'Automate web applications with the most popular tool.', 'Core Java', 'Build a robust test automation framework from scratch.', 99.99, 10, 'PAID', 'selenium.jpg', 'ACTIVE', 4, 10),
('Manual Testing Fundamentals', 'Learn the core principles and techniques of manual software testing.', 'None', 'The essential starting point for any QA professional.', 0.00, 100, 'FREE', 'manualtesting.png', 'ACTIVE', 4, 10);

-- 5. Insert Course Sections
-- (Original Data)
INSERT INTO course_section (section_no, name, description, course_id) VALUES
('1', 'React Basics', 'Understand the core concepts and building blocks of React.', 1),
('2', 'React Hooks', 'Learn to manage state and side effects in functional components.', 1),
('1', 'Node.js Fundamentals', 'Grasp the core architecture and modules of the Node.js runtime.', 2),
('2', 'Express.js Basics', 'Learn the fundamentals of the Express.js framework for building web servers.', 2),
('1', 'Docker Fundamentals', 'Learn the basics of containerization with Docker.', 3),
('2', 'Kubernetes Basics', 'Get started with container orchestration using Kubernetes.', 3);
-- (New Data)
INSERT INTO course_section (section_no, name, description, course_id) VALUES
('3', 'Advanced React Patterns', 'Explore higher-order components and render props.', 1),
('4', 'State Management with Redux', 'Manage complex application state with Redux Toolkit.', 1),
('3', 'API Authentication', 'Secure your API endpoints with JWT.', 2),
('4', 'Testing Node.js Applications', 'Unit and integration testing with Mocha and Chai.', 2),
('3', 'Advanced Docker Concepts', 'Multi-stage builds and Docker networking.', 3),
('4', 'Deploying to Kubernetes', 'Learn about Deployments, Services, and Ingress.', 3),
('1', 'CSS Transitions & Keyframes', 'Mastering smooth animations between states.', 4),
('2', 'CSS-in-JS', 'Exploring modern styling techniques in JavaScript frameworks.', 4),
('1', 'JS Core Concepts', 'Variables, data types, functions, and scope.', 5),
('2', 'DOM Manipulation', 'Interacting with the web page using JavaScript.', 5),
('1', 'Django Basics', 'Project setup, models, and the admin interface.', 6),
('2', 'REST APIs with DRF', 'Building APIs with Django REST Framework.', 6),
('1', 'Relational Databases', 'Understanding SQL and database normalization.', 7),
('2', 'NoSQL Databases', 'An introduction to document-based databases like MongoDB.', 7),
('1', 'Core AWS Services', 'An overview of IAM, S3, EC2, and VPC.', 8),
('2', 'Cloud Security & Billing', 'Understanding the Shared Responsibility Model and AWS pricing.', 8),
('1', 'Jenkins Basics', 'Getting started with the leading automation server.', 9),
('2', 'CI/CD Concepts', 'The theory behind modern software delivery pipelines.', 9),
('1', 'Selenium WebDriver Basics', 'Locating elements and performing actions.', 10),
('2', 'TestNG Framework', 'Structuring your tests with the TestNG framework.', 10),
('1', 'Software Testing Lifecycle', 'Understand the phases of testing from planning to closure.', 11),
('2', 'Test Case Design Techniques', 'Learn equivalence partitioning and boundary value analysis.', 11);

-- 6. Insert Course Section Topics
-- (Original Data)
INSERT INTO course_section_topic (topic_no, name, description, youtube_url, section_id) VALUES
('1.1', 'JSX Syntax', 'Learn the syntax extension that allows writing HTML-like markup in JavaScript.', 'https://www.youtube.com/watch?v=sBws8MSXN7A&t=1189s', 1),
('1.2', 'Components & Props', 'Understand how to build reusable components and pass data using props.', 'https://www.youtube.com/watch?v=t7d103y_1wE', 1),
('2.1', 'useState Hook', 'Master the useState hook to add state variables to functional components.', 'https://www.youtube.com/watch?v=O6P86uwfdR0', 2),
('2.2', 'useEffect Hook', 'Master the useEffect hook to perform side effects like data fetching.', 'https://www.youtube.com/watch?v=0ZJgAiU_h_4', 2),
('1.1', 'Node Architecture', 'Understand the event loop, V8 engine, and libuv.', 'https://www.youtube.com/watch?v=zphcsoSJMvM', 3),
('1.2', 'File System API', 'Learn to perform file operations using the built-in fs module.', 'https://www.youtube.com/watch?v=U57kU311-nE', 3),
('2.1', 'Setting up Server', 'Create a basic web server using Express.', 'https://www.youtube.com/watch?v=L72fhGm1tfE&t=577s', 4),
('2.2', 'Middleware Concepts', 'Understand the core concept of middleware in Express.', 'https://www.youtube.com/watch?v=lY6icfXJp8k', 4),
('1.1', 'What is Docker?', 'A conceptual overview of Docker and containerization.', 'https://www.youtube.com/watch?v=3c-iBn73dDE', 5),
('1.2', 'Docker Compose Explained', 'Learn how to define and run multi-container Docker applications.', 'https://www.youtube.com/watch?v=p34s7-sEm_g', 5),
('2.1', 'What is Kubernetes?', 'A high-level introduction to Kubernetes.', 'https://www.youtube.com/watch?v=V_0vm2cTjgg', 6),
('2.2', 'Kubernetes Architecture Explained', 'Understand the components of the Kubernetes control plane and worker nodes.', 'https://www.youtube.com/watch?v=R-3dfURb2hA', 6);
-- (New Data)
INSERT INTO course_section_topic (topic_no, name, description, youtube_url, section_id) VALUES
('3.1', 'Higher-Order Components (HOCs)', 'Reuse component logic with the HOC pattern.', 'https://www.youtube.com/watch?v=B6aNv8o_6-c', 7),
('3.2', 'Render Props', 'Share code between components using a prop whose value is a function.', 'https://www.youtube.com/watch?v=B6aNv8o_6-c&t=600s', 7),
('4.1', 'Introduction to Redux Toolkit', 'The modern, recommended way to use Redux.', 'https://www.youtube.com/watch?v=93CR_yURoYU', 8),
('4.2', 'Async Actions with Thunk', 'Handle asynchronous logic like API calls in Redux.', 'https://www.youtube.com/watch?v=bbkBuqC1rU4', 8),
('3.1', 'JWT Authentication', 'Understand and implement stateless authentication with JSON Web Tokens.', 'https://www.youtube.com/watch?v=mbsmsi7l3r4', 9),
('3.2', 'Password Hashing with bcrypt', 'Securely hash and salt user passwords before storing them.', 'https://www.youtube.com/watch?v=O6cmuiTBZVs', 9),
('4.1', 'Unit Testing with Mocha & Chai', 'Write unit tests for individual functions.', 'https://www.youtube.com/watch?v=kCg2V_4ZzeE', 10),
('4.2', 'Integration Testing with Supertest', 'Test your API endpoints by making real HTTP requests.', 'https://www.youtube.com/watch?v=1-y5b2bjcxk', 10),
('3.1', 'Docker Networking', 'Understand bridge, host, and overlay networks.', 'https://www.youtube.com/watch?v=bKFMS5C4CG0', 11),
('3.2', 'Multi-Stage Docker Builds', 'Create smaller, more secure Docker images.', 'https://www.youtube.com/watch?v=T2e_33dD4A8', 11),
('4.1', 'Kubernetes Deployments', 'Manage application lifecycle with Deployments.', 'https://www.youtube.com/watch?v=7bA0gW2_b9c', 12),
('4.2', 'Kubernetes Services & Ingress', 'Expose your applications to the outside world.', 'https://www.youtube.com/watch?v=7bA0gW2_b9c&t=1200s', 12),
('1.1', 'Understanding Transitions', 'Animate CSS property changes smoothly over a given duration.', 'https://www.youtube.com/watch?v=gJlgXq_S40c', 13),
('1.2', 'Creating Keyframe Animations', 'Define complex, multi-step animations using @keyframes.', 'https://www.youtube.com/watch?v=YszONjKpgg4', 13),
('2.1', 'Intro to Styled Components', 'Learn the basics of the popular CSS-in-JS library.', 'https://www.youtube.com/watch?v=02zO0hZmwnw', 14),
('2.2', 'Dynamic Styling with Props', 'Pass props from your React components to change styles dynamically.', 'https://www.youtube.com/watch?v=204I3b_1-qM', 14),
('1.1', 'Variables & Data Types', 'Learn about let, const, strings, numbers, and booleans.', 'https://www.youtube.com/watch?v=PkZNo7MFNFg&t=415s', 15),
('1.2', 'Functions & Scope', 'Understand how to define functions and the rules of scope.', 'https://www.youtube.com/watch?v=xUI5Tsl2JpY', 15),
('2.1', 'Selecting Elements', 'Use querySelector and getElementById to access DOM nodes.', 'https://www.youtube.com/watch?v=v-F3YLd6oYc', 16),
('2.2', 'Handling Events', 'Respond to user actions like clicks and key presses with event listeners.', 'https://www.youtube.com/watch?v=XF1_MlZ5l6M', 16),
('1.1', 'Project Setup & Models', 'Initialize a Django project and define your database models.', 'https://www.youtube.com/watch?v=rHux0g2I_kc', 17),
('1.2', 'Views & Templates', 'Create views to handle requests and render HTML templates.', 'https://www.youtube.com/watch?v=rHux0g2I_kc&t=1200s', 17),
('2.1', 'Serializers', 'Convert complex data types, like querysets, to JSON.', 'https://www.youtube.com/watch?v=B38aDwUpcFc', 18),
('2.2', 'ViewSets & Routers', 'Quickly build API views and URL patterns.', 'https://www.youtube.com/watch?v=B38aDwUpcFc&t=900s', 18),
('1.1', 'SQL Basics', 'Learn the fundamentals of Structured Query Language.', 'https://www.youtube.com/watch?v=HXV3zeQKqGY', 19),
('1.2', 'Database Normalization', 'Understand the principles of organizing a relational database.', 'https://www.youtube.com/watch?v=22ZksA4J_3M', 19),
('2.1', 'Intro to MongoDB', 'Learn about the popular NoSQL document database.', 'https://www.youtube.com/watch?v=-56x56UppqQ', 20),
('2.2', 'CRUD Operations in MongoDB', 'Learn to Create, Read, Update, and Delete documents.', 'https://www.youtube.com/watch?v=aypuD_DstWc', 20),
('1.1', 'IAM & S3', 'Manage user access and store objects in the cloud.', 'https://www.youtube.com/watch?v=9T99bJ2n2us', 21),
('1.2', 'EC2 & VPC', 'Launch virtual servers and create isolated networks.', 'https://www.youtube.com/watch?v=9T99bJ2n2us&t=1800s', 21),
('2.1', 'Shared Responsibility Model', 'Understand security responsibilities between AWS and the customer.', 'https://www.youtube.com/watch?v=J9_C8a2-5a4', 22),
('2.2', 'AWS Pricing Models', 'Learn about On-Demand, Reserved Instances, and Spot pricing.', 'https://www.youtube.com/watch?v=J9_C8a2-5a4&t=1200s', 22),
('1.1', 'What is Jenkins?', 'An introduction to the open-source automation server.', 'https://www.youtube.com/watch?v=8j1zCD-sVwQ', 23),
('1.2', 'Declarative vs Scripted Pipelines', 'Understand the two different ways to write a Jenkinsfile.', 'https://www.youtube.com/watch?v=7KCS70sCoK0', 23),
('2.1', 'What is Continuous Integration?', 'The practice of merging all developer working copies to a shared mainline several times a day.', 'https://www.youtube.com/watch?v=1er2mg_9U4E', 24),
('2.2', 'What is Continuous Deployment?', 'A strategy where every code change goes through the pipeline and automatically gets put into production.', 'https://www.youtube.com/watch?v=1er2mg_9U4E&t=300s', 24),
('1.1', 'WebDriver Commands', 'Learn common commands like findElement, click, and sendKeys.', 'https://www.youtube.com/watch?v=pupqaT9hgwI', 25),
('1.2', 'Waits in Selenium', 'Understand implicit, explicit, and fluent waits.', 'https://www.youtube.com/watch?v=pupqaT9hgwI&t=1500s', 25),
('2.1', 'TestNG Annotations', 'Learn about @Test, @BeforeMethod, @AfterMethod, etc.', 'https://www.youtube.com/watch?v=Vb_2M3O-z3w', 26),
('2.2', 'Assertions in TestNG', 'Verify expected outcomes in your tests.', 'https://www.youtube.com/watch?v=Vb_2M3O-z3w&t=600s', 26),
('1.1', 'What is a Bug?', 'Understanding defects in software.', 'https://www.youtube.com/watch?v=Y3-a2s-S3YE', 27),
('1.2', 'The 7 Principles of Testing', 'The fundamental principles of software testing.', 'https://www.youtube.com/watch?v=Y3-a2s-S3YE&t=300s', 27),
('2.1', 'Equivalence Partitioning', 'A black-box testing technique to reduce the number of test cases.', 'https://www.youtube.com/watch?v=Y3-a2s-S3YE&t=900s', 28),
('2.2', 'Boundary Value Analysis', 'Test the boundaries between partitions.', 'https://www.youtube.com/watch?v=Y3-a2s-S3YE&t=1200s', 28);

-- 7. Insert Payments
-- (Original Data)
INSERT INTO payment (amount, enrollment_id, card_no, cvv, expiry_date, name_on_card, payment_id, learner_id) VALUES
(89.99, '2025-07-28T02:31:37.827819200', '4242424242424242', '321', '04/27', 'Alice Smith', 'd128e07b-374a-4082-a7e2-aea020bed699', 3),
(89.99, '2025-07-28T02:40:15.987654300', '5555444433332222', '456', '08/28', 'Charlie Davis', 'a8c3b1e1-4a2d-4f9e-8b1a-9c2d7e5f6a3b', 6),
(89.99, '2025-07-28T02:42:05.123456700', '1111222233334444', '789', '12/26', 'Diana Miller', 'f4b2a1c9-8d7e-4c6b-a5f4-3e2d1c0b9a8d', 7);
-- (New Data)
INSERT INTO payment (amount, enrollment_id, card_no, cvv, expiry_date, name_on_card, payment_id, learner_id) VALUES
(79.99, '2025-07-28T03:01:00.123456700', '1234567812345678', '111', '01/28', 'Alice Smith', 'a1b2c3d4-e5f6-a1b2-c3d4-e5f6a1b2c3d4', 3),
(89.99, '2025-07-28T03:02:00.234567800', '2345678923456789', '222', '02/29', 'Bob Johnson', 'b2c3d4e5-f6a7-b2c3-d4e5-f6a7b2c3d4e5', 4),
(109.99, '2025-07-28T03:03:00.345678900', '3456789034567890', '333', '03/27', 'Charlie Davis', 'c3d4e5f6-a7b8-c3d4-e5f6-a7b8c3d4e5f6', 6),
(110.49, '2025-07-28T03:04:00.456789000', '4567890145678901', '444', '04/28', 'Diana Miller', 'd4e5f6a7-b8c9-d4e5-f6a7-b8c9d4e5f6a7', 7),
(89.99, '2025-07-28T03:05:00.567890100', '5678901256789012', '555', '05/29', 'Eve Wilson', 'e5f6a7b8-c9d0-e5f6-a7b8-c9d0e5f6a7b8', 8),
(89.99, '2025-07-28T03:06:00.678901200', '6789012367890123', '666', '06/27', 'Alice Smith', 'f6a7b8c9-d0e1-f6a7-b8c9-d0e1f6a7b8c9', 3),
(109.99, '2025-07-28T03:07:00.789012300', '7890123478901234', '777', '07/28', 'Bob Johnson', 'a7b8c9d0-e1f2-a7b8-c9d0-e1f2a7b8c9d0', 4),
(89.99, '2025-07-28T03:08:00.890123400', '8901234589012345', '888', '08/29', 'Charlie Davis', 'b8c9d0e1-f2a3-b8c9-d0e1-f2a3b8c9d0e1', 6),
(79.99, '2025-07-28T03:09:00.901234500', '9012345690123456', '999', '09/27', 'Diana Miller', 'c9d0e1f2-a3b4-c9d0-e1f2-a3b4c9d0e1f2', 7),
(110.49, '2025-07-28T03:10:00.012345600', '0123456701234567', '101', '10/28', 'Eve Wilson', 'd0e1f2a3-b4c5-d0e1-f2a3-b4c5d0e1f2a3', 8);

-- 8. Insert Enrollments
-- (Original Data)
INSERT INTO enrollment (amount, enrollment_id, enrollment_time, status, course_id, learner_id, payment_id) VALUES
(89.99, '2025-07-28T02:31:37.827819200', '2025-07-28 02:31:37', 'ENROLLED', 1, 3, 1),
(0.00,  '2025-07-28T02:35:10.123456700', '2025-07-28 02:35:10', 'ENROLLED', 2, 4, NULL),
(0.00,  '2025-07-28T02:38:00.555555500', '2025-07-28 02:38:00', 'ENROLLED', 2, 3, NULL),
(89.99, '2025-07-28T02:40:15.987654300', '2025-07-28 02:40:15', 'ENROLLED', 1, 6, 2),
(89.99, '2025-07-28T02:42:05.123456700', '2025-07-28 02:42:05', 'ENROLLED', 1, 7, 3),
(0.00,  '2025-07-28T02:44:30.444444400', '2025-07-28 02:44:30', 'ENROLLED', 2, 8, NULL),
(79.99, '2025-07-28T03:01:00.123456700', '2025-07-28 03:01:00', 'ENROLLED', 4, 3, 4),
(89.99, '2025-07-28T03:02:00.234567800', '2025-07-28 03:02:00', 'ENROLLED', 6, 4, 5),
(109.99,'2025-07-28T03:03:00.345678900', '2025-07-28 03:03:00', 'ENROLLED', 8, 6, 6),
(110.49,'2025-07-28T03:04:00.456789000', '2025-07-28 03:04:00', 'ENROLLED', 3, 7, 7),
(89.99, '2025-07-28T03:05:00.567890100', '2025-07-28 03:05:00', 'ENROLLED', 1, 8, 8),
(89.99, '2025-07-28T03:06:00.678901200', '2025-07-28 03:06:00', 'ENROLLED', 6, 3, 9),
(109.99,'2025-07-28T03:07:00.789012300', '2025-07-28 03:07:00', 'ENROLLED', 8, 4, 10),
(89.99, '2025-07-28T03:08:00.890123400', '2025-07-28 03:08:00', 'ENROLLED', 1, 6, 11),
(79.99, '2025-07-28T03:09:00.901234500', '2025-07-28 03:09:00', 'ENROLLED', 4, 7, 12),
(110.49,'2025-07-28T03:10:00.012345600', '2025-07-28 03:10:00', 'ENROLLED', 3, 8, 13),
(0.00,  '2025-07-28T03:11:00.111111100', '2025-07-28 03:11:00', 'ENROLLED', 5, 3, NULL),
(0.00,  '2025-07-28T03:12:00.222222200', '2025-07-28 03:12:00', 'ENROLLED', 7, 4, NULL),
(0.00,  '2025-07-28T03:13:00.333333300', '2025-07-28 03:13:00', 'ENROLLED', 9, 6, NULL),
(0.00,  '2025-07-28T03:14:00.444444400', '2025-07-28 03:14:00', 'ENROLLED', 11, 7, NULL);


-- 1. Insert a new Category for 'Data Science'
INSERT INTO category (name, description, status) VALUES 
('Data Science', 'Courses on data analysis, machine learning, and visualization.', 'ACTIVE');

-- 2. Insert Mentor Details for the new mentor
INSERT INTO mentor_detail (age, experience, qualification, profession, profile_pic) VALUES 
(38, 12, 'Ph.D. in Statistics', 'Data Scientist', 'profilepic_5.jpg');

-- 3. Insert the new Mentor user
-- Note: The mentor_detail_id should correspond to the ID created in the previous step (assumed to be 5).
INSERT INTO users (first_name, last_name, email, password, phone_no, role, status, mentor_detail_id, amount) VALUES 
('Andrew', 'Ng', 'andrew.ng@example.com', 'password_Andrew', '789-012-3456', 'MENTOR', 'ACTIVE', 5, 0.00);

-- 4. Insert the new Course for Data Science
-- Note: The category_id corresponds to the new 'Data Science' category (assumed to be 5).
-- Note: The mentor_id corresponds to the new mentor 'Andrew Ng' (assumed to be 11).
INSERT INTO course (name, description, prerequisite, author_course_note, price, discount_in_percent, type, thumbnail, status, category_id, mentor_id) VALUES 
('Machine Learning A-Z', 'Learn to create Machine Learning algorithms in Python and R.', 'High School Mathematics', 'Hands-on course with practical examples.', 149.99, 10, 'PAID', 'machinelearning.png', 'ACTIVE', 5, 11);

-- 5. Insert Sections for the new course
-- Note: The course_id corresponds to the new 'Machine Learning A-Z' course (assumed to be 12).
INSERT INTO course_section (section_no, name, description, course_id) VALUES 
('1', 'Introduction to Data Science', 'Core concepts and methodologies.', 12),
('2', 'Regression Models', 'Understanding linear and logistic regression.', 12);

-- 6. Insert Topics for the new sections
-- Note: The section_id's correspond to the sections created above (assumed to be 29 and 30).
INSERT INTO course_section_topic (topic_no, name, description, youtube_url, section_id) VALUES 
('1.1', 'What is Data Science?', 'An overview of the field.', 'https://www.youtube.com/watch?v=X3paOmcrTjQ', 29),
('1.2', 'Data Preprocessing', 'Techniques for cleaning and preparing data.', 'https://www.youtube.com/watch?v=F608hzn_ygo', 29),
('2.1', 'Simple Linear Regression', 'Modeling the relationship between two continuous variables.', 'https://www.youtube.com/watch?v=zPG4NjIkCjc', 30),
('2.2', 'Logistic Regression', 'Predicting a categorical dependent variable.', 'https://www.youtube.com/watch?v=yIYKR4sgzI8', 30);

-- Insert 10 new payment records for the paid enrollments
INSERT INTO payment (amount, enrollment_id, card_no, cvv, expiry_date, name_on_card, payment_id, learner_id) VALUES
(134.99, '2025-08-04T09:35:01.111111100', '1111222233334444', '123', '11/27', 'Bob Johnson', 'e1f2a3b4-c5d6-e1f2-a3b4-c5d6e1f2a3b4', 4),
(134.99, '2025-08-04T09:36:02.222222200', '2222333344445555', '234', '12/28', 'Charlie Davis', 'f2a3b4c5-d6e7-f2a3-b4c5-d6e7f2a3b4c5', 6),
(89.99, '2025-08-04T09:37:03.333333300', '3333444455556666', '345', '01/29', 'Diana Miller', 'a3b4c5d6-e7f8-a3b4-c5d6-e7f8a3b4c5d6', 7),
(89.99, '2025-08-04T09:38:04.444444400', '4444555566667777', '456', '02/27', 'Eve Wilson', 'b4c5d6e7-f8a9-b4c5-d6e7-f8a9b4c5d6e7', 8),
(79.99, '2025-08-04T09:39:05.555555500', '5555666677778888', '567', '03/28', 'Alice Smith', 'c5d6e7f8-a9b0-c5d6-e7f8-a9b0c5d6e7f8', 3),
(79.99, '2025-08-04T09:40:06.666666600', '6666777788889999', '678', '04/29', 'Bob Johnson', 'd6e7f8a9-b0c1-d6e7-f8a9-b0c1d6e7f8a9', 4),
(109.99, '2025-08-04T09:41:07.777777700', '7777888899990000', '789', '05/27', 'Charlie Davis', 'e7f8a9b0-c1d2-e7f8-a9b0-c1d2e7f8a9b0', 6),
(109.99, '2025-08-04T09:42:08.888888800', '8888999900001111', '890', '06/28', 'Diana Miller', 'f8a9b0c1-d2e3-f8a9-b0c1-d2e3f8a9b0c1', 7),
(89.99, '2025-08-04T09:43:09.999999900', '9999000011112222', '901', '07/29', 'Eve Wilson', 'a9b0c1d2-e3f4-a9b0-c1d2-e3f4a9b0c1d2', 8),
(134.99, '2025-08-04T09:44:10.101010100', '0000111122223333', '012', '08/27', 'Alice Smith', 'b0c1d2e3-f4a5-b0c1-d2e3-f4a5b0c1d2e3', 3);

-- Insert 15 new enrollment records (10 paid, 5 free)
INSERT INTO enrollment (amount, enrollment_id, enrollment_time, status, course_id, learner_id, payment_id) VALUES
-- Paid Enrollments
(134.99, '2025-08-04T09:35:01.111111100', '2025-08-04 09:35:01', 'ENROLLED', 12, 4, 14),
(134.99, '2025-08-04T09:36:02.222222200', '2025-08-04 09:36:02', 'ENROLLED', 12, 6, 15),
(89.99, '2025-08-04T09:37:03.333333300', '2025-08-04 09:37:03', 'ENROLLED', 10, 7, 16),
(89.99, '2025-08-04T09:38:04.444444400', '2025-08-04 09:38:04', 'ENROLLED', 10, 8, 17),
(79.99, '2025-08-04T09:39:05.555555500', '2025-08-04 09:39:05', 'ENROLLED', 4, 3, 18),
(79.99, '2025-08-04T09:40:06.666666600', '2025-08-04 09:40:06', 'ENROLLED', 4, 4, 19),
(109.99, '2025-08-04T09:41:07.777777700', '2025-08-04 09:41:07', 'ENROLLED', 8, 6, 20),
(109.99, '2025-08-04T09:42:08.888888800', '2025-08-04 09:42:08', 'ENROLLED', 8, 7, 21),
(89.99, '2025-08-04T09:43:09.999999900', '2025-08-04 09:43:09', 'ENROLLED', 6, 8, 22),
(134.99, '2025-08-04T09:44:10.101010100', '2025-08-04 09:44:10', 'ENROLLED', 12, 3, 23),
-- Free Enrollments
(0.00, '2025-08-04T09:45:11.121212100', '2025-08-04 09:45:11', 'ENROLLED', 5, 4, NULL),
(0.00, '2025-08-04T09:46:12.131313100', '2025-08-04 09:46:12', 'ENROLLED', 7, 6, NULL),
(0.00, '2025-08-04T09:47:13.141414100', '2025-08-04 09:47:13', 'ENROLLED', 9, 7, NULL),
(0.00, '2025-08-04T09:48:14.151515100', '2025-08-04 09:48:14', 'ENROLLED', 11, 8, NULL),
(0.00, '2025-08-04T09:49:15.161616100', '2025-08-04 09:49:15', 'ENROLLED', 5, 3, NULL);


-- Update Brad Traversy's (mentor_id: 1) earnings
UPDATE users 
SET amount = amount + (2 * 79.99) 
WHERE id = 1;

-- Update Hussein Nasser's (mentor_id: 2) earnings
UPDATE users 
SET amount = amount + 89.99 
WHERE id = 2;

-- Update Nana Janashia's (mentor_id: 9) earnings
UPDATE users 
SET amount = amount + (2 * 109.99) 
WHERE id = 9;

-- Update James Bach's (mentor_id: 10) earnings
UPDATE users 
SET amount = amount + (2 * 89.99) 
WHERE id = 10;

-- Update Andrew Ng's (mentor_id: 11) earnings
UPDATE users 
SET amount = amount + (3 * 134.99) 
WHERE id = 11;