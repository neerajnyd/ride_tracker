CREATE TABLE `ride_tracker`.`ride` ( `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(100) NOT NULL, `duration` INT NOT NULL, PRIMARY KEY (`id`)); 

ALTER TABLE ride ADD ride_date DATETIME AFTER duration;