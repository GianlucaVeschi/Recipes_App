package www.gianlucaveschi.mijirecipesapp.login.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.gianlucaveschi.load_json_images_picasso.R;

import www.gianlucaveschi.mijirecipesapp.login.data.LoginRepository;
import www.gianlucaveschi.mijirecipesapp.login.data.LoginResult;
import www.gianlucaveschi.mijirecipesapp.models.LoggedInUser;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<www.gianlucaveschi.mijirecipesapp.login.ui.LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<www.gianlucaveschi.mijirecipesapp.login.ui.LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        LoginResult<LoggedInUser> loginResult = loginRepository.login(username, password);

        if (loginResult instanceof LoginResult.Success) {
            LoggedInUser data = ((LoginResult.Success<LoggedInUser>) loginResult).getData();
            LoggedInUserView loggedInUserView = new LoggedInUserView(data.getDisplayName());
            this.loginResult.setValue(new www.gianlucaveschi.mijirecipesapp.login.ui.LoginResult(loggedInUserView));
        } else {
            this.loginResult.setValue(new www.gianlucaveschi.mijirecipesapp.login.ui.LoginResult(R.string.login_failed));
        }
    }

    //Validates username and password
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public boolean isLoginDataValid(String username, String password){
        return isUserNameValid(username) && isPasswordValid(password);
    }
}
