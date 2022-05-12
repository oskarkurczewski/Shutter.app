package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

public class NoConfigFileFound extends CustomApplicationException {
    public NoConfigFileFound() {
        super();
    }

    public NoConfigFileFound(String message) {
        super(message);
    }
}
