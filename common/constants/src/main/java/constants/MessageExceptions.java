package constants;

public final class MessageExceptions {


    private MessageExceptions() {
        throw new IllegalStateException(MSG_UTILITY_CLASS);
    }

    public static final String MSG_NOT_FOUND = "No results found for %s: '%s'.";
    public static final String MSG_UTILITY_CLASS = "The entity with identifier %s already exists.";
    public static final String MSG_AUTH_SERVICE_ERROR = "Authentication service error: %s";

}
