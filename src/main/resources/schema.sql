

DROP TABLE IF EXISTS 
    category,
    course,
    course_section,
    course_section_topic,
    enrollment,
    mentor_detail,
    payment,
    users,
    forgot_password
CASCADE;


-- 1. Create mentor_detail table first (because it is referenced in users)
CREATE TABLE IF NOT EXISTS mentor_detail (
    id SERIAL PRIMARY KEY,
    age INTEGER,
    experience INTEGER,
    qualification VARCHAR(255),
    profession VARCHAR(255),
    profile_pic VARCHAR(255)
);

-- 2. Create users table (depends on mentor_detail)
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    phone_no VARCHAR(255),
    role VARCHAR(255),
    status VARCHAR(255),
    mentor_detail_id INTEGER,
    amount NUMERIC(38, 2) DEFAULT 0,
    CONSTRAINT users_mentor_detail_id_fkey FOREIGN KEY (mentor_detail_id) REFERENCES mentor_detail(id)
);

-- 3. Create category table (independent, no foreign keys)
CREATE TABLE IF NOT EXISTS category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    status VARCHAR(255)
);

-- 4. Create course table (depends on category and users)
CREATE TABLE IF NOT EXISTS course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    prerequisite VARCHAR(255),
    author_course_note VARCHAR(255),
    price NUMERIC(38, 2),
    discount_in_percent INTEGER,
    added_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type VARCHAR(10) CHECK (type IN ('PAID', 'FREE')),
    thumbnail VARCHAR(255),
    status VARCHAR(255),
    category_id INTEGER,
    mentor_id INTEGER,
    CONSTRAINT course_category_id_fkey FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT course_mentor_id_fkey FOREIGN KEY (mentor_id) REFERENCES users(id)
);

-- 5. Create course_section table (depends on course)
CREATE TABLE IF NOT EXISTS course_section (
    id SERIAL PRIMARY KEY,
    section_no VARCHAR(255),
    name VARCHAR(255),
    description VARCHAR(255),
    course_id INTEGER,
    CONSTRAINT course_section_course_id_fkey FOREIGN KEY (course_id) REFERENCES course(id)
);

-- 6. Create course_section_topic table (depends on course_section)
CREATE TABLE IF NOT EXISTS course_section_topic (
    id SERIAL PRIMARY KEY,
    topic_no VARCHAR(255),
    name VARCHAR(255),
    description VARCHAR(255),
    youtube_url VARCHAR(255),
    section_id INTEGER,
    CONSTRAINT course_section_topic_section_id_fkey FOREIGN KEY (section_id) REFERENCES course_section(id)
);

-- 7. Create payment table (depends on users)
CREATE TABLE IF NOT EXISTS payment (
    id SERIAL PRIMARY KEY,
    amount NUMERIC(38, 2),
    enrollment_id VARCHAR(255),
    card_no VARCHAR(255),
    cvv VARCHAR(255),
    expiry_date VARCHAR(255),
    name_on_card VARCHAR(255),
    payment_id VARCHAR(255),
    learner_id INTEGER,
    CONSTRAINT payment_learner_id_fkey FOREIGN KEY (learner_id) REFERENCES users(id)
);

-- 8. Create enrollment table (depends on course and users)
CREATE TABLE enrollment (
    id SERIAL PRIMARY KEY,
    amount NUMERIC(38, 2),
    enrollment_id VARCHAR(255),
    enrollment_time VARCHAR(255),
    status VARCHAR(255),
    course_id INTEGER,
    learner_id INTEGER,
    payment_id INTEGER UNIQUE,

    CONSTRAINT enrollment_course_id_fkey FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT enrollment_learner_id_fkey FOREIGN KEY (learner_id) REFERENCES users(id),
    CONSTRAINT enrollment_payment_id_fkey FOREIGN KEY (payment_id) REFERENCES payment(id)
);

CREATE TABLE forgot_password (
    fpid SERIAL PRIMARY KEY,
    otp INT NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
