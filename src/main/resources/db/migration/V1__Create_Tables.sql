-- Table for users
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                       `user_name` VARCHAR(255) DEFAULT NULL,
                                       `full_name` VARCHAR(255) DEFAULT NULL,
                                       `password` VARCHAR(255) DEFAULT NULL,
                                       `phone` VARCHAR(20) DEFAULT NULL,
                                       `email` VARCHAR(100) DEFAULT NULL,
                                       `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       `account_non_expired` BIT(1) DEFAULT NULL,
                                       `account_non_locked` BIT(1) DEFAULT NULL,
                                       `credentials_non_expired` BIT(1) DEFAULT NULL,
                                       `enabled` BIT(1) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_name` (`user_name`)
) ENGINE=InnoDB;

-- Table for coaches
CREATE TABLE IF NOT EXISTS `coaches` (
                                         `user_id` BIGINT(20) PRIMARY KEY,
                                         `certification` VARCHAR(100) NOT NULL,
                                         `hired_date` DATE NOT NULL,
                                         FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

-- Table for clients
CREATE TABLE IF NOT EXISTS `clients` (
                                         `user_id` BIGINT(20) PRIMARY KEY,
                                         `coach_id` BIGINT(20),
                                         FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                                         FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`user_id`) ON DELETE SET NULL
);

-- Table for exercise groups
CREATE TABLE IF NOT EXISTS `exercise_groups` (
                                                 `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
                                                 `name` VARCHAR(100) NOT NULL
);

-- Table for exercises
CREATE TABLE IF NOT EXISTS `exercises` (
                                           `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
                                           `name` VARCHAR(100) NOT NULL,
                                           `video_url` VARCHAR(255),
                                           `group_id` BIGINT(20) NOT NULL,
                                           FOREIGN KEY (`group_id`) REFERENCES `exercise_groups` (`id`) ON DELETE CASCADE
);

-- Table for training sessions
CREATE TABLE IF NOT EXISTS `training_sessions` (
                                                   `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
                                                   `client_id` BIGINT(20) NOT NULL,
                                                   `coach_id` BIGINT(20) NOT NULL,
                                                   `name` VARCHAR(100) NOT NULL,
                                                   `start_date` DATE NOT NULL,
                                                   `status` BIT(1) DEFAULT 1,
                                                   FOREIGN KEY (`client_id`) REFERENCES `clients` (`user_id`) ON DELETE CASCADE,
                                                   FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`user_id`) ON DELETE CASCADE
);

-- Table for session exercises
CREATE TABLE IF NOT EXISTS `session_exercises` (
                                                   `session_id` BIGINT(20) NOT NULL,
                                                   `exercise_id` BIGINT(20) NOT NULL,
                                                   `sequence` INT NOT NULL,
                                                   `sets` INT NOT NULL,
                                                   `reps` INT NOT NULL,
                                                   `weight` FLOAT,
                                                   PRIMARY KEY (`session_id`, `exercise_id`),
                                                   FOREIGN KEY (`session_id`) REFERENCES `training_sessions` (`id`) ON DELETE CASCADE,
                                                   FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE
);

-- Table for payments
CREATE TABLE IF NOT EXISTS `payments` (
                                          `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
                                          `client_id` BIGINT(20) NOT NULL,
                                          `amount` DECIMAL(10,2) NOT NULL,
                                          `discount` DECIMAL(10,2) DEFAULT 0.00,
                                          `total` DECIMAL(10,2) NOT NULL,
                                          `payment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          `due_date` DATE NOT NULL,
                                          `status` ENUM('paid', 'unpaid') DEFAULT 'unpaid',
                                          FOREIGN KEY (`client_id`) REFERENCES `clients` (`user_id`) ON DELETE CASCADE
);
