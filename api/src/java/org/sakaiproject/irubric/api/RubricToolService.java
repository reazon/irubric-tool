package org.sakaiproject.irubric.api;

import java.util.List;

import java.lang.String;
import org.sakaiproject.entity.api.EntityProducer;

/**
 * <p>
 * RubricToolService is the service that handles irubric.
 * </p>
 * 
 */
public interface RubricToolService
{

	/** Security lock for build irubric. */
	public static final String SECURE_BUILD_IRUBRIC = "irubric.build";

	/** Security lock for gallery rurbic. */
	public static final String SECURE_ACCESS_GALLERY_IRUBRIC = "irubric.accessgallery";

	/** Security lock for assessment. */
	public static final String SECURE_COLLABORATIVE_ASSESSMENTS_IRUBRIC = "irubric.collaborativeassessments";

	//check permission build rubric
	public boolean allowBuildiRubric(String userid, String resourceString);

	//check permission access gallery irubric
	public boolean allowAccessGalleryiRubric(String userid, String resourceString);

	//check permission access Assessment
	public boolean allowAssessmentiRubric(String userid, String resourceString);

	//get list assignment (gradebook item)
	public List getAssignmentsByGradebookUId(String gradebookUId, String sort, String ascending);
	
}