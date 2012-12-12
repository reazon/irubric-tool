irubric-tool
============

irubric-tool install in sakai

Install sakai-2.8.1:
1. Build Gradebook
	
	

2. Build irubric-tool
	$cd {sakai_root}/irubric-tool
	$mvn clean install sakai:deploy

        
3. Insert database:

+Database:

	insert into SAKAI_REALM_FUNCTION values(DEFAULT,"irubric.collaborativeassessments");
	insert into SAKAI_REALM_FUNCTION values(DEFAULT,"irubric.accessgallery");
	insert into SAKAI_REALM_FUNCTION values(DEFAULT,"irubric.build");
