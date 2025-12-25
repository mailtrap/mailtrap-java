package io.mailtrap.api.projects;

import io.mailtrap.model.request.projects.ProjectRequest;
import io.mailtrap.model.response.projects.DeleteProjectResponse;
import io.mailtrap.model.response.projects.ProjectsResponse;

import java.util.List;

/**
 * Interface representing the Mailtrap Testing API for interaction with projects
 */
public interface Projects {

    /**
     * Create a project
     *
     * @param accountId unique account ID
     * @param request   request data
     * @return Returns attributes of the created project. <b>permissions</b> returns the permissions of the token for the project
     */
    ProjectsResponse createProject(long accountId, ProjectRequest request);

    /**
     * List projects and their inboxes to which the API token has access.
     *
     * @param accountId unique account ID
     * @return the list of projects with nested inboxes. <b>permissions</b> returns the permissions of the token for the project
     */
    List<ProjectsResponse> getProjects(long accountId);

    /**
     * Get the project and its inboxes
     *
     * @param accountId unique account ID
     * @param projectId unique project ID
     * @return project attributes and inboxes of this project with their attributes. <b>permissions</b> returns the permissions of the token for the project
     */
    ProjectsResponse getProject(long accountId, long projectId);

    /**
     * Update project data
     *
     * @param accountId     unique account ID
     * @param projectId     unique project ID
     * @param updateRequest request data
     * @return project attributes and inboxes of this project with their attributes. <b>permissions</b> returns the permissions of the token for the project
     */
    ProjectsResponse updateProject(long accountId, long projectId, ProjectRequest updateRequest);

    /**
     * Delete project and its inboxes
     *
     * @param accountId unique account ID
     * @param projectId unique project ID
     * @return id of the deleted project
     */
    DeleteProjectResponse deleteProject(long accountId, long projectId);

}
