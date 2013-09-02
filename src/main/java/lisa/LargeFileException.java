package lisa;

/**
 * Created with IntelliJ IDEA.
 * User: masyes
 * Date: 03.08.13
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public class LargeFileException extends RuntimeException {
		public LargeFileException() {
			super();
		}

		public LargeFileException(String s) {
			super(s);
		}

		public LargeFileException(String message, Throwable cause) {
			super(message, cause);
		}

		public LargeFileException(Throwable cause) {
			super(cause);
		}

}
