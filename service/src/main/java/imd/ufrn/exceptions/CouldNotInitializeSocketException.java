package imd.ufrn.exceptions;

public class CouldNotInitializeSocketException extends RuntimeException {
    public CouldNotInitializeSocketException(String errorMessage) {
        super(errorMessage);
    }

}
