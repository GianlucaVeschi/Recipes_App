package www.gianlucaveschi.mijirecipesapp.login.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class LoginResult<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private LoginResult() {
    }

    @Override
    public String toString() {
        if (this instanceof LoginResult.Success) {
            LoginResult.Success success = (LoginResult.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof LoginResult.Error) {
            LoginResult.Error error = (LoginResult.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends LoginResult {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends LoginResult {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
