README.txt
==========
This web application generates and downloads code/projects, for doing CRUD operations
on database tables chosen by the end user ("connected" mode), or from an uploaded
schema script (disconnected mode). This includes a Spring Boot based Rest application
per table, and a front-end SPA (Single Page Application) front-end which calls the
remote CRUD Rest services.

SUPPORTED DBs: MySQL
PLANNED DBs TO SUPPORT: Oracle, DB2, MongoDB

***NOTE: This is NOT open source. This application is intended to be used as a SAAS
solution and is intellectual property of  makecodeforme.com, copyright 2020.
======================================================================================

TODO
====
-CRUD Boot App functionality
	-high priority
		-choose options
			-base package validations
				-max length: 1000
				-must start with a character
			-java version: change to a drop down, with 5-14 (default to 8: most commonly used)
		-schema script validations
	 		-Must NOT contain "drop database" or "drop table"
	 		-Must NOT contain: "delete from", "delete * from", "insert into"
		-Domain model
			-PK
				-Only add @GeneratedValue if (DB specific): MySql - columns.extra = 'auto_increment', Oracle: Sequence
					-strategy attribute of @GeneratedValue should be app specific too ***See if JPA API exists to make domain class
				-Add support for composite keys, which should include update to repo class
		-Add cleanup cron job which drops DB's/schemas older than 1 hour
	-low priority
		-XML or JSON responses
		-Common user chosen validations for create/edit
		-use velocity template for view & partials
-Front end CRUD App
	-Start Angular impl
	
-refactorings
	-validation logic to validators
	-application.properties: extract entries to multiple files
-
-
-