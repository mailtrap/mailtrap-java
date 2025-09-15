package io.mailtrap.examples.testing;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.projects.CreateUpdateProjectRequest;

public class ProjectsExample {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final long ACCOUNT_ID = 1L;

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var testingClient = MailtrapClientFactory.createMailtrapClient(config).testingApi();

        var projects = testingClient.projects().getProjects(ACCOUNT_ID);

        if (!projects.isEmpty()) {
            long firstProjectId = projects.get(0).getId();

            var updatedProject =
                    testingClient.projects().updateProject(ACCOUNT_ID, firstProjectId, new CreateUpdateProjectRequest(new CreateUpdateProjectRequest.ProjectData("mock project")));
            System.out.println(updatedProject);

            var deletedProject = testingClient.projects().deleteProject(ACCOUNT_ID, firstProjectId);
            System.out.println(deletedProject);
        }
    }
}
