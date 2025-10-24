# Quick Start

Follow these few simple steps to add Mailtrap API functionality into your Java project.

# Prerequisites

Make sure your project uses JDK 11 or higher.

## Setup

### 1. Add the library as a Maven/Gradle dependency

Maven dependency:

```xml

<dependency>
    <groupId>io.mailtrap</groupId>
    <artifactId>mailtrap-java</artifactId>
    <version>1.1.0</version>
</dependency>
```

Gradle Groovy dependency:

```groovy
implementation 'io.mailtrap:mailtrap-java:1.1.0'
```

Gradle Kotlin DSL dependency:

```kotlin
implementation("io.mailtrap:mailtrap-java:1.1.0")
```

### 2. Authentication

- Register/login into Mailtrap
- Obtain an API token [with respective permissions and access level](https://help.mailtrap.io/article/103-api-tokens) to configure and use MailtrapClient.

### 3. Configuration and usage

```java
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;

public class Minimal {

    private static final String TOKEN = "<YOUR MAILTRAP TOKEN>";
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";

    public static void main(String[] args) {
        final MailtrapConfig config = new MailtrapConfig.Builder()
                .token(TOKEN)
                .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        final MailtrapMail mail = MailtrapMail.builder()
                .from(new Address(SENDER_EMAIL))
                .to(List.of(new Address(RECIPIENT_EMAIL)))
                .subject("Hello from Mailtrap Sending!")
                .text("Welcome to Mailtrap!")
                .build();

        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }
    }
}
```
