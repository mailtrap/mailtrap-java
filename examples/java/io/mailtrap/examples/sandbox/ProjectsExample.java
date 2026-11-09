package io.mailtrap.examples.sandbox;

import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.projects.ProjectRequest;

public class ProjectsExample {

    private static final String TOKEN = System.getenv("MAILTRAP_API_KEY");
    private static final long ACCOUNT_ID = Long.parseLong(System.getenv("MAILTRAP_ACCOUNT_ID"));

    public static void main(String[] args) {
        final var config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final var sandboxClient = MailtrapClientFactory.createMailtrapClient(config).sandboxApi();

        final var projects = sandboxClient.projects().getProjects(ACCOUNT_ID);

        if (!projects.isEmpty()) {
            long firstProjectId = projects.get(0).getId();

            final var updatedProject =
                    sandboxClient.projects().updateProject(ACCOUNT_ID, firstProjectId, new ProjectRequest(new ProjectRequest.ProjectData("mock project")));
            System.out.println(updatedProject);

            final var deletedProject = sandboxClient.projects().deleteProject(ACCOUNT_ID, firstProjectId);
            System.out.println(deletedProject);
        }
    }
}
