package constants;

public final class MessageExceptions {


    private MessageExceptions() {
        throw new IllegalStateException(MSG_UTILITY_CLASS);
    }

    public static final String MSG_NOT_FOUND = "No results found for %s: '%s'.";
    public static final String MSG_UTILITY_CLASS = "The entity with identifier %s already exists.";
    public static final String MSG_AUTH_SERVICE_ERROR = "Authentication service error: %s";
    public static final String MSG_UNAUTHORIZED = "Authentication required or token invalid";
    public static final String MSG_FORBIDDEN = "Oops! It looks like you’re trying to access a restricted area. Unfortunately, you don’t have the proper clearance. Better luck next time!";
    public static final String MSG_SQS_MESSAGE_ERROR = "Error sending message to SQS: %s";
    public static final String MSG_SQS_ENDEUDAMIENTO = "Error evaluating debt status: %s";
}
