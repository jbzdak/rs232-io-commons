package cx.ath.jbzdak.diesIrae.ioCommons;

public class IllegalPortStateException extends PortException {

	private static final long serialVersionUID = 1L;

	public IllegalPortStateException() {
		super();
	}

	public IllegalPortStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalPortStateException(String message) {
		super(message);
	}

	public IllegalPortStateException(Throwable cause) {
		super(cause);
	}
}
