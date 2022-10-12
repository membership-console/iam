CREATE TABLE IF NOT EXISTS `password_reset_token`
(
    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`    INT UNSIGNED NOT NULL,
    `token`      CHAR(36)     NOT NULL,
    `expire_at`  DATETIME     NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `fk_password_reset_token_user_id_idx` (`user_id` ASC) VISIBLE,
    UNIQUE INDEX `token_UNIQUE` (`token` ASC) VISIBLE,
    CONSTRAINT `fk_password_reset_token_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `user` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB