irubric-tool
============

irubric-tool install in sakai

Install sakai-2.8.0:
1. Build Gradebook
	Go to https://github.com/reazon/gradebook/tree/sakai-2.8.0
	Click button "Zip" and download file gradebook
	Extract file gradebook-sakai-2.8.0.zip into {sakai_root}
	
	Command line:
	$cd {sakai-root}/gradebook-sakai-2.8.0
	$mvn clean install sakai:deploy
	$cp ~/.m2/repository/org/sakaiproject/edu-services/gradebook/gradebook-service-impl/1.1.1/gradebook-service-impl-1.1.1.jar {CATALINA_HOME}/shared/lib


2. Build irubric-tool
	$cd {sakai_root}/irubric-tool
	$mvn clean install sakai:deploy


