package io.mailtrap.model.request.emails;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class BatchEmailBase {

    private Address from;

    @JsonProperty("reply_to")
    private Address replyTo;

    @Size(min = 1)
    private String subject;

    /**
     * Can be used alongside with {@link #html} as a fallback option.
     * Required in the absence of {@link #html}
     */
    private String text;

    /**
     * Can be used alongside with {@link #text} as a fallback option.
     * Required in the absence of {@link #text}
     */
    private String html;

    @Valid
    private List<EmailAttachment> attachments;

    /**
     * 'Content-Transfer-Encoding' header will be ignored and replaced with 'quoted-printable'
     */
    private Map<String, String> headers;

    @Size(max = 255)
    private String category;

    /**
     * Values that are specific to the entire send that will be carried along with the email and its activity data
     */
    @JsonProperty("custom_variables")
    private Map<String, String> customVariables;

    /**
     * Should NOT be used if {@link #subject}, {@link #text} or {@link #html} is used
     */
    @JsonProperty("template_uuid")
    private String templateUuid;

    /**
     * Should be sent if {@link #templateUuid} is used
     */
    @JsonProperty("template_variables")
    private Map<String, Object> templateVariables;

}
