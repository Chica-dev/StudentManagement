CREATE TABLE  IF NOT EXISTS students
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(50) NOT NULL,
    furigana     VARCHAR(50) NOT NULL,
    nickname     VARCHAR(100),
    email        VARCHAR(50) NOT NULL,
    city         VARCHAR(100),
    age          INT,
    gender       VARCHAR(50),
    remark       VARCHAR(255),
    is_deleted   TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    student_id         INT NOT NULL,
    course             VARCHAR(50) NOT NULL,
    start_date         TIMESTAMP,
    expected_end_date  TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS application_statuses
(
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    students_courses_id  INT NOT NULL,
    status               VARCHAR(20) NOT NULL,
    FOREIGN KEY (students_courses_id) REFERENCES students_courses(id)
    );

-- シーケンスを既存の最大IDより後ろに調整(次のINSERTでID重複しないように)
-- ALTER TABLE students ALTER COLUMN id RESTART WITH 14;
-- ALTER TABLE students_courses ALTER COLUMN id RESTART WITH 17;