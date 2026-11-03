package io.mailtrap.api.messages;

import io.mailtrap.model.request.accountaccesses.ListMessagesQueryParams;
import io.mailtrap.model.request.messages.ForwardMessageRequest;
import io.mailtrap.model.request.messages.UpdateMessageRequest;
import io.mailtrap.model.response.messages.*;

import java.util.List;

/**
 * Interface representing the Mailtrap Sandbox API for interaction with email messages
 */
public interface Messages {

    /**
     * Get email message by ID
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return email message with its attributes
     */
    MessageResponse getMessage(long accountId, long sandboxId, long messageId);

    /**
     * Update message attributes
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @param request   attributes to update
     * @return email message with its updated attributes
     */
    MessageResponse updateMessage(long accountId, long sandboxId, long messageId, UpdateMessageRequest request);

    /**
     * Delete message from sandbox
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return attributes of the deleted message
     */
    MessageResponse deleteMessage(long accountId, long sandboxId, long messageId);

    /**
     * @param accountId   unique account ID
     * @param sandboxId   unique sandbox ID
     * @param queryParams additional query parameters
     * @return list of messages
     */
    List<MessageResponse> getMessages(long accountId, long sandboxId, ListMessagesQueryParams queryParams);

    /**
     * Forward message to an email address. The email address must be confirmed by the recipient in advance
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @param request   email where to forward message
     * @return confirmation about forwarding
     */
    ForwardMessageResponse forwardMessage(long accountId, long sandboxId, long messageId, ForwardMessageRequest request);

    /**
     * Get a brief spam report by message ID
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return message spam analysis report
     */
    MessageSpamScoreResponse getSpamScore(long accountId, long sandboxId, long messageId);

    /**
     * Get a brief HTML report by message ID
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return message HTML analysis report
     */
    MessageHtmlAnalysisResponse getMessageHtmlAnalysis(long accountId, long sandboxId, long messageId);

    /**
     * Get text email body, if it exists
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return message body in txt format
     */
    String getTextMessage(long accountId, long sandboxId, long messageId);

    /**
     * Get raw email body
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return raw message body
     */
    String getRawMessage(long accountId, long sandboxId, long messageId);

    /**
     * Get HTML source of email
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return HTML source of a message
     */
    String getMessageSource(long accountId, long sandboxId, long messageId);

    /**
     * Get formatted HTML email body. Not applicable for plain text emails
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return message body in html format
     */
    String getHtmlMessage(long accountId, long sandboxId, long messageId);

    /**
     * Get email message in .eml format
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return .eml of the message
     */
    String getMessageAsEml(long accountId, long sandboxId, long messageId);

    /**
     * Get mail headers of a message
     *
     * @param accountId unique account ID
     * @param sandboxId unique sandbox ID
     * @param messageId unique message ID
     * @return mail headers of the message
     */
    MessageHeadersResponse getMailHeaders(long accountId, long sandboxId, long messageId);

}
