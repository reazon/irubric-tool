irubric-tool
============

irubric-tool install in sakai

Install sakai-2.8.0:
1. Build Gradebook
	Go to https://github.com/reazon/gradebook/tree/sakai-2.8.0
	Click button "Zip" and download file gradebook
	Extract file {sakai_root}
	
	$cd {sakai-root}/gradebook-sakai-2.8.0
	$mvn clean install sakai:deploy
	$cp ~/.m2/repository/org/sakaiproject/edu-services/gradebook/gradebook-service-impl/1.1.1/gradebook-service-impl-1.1.1.jar {CATALINA_HOME}/shared/lib


2. Build irubric-tool
	$cd {sakai_root}/irubric-tool
	$mvn clean install sakai:deploy

        
3. Insert database:

+Database:

	insert into SAKAI_REALM_FUNCTION values(DEFAULT,"irubric.collaborativeassessments");
	insert into SAKAI_REALM_FUNCTION values(DEFAULT,"irubric.accessgallery");
	insert into SAKAI_REALM_FUNCTION values(DEFAULT,"irubric.build");

4. Restart tomcat
	(also copy images rubric.gif and refresh.gif to  /opt/tomcat/webapps/sakai-gradebook-tool/images/)

	$ cp rubric.gif /opt/tomcat/webapps/sakai-gradebook-tool/images/
	$ cp refresh.gif /opt/tomcat/webapps/sakai-gradebook-tool/images/
