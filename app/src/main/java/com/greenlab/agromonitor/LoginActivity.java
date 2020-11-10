package com.greenlab.agromonitor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.managers.SessionManager;
import com.greenlab.agromonitor.managers.UserManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements UserManager.OnFindUser {

     private static final String[] DUMMY_CREDENTIALS = new String[]{
            "arthur@email.com", "123456"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private Button mEmailSignInButton;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private TextView loadingText;
    private TextView developedBy;
    private Animation slideUp;
    private UserManager userManager;
    //private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isUserLoggedIn())
            callHomeActivity();

        layoutEmail = findViewById(R.id.text_email_input_layout);
        layoutPassword = findViewById(R.id.text_senha_input_layout);
        loadingText = findViewById(R.id.loading_text);
        developedBy = findViewById(R.id.developedBy);
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        developedBy.startAnimation(slideUp);
        developedBy.setVisibility(View.VISIBLE);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
       // populateAutoComplete();
        getSupportActionBar().hide();
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

       mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Reset errors.
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        String login = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(login)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            userManager = new UserManager(this);
            userManager.findUser(login, this);

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            layoutEmail.setVisibility(show ? View.GONE : View.VISIBLE);
            layoutPassword.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
            layoutEmail.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmailView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loadingText.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    loadingText.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loadingText.setVisibility(show ? View.VISIBLE : View.GONE);
            layoutEmail.setVisibility(show ? View.GONE : View.VISIBLE);
            layoutPassword.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void callHomeActivity(){
        Intent it = new Intent(this, HomeActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @Override
    public void onFindUserSuccess(User user) {
        showProgress(false);
        if (user == null){
            View focusView = mEmailView;
            mEmailView.setError(getString(R.string.error_user_not_found));
            focusView.requestFocus();
        }else{
            if ( mPasswordView.getText().toString().equals(user.getPassword())){
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.createUserSession(user);
                callHomeActivity();
                finish();
            }else{

                mEmailView.setError(getString(R.string.error_user_not_found));
            }
        }
    }

    @Override
    public void onFindUserFailed(String e) {
        showProgress(false);
        mEmailView.setError(getString(R.string.error_connection));
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, User> {

        private final String mEmail;
        private final String mPassword;
        //private final DbManager db;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected User doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return null;
            }

            User user = new User(this.mEmail, this.mPassword);
            return user.login(getApplicationContext());

        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected void onPostExecute(final User user) {
            mAuthTask = null;
            showProgress(false);

            if (user != null) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.createUserSession(user);
                callHomeActivity();
                finish();
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

