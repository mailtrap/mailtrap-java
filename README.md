![Java](https://badgen.net/badge/icon/Java?icon=java&label=) 

# Official Mailtrap Java client

This SDK offers integration with the [official API](https://api-docs.mailtrap.io/) for [Mailtrap](https://mailtrap.io).

Quickly add email sending functionality to your Java application with Mailtrap.

## Java Version

Requires JDK 11 or higher.

## Install package

As Maven dependency:

```xml

<dependency>
    <groupId>io.mailtrap</groupId>
    <artifactId>mailtrap-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

As Gradle Groovy dependency:

```groovy
implementation 'io.mailtrap:mailtrap-java:1.0.0'
```

As Gradle Kotlin DSL dependency:

```kotlin
implementation("io.mailtrap:mailtrap-java:1.0.0")
```

## Usage

### Minimal

```java
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;

public class MailtrapJavaSDKTest {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";
    private static final String REPLY_TO_EMAIL = "reply_to@domain.com";

    public static void main(String[] args) {
        final MailtrapConfig config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        final MailtrapMail mail = MailtrapMail.builder()
                .from(new Address(SENDER_EMAIL))
                .to(List.of(new Address(RECIPIENT_EMAIL)))
                .replyTo(new Address(REPLY_TO_EMAIL, "Vincent Vega"))
                .subject("Hello from Mailtrap Sending!")
                .text("Welcome to Mailtrap Sending!")
                .build();

        // Send email using Mailtrap Sending API
        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }

        // Or send email using Mailtrap Testing API
        try {
            long inboxId = 1000001L;

            // Either instantiate a new client
            MailtrapClient sandboxClient = MailtrapClientFactory.createMailtrapClient(
                    new MailtrapConfig.Builder()
                            .sandbox(true)
                            .inboxId(inboxId)
                            .token(TOKEN)
                            .build());

            System.out.println(sandboxClient.send(mail));

            // Or reuse already created client
            client.switchToEmailTestingApi(inboxId);

            System.out.println(client.send(mail));

            // Or use Testing API directly
            System.out.println(client.testingApi().emails().send(mail, inboxId));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }
    }
}
```

Refer to the [`examples`](examples) folder for the source code of this and other advanced examples.

### API Reference

You can find the API reference [here](https://mailtrap.github.io/mailtrap-java/index.html).

### General API

- [List User & Invite account accesses](examples/java/io/mailtrap/examples/general/AccountAccessExample.java)
- [Remove account access](examples/java/io/mailtrap/examples/general/AccountsExample.java)
- [Permissions](examples/java/io/mailtrap/examples/general/PermissionsExample.java)
- [Current billing usage cycle](examples/java/io/mailtrap/examples/general/BillingExample.java)

### Sending API

- [Advanced](examples/java/io/mailtrap/examples/sending/EverythingExample.java)
- [Minimal](examples/java/io/mailtrap/examples/sending/MinimalExample.java)
- [Error handling](examples/java/io/mailtrap/examples/sending/ErrorsExample.java)
- [Send email using template](examples/java/io/mailtrap/examples/sending/TemplateExample.java)

### Email Testing API

- [Attachments](examples/java/io/mailtrap/examples/testing/AttachmentsExample.java)
- [Inboxes](examples/java/io/mailtrap/examples/testing/InboxesExample.java)
- [Messages](examples/java/io/mailtrap/examples/testing/MessagesExample.java)
- [Projects](examples/java/io/mailtrap/examples/testing/ProjectsExample.java)
- [Send mail using template](examples/java/io/mailtrap/examples/testing/EmailExample.java)

### Bulk Sending API

- [Send mail](examples/java/io/mailtrap/examples/bulk/BulkSendExample.java)

### Contacts API

- [Contacts](examples/java/io/mailtrap/examples/contacts/ContactsExample.java)
- [Contact lists](examples/java/io/mailtrap/examples/contactlists/ContactListsExample.java)

## Contributing

Bug reports and pull requests are welcome on [GitHub](https://github.com/mailtrap/mailtrap-java). This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the [code of conduct](CODE_OF_CONDUCT.md).

## License

The package is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

## Code of Conduct

Everyone interacting in the Mailtrap project's codebases, issue trackers, chat rooms and mailing lists is expected to follow the [code of conduct](CODE_OF_CONDUCT.md).
