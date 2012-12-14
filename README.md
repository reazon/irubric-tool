irubric-tool
============

irubric-tool install in sakai

Install sakai-2.8.0:
	
1. Build Gradebook
	Go to https://github.com/reazon/gradebook/tree/tags/sakai-2.9.0-rc03
	Click button "Zip" and download file gradebook
	Extract file gradebook-tags-sakai-2.9.0-rc03.zip into {sakai_root}
	
	$cd {sakai_root}/gradebook-tags-sakai-2.9.0-rc03
	$mvn clean install sakai:deploy
	$cp ~/.m2/repository/org/sakaiproject/edu-services/gradebook/gradebook-service-impl/1.2.0-rc03/gradebook-service-impl-1.2.0-rc03.jar {CATALINA_HOME}/shared/lib
	

2. Build irubric-tool
	$cd {sakai_root}/irubric-tool
	$mvn clean install sakai:deploy

