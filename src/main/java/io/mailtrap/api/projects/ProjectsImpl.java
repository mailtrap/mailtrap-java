package io.mailtrap.api.projects;

import io.mailtrap.Constants;
import io.mailtrap.CustomValidator;
import io.mailtrap.api.apiresource.ApiResourceWithValidation;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.projects.CreateUpdateProjectRequest;
import io.mailtrap.model.response.projects.DeleteProjectResponse;
import io.mailtrap.model.response.projects.ProjectsResponse;

import java.util.List;

public class ProjectsImpl extends ApiResourceWithValidation implements Projects {

    public ProjectsImpl(final MailtrapConfig config, final CustomValidator customValidator) {
        super(config, customValidator);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public ProjectsResponse createProject(final long accountId, final CreateUpdateProjectRequest request) {

        validateRequestBodyAndThrowException(request);

        return httpClient.post(
            String.format(apiHost + "/api/accounts/%d/projects", accountId),
            request,
            new RequestData(),
            ProjectsResponse.class
        );
    }

    @Override
    public List<ProjectsResponse> getProjects(final long accountId) {
        return httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/projects", accountId),
            new RequestData(),
            ProjectsResponse.class
        );
    }

    @Override
    public ProjectsResponse getProject(final long accountId, final long projectId) {
        return httpClient.get(
            String.format(apiHost + "/api/accounts/%d/projects/%d", accountId, projectId),
            new RequestData(),
            ProjectsResponse.class
        );
    }

    @Override
    public ProjectsResponse updateProject(final long accountId, final long projectId, final CreateUpdateProjectRequest updateRequest) {

        validateRequestBodyAndThrowException(updateRequest);

        return httpClient.patch(
            String.format(apiHost + "/api/accounts/%d/projects/%d", accountId, projectId),
            updateRequest,
            new RequestData(),
            ProjectsResponse.class
        );
    }

    @Override
    public DeleteProjectResponse deleteProject(final long accountId, final long projectId) {
        return httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/projects/%d", accountId, projectId),
            new RequestData(),
            DeleteProjectResponse.class
        );
    }

}
