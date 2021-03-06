-- MySQL Script generated by MySQL Workbench
-- 11/22/17 01:47:31
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema rss_feed
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema rss_feed
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `rss_feed` DEFAULT CHARACTER SET utf8 ;
USE `rss_feed` ;

-- -----------------------------------------------------
-- Table `rss_feed`.`feed_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rss_feed`.`feed_item` (
  `id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
  `description` LONGTEXT NULL DEFAULT NULL,
  `logo` LONGBLOB NULL DEFAULT NULL,
  `publication_date` DATETIME NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `update_date` DATETIME NULL DEFAULT NULL,
  `uri` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
