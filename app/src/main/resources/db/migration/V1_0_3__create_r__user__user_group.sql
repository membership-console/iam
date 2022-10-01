CREATE TABLE IF NOT EXISTS `r__user__user_group`
(
    `user_id`       INT UNSIGNED NOT NULL,
    `user_group_id` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`, `user_group_id`),
    CONSTRAINT `fk_r__user__user_group_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `user` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_r__user__user_group_user_group_id`
        FOREIGN KEY (`user_group_id`)
            REFERENCES `user_group` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;
