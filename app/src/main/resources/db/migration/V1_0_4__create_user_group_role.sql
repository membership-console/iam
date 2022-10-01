CREATE TABLE IF NOT EXISTS `user_group_role`
(
    `user_group_id` INT UNSIGNED NOT NULL,
    `role_id`       INT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_group_id`, `role_id`),
    CONSTRAINT `fk_user_group_role_user_group_id`
        FOREIGN KEY (`user_group_id`)
            REFERENCES `user_group` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;
