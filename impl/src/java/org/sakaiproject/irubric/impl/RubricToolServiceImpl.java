package org.sakaiproject.irubric.impl;

import java.util.List;
import java.util.Iterator;

import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.text.Collator;
import java.util.Date;

import org.sakaiproject.irubric.api.RubricToolService;
import org.sakaiproject.authz.cover.AuthzGroupService;
import org.sakaiproject.authz.cover.FunctionManager;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.authz.api.SecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.gradebook.business.GradebookManager;
import org.sakaiproject.tool.gradebook.Gradebook;
import org.sakaiproject.tool.gradebook.Assignment;
import org.sakaiproject.service.gradebook.shared.GradebookNotFoundException;
import org.sakaiproject.tool.gradebook.iRubric.GradableObjectRubric;

public class RubricToolServiceImpl implements RubricToolService
{
	/** Our logger. */
	private static Log M_log = LogFactory.getLog(RubricToolServiceImpl.class);
	
	private SecurityService securityService;
    private SiteService siteService;
    private GradebookManager gradebookManager;
    

    private static final String SORTED_BY_TITLE = "title";
    private static final String SORTED_BY_DUEDATE = "duedate";

	public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setGradebookManager(GradebookManager gradebookManager) {
        this.gradebookManager = gradebookManager;
    }

	/**
	 * Final initialization, once all dependencies are set.
	 */
	public void init()
	{
		
		M_log.info(this + " init()");

		//register permission
		// register functions
		FunctionManager.registerFunction(SECURE_BUILD_IRUBRIC);
		FunctionManager.registerFunction(SECURE_ACCESS_GALLERY_IRUBRIC);
		FunctionManager.registerFunction(SECURE_COLLABORATIVE_ASSESSMENTS_IRUBRIC);
		//FunctionManager.registerFunction(SECURE_REPORT_IRUBRIC);
 		
	} // init

	/**
	 * Returns to uninitialized state.
	 */
	public void destroy()
	{
		M_log.info(this + " destroy()");
	}

	//check allow build irubric
	public boolean allowBuildiRubric(String userid, String resourceString) {
		
		// checking allow at the site
		if (unlockCheck(userid, SECURE_BUILD_IRUBRIC, resourceString)) 
			return true;
		return false;
	}

	//check allow access gallery
	public boolean allowAccessGalleryiRubric(String userid, String resourceString) {
		// checking allow at the site level
		if (unlockCheck(userid, SECURE_ACCESS_GALLERY_IRUBRIC, resourceString)) 
			return true;
		return false;
	}
	/*
	//check allow view report
	public boolean allowReportiRubric(String userid, String resourceString) {
		// checking allow at the site level
		if (unlockCheck(userid, SECURE_REPORT_IRUBRIC, resourceString)) 
			return true;
		return false;
	}
	*/
	//check allow access Assessment
	public boolean allowAssessmentiRubric(String userid, String resourceString) {
		// checking allow at the site level
		if (unlockCheck(userid, SECURE_COLLABORATIVE_ASSESSMENTS_IRUBRIC, resourceString)) 
			return true;
		return false;
	}


	/**
	 * Check security permission.
	 * @param userid - The user id string.
	 * @param lock - The lock id string.
	 * @param resource - The resource reference string, or null if no resource is involved.
	 * @return true if allowed, false if not
	 */
	protected boolean unlockCheck(String userid, String lock, String resource)
	{	
		boolean hasPerm = securityService.unlock(userid, lock, resource);
		return hasPerm;

	}// unlockCheck

	//get list assignment(gradebook item)
	private List getAssignments(final Long gradebookId) {
		List assignments = gradebookManager.getAssignments(gradebookId);
		return assignments;
	}

	/*
	*	get gradebookId by gradebookUId
	*	@param gradebookUId - gradebookUId string
	*	return gradebookid(long)
	*/
	private Long getGradebookId(String gradebookUId) {

		Gradebook gradebook = null;
        try {
            gradebook = gradebookManager.getGradebook(gradebookUId);
        } catch (GradebookNotFoundException e) {
            M_log.error("Request made for inaccessible gradebookUid=" + gradebookUId);
            gradebookUId = null;
        }

        if(gradebook == null)
        	throw new IllegalStateException("Gradebook gradebook == null!");
        
        return gradebook.getId();
	}

	/*
	*	get list assignment(gradebook item) by gradebookUId
	*	@param gradebookUId - gradebookUId string
	*	@param	sort- type sort(title, duedate)
	*	@param	ascending - string "true" else "false"
	*	return list assignment
	*/
	public List getAssignmentsByGradebookUId(String gradebookUId, String sort, String ascending) {
		//get gradebookid
		Long gradebookId = getGradebookId(gradebookUId);
		
		//get list assignment(gradebook item)
		List assignments = getAssignments(gradebookId);
		
		//for each object, set value use check attach rubric
		for (Iterator iter = assignments.iterator(); iter.hasNext(); ) {
            Assignment assignment = (Assignment)iter.next();
            Long assignmentId = assignment.getId();
            //if gradebook item have attached rubric then set isRemoved=false, else isRemoved=true
            assignment.setRemoved(!isHaveAttach(assignmentId));

        }
        
        //sort list assignment
        try
		{
			Collections.sort(assignments, new AssignmentComparator(sort, ascending));		
		}
		catch (Exception e)
		{
			// log exception during sorting for helping debugging
			M_log.error("have error sort list,sort=" + sort + " ascending=" + ascending );
		}
		

		return assignments;
	}

	/*
	*	check have attach irubric in gradebook item
	*	@param assignmentid - gradebookitem id (Long)
	*	return true if have attach irubric, false is otherwise
	*/
	private boolean isHaveAttach(Long assignmentId) {

		if( (GradableObjectRubric)gradebookManager.getGradableObjectRubric(assignmentId) != null)
			return true;

		return false;
	}

	/**
	 * the AssignmentComparator clas
	 */
	private class AssignmentComparator implements Comparator
	{
		Collator collator = Collator.getInstance();
		
		/**
		 * the criteria
		 */
		String m_criteria = null;

		/**
		 * the criteria
		 */
		String m_asc = null;


		/**
		 * constructor
		 *
		 * @param state
		 *        The state object
		 * @param criteria
		 *        The sort criteria string
		 * @param asc
		 *        The sort order string. TRUE_STRING if ascending; "false" otherwise.
		 */
		public AssignmentComparator(String criteria, String asc)
		{
			m_criteria = criteria;
			m_asc = asc;

		} // constructor

		/**
		 * implementing the compare function
		 *
		 * @param o1
		 *        The first object
		 * @param o2
		 *        The second object
		 * @return The compare result. 1 is o1 < o2; -1 otherwise
		 */
		public int compare(Object o1, Object o2)
		{
			int result = -1;
			
			if (m_criteria == null)
			{
				m_criteria = SORTED_BY_TITLE;
			}

			if (m_criteria.equals(SORTED_BY_TITLE))
			{
				// sorted by the assignment title
				String s1 = ((Assignment) o1).getName();
				String s2 = ((Assignment) o2).getName();
				result = compareString(s1, s2);
			}
			
			else if (m_criteria.equals(SORTED_BY_DUEDATE))
			{
				// sorted by the assignment due date
				Date t1 = ((Assignment) o1).getDueDate();
				Date t2 = ((Assignment) o2).getDueDate();

				if (t1 == null)
				{
					result = -1;
				}
				else if (t2 == null)
				{
					result = 1;
				}
				else if (t1.before(t2))
				{
					result = -1;
				}
				else
				{
					result = 1;
				}
			}
			
			// sort ascending or descending
			if (!Boolean.valueOf(m_asc))
			{
				result = -result;
			}
			return result;
		}

		private int compareString(String s1, String s2) 
		{
			int result;
			if (s1 == null && s2 == null) {
				result = 0;
			} else if (s2 == null) {
				result = 1;
			} else if (s1 == null) {
				result = -1;
			} else {
				result = collator.compare(s1.toLowerCase(), s2.toLowerCase());
			}
			return result;
		}

	} // DiscussionComparator

}