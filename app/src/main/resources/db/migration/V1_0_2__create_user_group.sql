CREATE TABLE IF NOT EXISTS `user_group`
(
    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(100) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;
