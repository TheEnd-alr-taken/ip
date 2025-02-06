package motiva;

/**
 * Represents an exception specific to the Motiva application.
 */
public class MotivaException extends Exception {

    /**
     * Constructs a new MotivaException with the specified detail message.
     *
     * @param message The detail message of the exception.
     */
    public MotivaException(String message) {
        super(message);
    }
}
