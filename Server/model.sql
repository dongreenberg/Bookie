-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 22, 2013 at 06:13 PM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `model`
--
CREATE DATABASE IF NOT EXISTS `model` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `model`;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `number` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `orgnumber` int(11) NOT NULL,
  PRIMARY KEY (`number`),
  KEY `fk_username_admin` (`username`),
  KEY `fk_orgnumber_admin` (`orgnumber`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- Table structure for table `androiddevice`
--

CREATE TABLE IF NOT EXISTS `androiddevice` (
  `username` varchar(16) NOT NULL,
  `device_id` varchar(512) NOT NULL,
  UNIQUE KEY `username` (`username`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

CREATE TABLE IF NOT EXISTS `event` (
  `eventnumber` int(11) NOT NULL AUTO_INCREMENT,
  `orgnumber` int(11) NOT NULL,
  `eventname` varchar(256) DEFAULT NULL,
  `location` varchar(64) DEFAULT NULL,
  `days` varchar(7) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `startyear` int(11) DEFAULT NULL,
  `startmonth` int(11) DEFAULT NULL,
  `startdayofmonth` int(11) DEFAULT NULL,
  `endyear` int(11) DEFAULT NULL,
  `endmonth` int(11) DEFAULT NULL,
  `enddayofmonth` int(11) DEFAULT NULL,
  `starthour` int(11) DEFAULT NULL,
  `startminute` int(11) DEFAULT NULL,
  `endhour` int(11) DEFAULT NULL,
  `endminute` int(11) DEFAULT NULL,
  PRIMARY KEY (`eventnumber`),
  KEY `fk_orgnumber` (`orgnumber`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `organization`
--

CREATE TABLE IF NOT EXISTS `organization` (
  `orgnumber` int(11) NOT NULL AUTO_INCREMENT,
  `orgname` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `contact` varchar(64) NOT NULL,
  PRIMARY KEY (`orgnumber`),
  UNIQUE KEY `orgname` (`orgname`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=27 ;

-- --------------------------------------------------------

--
-- Table structure for table `rsvp`
--

CREATE TABLE IF NOT EXISTS `rsvp` (
  `number` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `eventnumber` int(11) NOT NULL,
  PRIMARY KEY (`number`),
  UNIQUE KEY `username` (`username`,`eventnumber`),
  KEY `fk_eventnumber` (`eventnumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `subscription`
--

CREATE TABLE IF NOT EXISTS `subscription` (
  `number` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `orgnumber` int(11) NOT NULL,
  PRIMARY KEY (`number`),
  KEY `fk_username_subs` (`username`),
  KEY `fk_orgnumber_subs` (`orgnumber`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

-- --------------------------------------------------------

--
-- Table structure for table `term`
--

CREATE TABLE IF NOT EXISTS `term` (
  `termID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(16) NOT NULL,
  `password` varchar(32) NOT NULL,
  `firstname` varchar(32) DEFAULT NULL,
  `lastname` varchar(32) DEFAULT NULL,
  `bmail` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `fk_orgnumber_admin` FOREIGN KEY (`orgnumber`) REFERENCES `organization` (`orgnumber`),
  ADD CONSTRAINT `fk_username_admin` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `androiddevice`
--
ALTER TABLE `androiddevice`
  ADD CONSTRAINT `androiddevice_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `event`
--
ALTER TABLE `event`
  ADD CONSTRAINT `fk_orgnumber` FOREIGN KEY (`orgnumber`) REFERENCES `organization` (`orgnumber`);

--
-- Constraints for table `rsvp`
--
ALTER TABLE `rsvp`
  ADD CONSTRAINT `fk_eventnumber` FOREIGN KEY (`eventnumber`) REFERENCES `event` (`eventnumber`),
  ADD CONSTRAINT `fk_username` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

--
-- Constraints for table `subscription`
--
ALTER TABLE `subscription`
  ADD CONSTRAINT `fk_orgnumber_subs` FOREIGN KEY (`orgnumber`) REFERENCES `organization` (`orgnumber`),
  ADD CONSTRAINT `fk_username_subs` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
