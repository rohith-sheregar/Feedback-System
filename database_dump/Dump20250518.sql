CREATE DATABASE  IF NOT EXISTS `feedback_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `feedback_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: feedback_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','admin','ADMIN');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `course_code` varchar(255) DEFAULT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `dept_id` bigint NOT NULL,
  `semester` int NOT NULL,
  PRIMARY KEY (`course_id`),
  KEY `dept_id` (`dept_id`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'BCS401','Analysis & Design of Algorithms',1,4),(2,'BCS402','Microcontrollers',1,4),(3,'BCS403','Database Management Systems',1,4),(4,'BCS405B','Graph Theory',1,4),(5,'BCS456C','UI/UX',1,4),(6,'BBOC407','Biology For Computer Engineers',1,4),(7,'BUHK408','Universal Human Values Course',1,4),(10,'BCS401','ADA',1,4);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_assignment`
--

DROP TABLE IF EXISTS `course_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_assignment` (
  `assign_id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `section_id` bigint NOT NULL,
  `faculty_id` bigint NOT NULL,
  PRIMARY KEY (`assign_id`),
  KEY `course_id` (`course_id`),
  KEY `section_id` (`section_id`),
  KEY `faculty_id` (`faculty_id`),
  CONSTRAINT `course_assignment_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `course_assignment_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `course_assignment_ibfk_3` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`faculty_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_assignment`
--

LOCK TABLES `course_assignment` WRITE;
/*!40000 ALTER TABLE `course_assignment` DISABLE KEYS */;
INSERT INTO `course_assignment` VALUES (2,1,24,1),(3,2,24,2),(4,3,24,3),(6,4,24,4);
/*!40000 ALTER TABLE `course_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Computer Science'),(2,'Electronics'),(3,'Mechanical'),(4,'Civil Engineering');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faculty`
--

DROP TABLE IF EXISTS `faculty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculty` (
  `faculty_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'FACULTY',
  PRIMARY KEY (`faculty_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculty`
--

LOCK TABLES `faculty` WRITE;
/*!40000 ALTER TABLE `faculty` DISABLE KEYS */;
INSERT INTO `faculty` VALUES (1,'Ms. Reshma','reshma@example.com','hashed_password1','FACULTY'),(2,'Mr. Chandrashekhar Kuthyar','chandra@example.com','hashed_password2','FACULTY'),(3,'Ms. Preethi M','preethi@example.com','hashed_password3','FACULTY'),(4,'Dr. Soumya J Bhat','soumya@example.com','hashed_password4','FACULTY'),(5,'Ms. Sowmya NH','sowmya@example.com','hashed_password5','FACULTY'),(6,'Ms. Swathi','swathi@example.com','hashed_password6','FACULTY'),(7,'Ms. Yashaswini A S','yashaswini@example.com','hashed_password7','FACULTY');
/*!40000 ALTER TABLE `faculty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_answer`
--

DROP TABLE IF EXISTS `feedback_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_answer` (
  `answer_id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` bigint NOT NULL,
  `question_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `answer_text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`answer_id`),
  KEY `session_id` (`session_id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `feedback_answer_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `feedback_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `feedback_answer_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `feedback_answer_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_answer`
--

LOCK TABLES `feedback_answer` WRITE;
/*!40000 ALTER TABLE `feedback_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_comment`
--

DROP TABLE IF EXISTS `feedback_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` bigint NOT NULL,
  `raw_text` longtext,
  `nlp_summary` longtext,
  PRIMARY KEY (`comment_id`),
  UNIQUE KEY `session_id` (`session_id`),
  CONSTRAINT `feedback_comment_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `feedback_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_comment`
--

LOCK TABLES `feedback_comment` WRITE;
/*!40000 ALTER TABLE `feedback_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_session`
--

DROP TABLE IF EXISTS `feedback_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_session` (
  `session_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `section_id` bigint NOT NULL,
  `faculty_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`),
  KEY `student_id` (`student_id`),
  KEY `course_id` (`course_id`),
  KEY `section_id` (`section_id`),
  KEY `faculty_id` (`faculty_id`),
  CONSTRAINT `feedback_session_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `feedback_session_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `feedback_session_ibfk_3` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `feedback_session_ibfk_4` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`faculty_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_session`
--

LOCK TABLES `feedback_session` WRITE;
/*!40000 ALTER TABLE `feedback_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` bigint NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `section` (
  `section_id` bigint NOT NULL AUTO_INCREMENT,
  `section_name` varchar(255) DEFAULT NULL,
  `dept_id` bigint DEFAULT NULL,
  `semester` int NOT NULL,
  PRIMARY KEY (`section_id`),
  UNIQUE KEY `uq_section_unique` (`dept_id`,`semester`,`section_name`),
  UNIQUE KEY `uq_section_combo` (`dept_id`,`semester`,`section_name`),
  CONSTRAINT `fk_section_dept` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (23,'C',1,3),(24,'B',1,4);
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `student_id` bigint NOT NULL AUTO_INCREMENT,
  `usn` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `dept_id` bigint NOT NULL,
  `semester` int NOT NULL,
  `section_id` bigint NOT NULL,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `usn` (`usn`),
  UNIQUE KEY `email` (`email`),
  KEY `dept_id` (`dept_id`),
  KEY `section_id` (`section_id`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`),
  CONSTRAINT `student_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (195,'4MW23CS066','MANOJ KUMAR','manoj.23cs066@sode-edu.in','manoj',1,4,24),(196,'4MW23CS067','MANVITHA','manvitha.23cs067@sode-edu.in','manvitha',1,4,24),(197,'4MW23CS068','MANVITHA PAI K','manvitha.23cs068@sode-edu.in','manvitha',1,4,24),(198,'4MW23CS069','MANYA','manya.23cs069@sode-edu.in','manya',1,4,24),(199,'4MW23CS070','MANYA PATKAR','manya.23cs070@sode-edu.in','manya',1,4,24),(200,'4MW23CS071','MILANRAJ','milanraj.23cs071@sode-edu.in','milanraj',1,4,24),(201,'4MW23CS072','MOHAMMAD ZEESHAN','mohammad.23cs072@sode-edu.in','zeeshan',1,4,24),(202,'4MW23CS073','MOHAMMED NAHAL REHMAN','mohammed.23cs073@sode-edu.in','mohammed',1,4,24),(203,'4MW23CS074','MOHAMMED WAQAS','mohammed.23cs074@sode-edu.in','mohammed',1,4,24),(204,'4MW23CS075','MOHD ARMAN','mohd.23cs075@sode-edu.in','mohd',1,4,24),(205,'4MW23CS076','MOHITH RAJ K KOTIAN','mohith.23cs076@sode-edu.in','mohith',1,4,24),(206,'4MW23CS077','MOULYA M','moulya.23cs077@sode-edu.in','moulya',1,4,24),(207,'4MW23CS078','N NAMANA ADIGA','n.23cs078@sode-edu.in','n',1,4,24),(208,'4MW23CS079','NANDITHA','nanditha.23cs079@sode-edu.in','nanditha',1,4,24),(209,'4MW23CS080','NAYANA K','nayana.23cs080@sode-edu.in','nayana',1,4,24),(210,'4MW23CS081','NHA R M','nha.23cs081@sode-edu.in','nha',1,4,24),(211,'4MW23CS082','NIKHIL G SHETTY','nikhil.23cs082@sode-edu.in','nikhil',1,4,24),(212,'4MW23CS083','NIKHIL KULAL','nikhil.23cs083@sode-edu.in','nikhil',1,4,24),(213,'4MW23CS084','NIKHITA YALLAPPA SAVANOOR','nikhita.23cs084@sode-edu.in','nikhita',1,4,24),(214,'4MW23CS085','NIKITHA','nikitha.23cs085@sode-edu.in','nikitha',1,4,24),(215,'4MW23CS086','NIMISHAMBA M','nimishamba.23cs086@sode-edu.in','nimishamba',1,4,24),(216,'4MW23CS087','NISHITH RAMESH POOJARY','nishith.23cs087@sode-edu.in','nishith',1,4,24),(217,'4MW23CS088','NISHMITHA','nishmitha.23cs088@sode-edu.in','nishmitha',1,4,24),(218,'4MW23CS089','NITHESH G SHETTY','nithesh.23cs089@sode-edu.in','nithesh',1,4,24),(219,'4MW23CS090','PARINEETA G P','parineeta.23cs090@sode-edu.in','parineeta',1,4,24),(220,'4MW23CS091','PAVAN SUDHEENDRA KULKARNI','pavan.23cs091@sode-edu.in','pavan',1,4,24),(221,'4MW23CS092','POORVIK ACHARY','poorvik.23cs092@sode-edu.in','poorvik',1,4,24),(222,'4MW23CS093','PRADYUMNA SHETTIGAR','pradyumna.23cs093@sode-edu.in','pradyumna',1,4,24),(223,'4MW23CS094','PRAJWAL B R','prajwal.23cs094@sode-edu.in','prajwal',1,4,24),(224,'4MW23CS095','PRAJWAL SHANBHAG','prajwal.23cs095@sode-edu.in','prajwal',1,4,24),(225,'4MW23CS096','PRAKHYATH NAYAK','prakhyath.23cs096@sode-edu.in','prakhyath',1,4,24),(226,'4MW23CS097','PRAKYATH P SHETTY','prakyath.23cs097@sode-edu.in','prakyath',1,4,24),(227,'4MW23CS098','PRASHANTH KULAL','prashanth.23cs098@sode-edu.in','prashanth',1,4,24),(228,'4MW23CS099','PRATHAM P MARAKALA','pratham.23cs099@sode-edu.in','pratham',1,4,24),(229,'4MW23CS100','PRATHAM SHANBHAG','pratham.23cs100@sode-edu.in','pratham',1,4,24),(230,'4MW23CS101','PRATHEEKSHA P RAO','pratheeksha.23cs101@sode-edu.in','pratheeksha',1,4,24),(231,'4MW23CS102','PRATHIKSHA','prathiksha.23cs102@sode-edu.in','prathiksha',1,4,24),(232,'4MW23CS103','PRATHVI','prathvi.23cs103@sode-edu.in','prathvi',1,4,24),(233,'4MW23CS104','PRITHESH R SHETTY','prithesh.23cs104@sode-edu.in','prithesh',1,4,24),(234,'4MW23CS105','PRIYA LAKSHMI','priya.23cs105@sode-edu.in','priya',1,4,24),(235,'4MW23CS106','PRIYANKA H NAIK','priyanka.23cs106@sode-edu.in','priyanka',1,4,24),(236,'4MW23CS107','PRIYANKA S','priyanka.23cs107@sode-edu.in','priyanka',1,4,24),(237,'4MW23CS108','PRUTHVI SHETTY','pruthvi.23cs108@sode-edu.in','pruthvi',1,4,24),(238,'4MW23CS109','RACHITHA KAMATH','rachitha.23cs109@sode-edu.in','rachitha',1,4,24),(239,'4MW23CS110','RAHUL','rahul.23cs110@sode-edu.in','rahul',1,4,24),(240,'4MW23CS111','RAKSHA','raksha.23cs111@sode-edu.in','raksha',1,4,24),(241,'4MW23CS112','RAKSHA P','raksha.23cs112@sode-edu.in','raksha',1,4,24),(242,'4MW23CS113','RAKSHITA SATISH RAYAKAR','rakshita.23cs113@sode-edu.in','rakshita',1,4,24),(243,'4MW23CS114','RAKSHITHA S SHEREGAR','rakshitha.23cs114@sode-edu.in','rakshitha',1,4,24),(244,'4MW23CS115','RASHMI SALVANKAR','rashmi.23cs115@sode-edu.in','rashmi',1,4,24),(245,'4MW23CS116','RASHMI V POOJARY','rashmi.23cs116@sode-edu.in','rashmi',1,4,24),(246,'4MW23CS117','RASHMITHA','rashmitha.23cs117@sode-edu.in','rashmitha',1,4,24),(247,'4MW23CS118','RAVIKUMAR','ravikumar.23cs118@sode-edu.in','ravikumar',1,4,24),(248,'4MW23CS119','REYNOL DSOUZA','reynol.23cs119@sode-edu.in','reynol',1,4,24),(249,'4MW23CS120','ROHITH','rohith.23cs120@sode-edu.in','rohith',1,4,24),(250,'4MW23CS122','ROSHNI','roshni.23cs122@sode-edu.in','roshni',1,4,24),(251,'4MW23CS123','S SHARVARI CHATRA','sharvari.23cs123@sode-edu.in','s',1,4,24),(252,'4MW23CS124','SACHIDANANDA','sachidananda.23cs124@sode-edu.in','sachidananda',1,4,24),(253,'4MW23CS125','SAGAR S','sagar.23cs125@sode-edu.in','sagar',1,4,24),(254,'4MW23CS126','SAHANA','sahana.23cs126@sode-edu.in','sahana',1,4,24),(255,'4MW23CS128','SAHANA SHETTY','sahana.23cs128@sode-edu.in','sahana',1,4,24),(256,'4MW23CS129','SAIRAJ SERIGARA','sairaj.23cs129@sode-edu.in','sairaj',1,4,24),(257,'4MW23CS130','SAKSHI HC','sakshi.23cs130@sode-edu.in','sakshi',1,4,24),(258,'4MW23CS131','SAKSHI S KULAL','sakshi.23cs131@sode-edu.in','sakshi',1,4,24);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-18 19:33:34
