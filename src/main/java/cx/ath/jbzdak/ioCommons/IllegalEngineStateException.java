package cx.ath.jbzdak.ioCommons;

public class IllegalEngineStateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalEngineStateException() {	}

	public IllegalEngineStateException(String message) {
		super(message);
	}

	public IllegalEngineStateException(Throwable cause) {
		super(cause);
	}

	public IllegalEngineStateException(String message, Throwable cause) {
		super(message, cause);
	}

}
