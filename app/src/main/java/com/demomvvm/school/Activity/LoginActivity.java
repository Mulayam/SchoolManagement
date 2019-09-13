package com.demomvvm.school.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.demomvvm.school.SchoolApplication;
import com.demomvvm.school.R;
import com.demomvvm.school.databinding.ActivityLoginBinding;
import com.demomvvm.school.model.ResponseBase;
import com.demomvvm.school.model.loginModel.LoginResponse;
import com.demomvvm.school.mvvm.login.LoginNavigator;
import com.demomvvm.school.mvvm.login.LoginViewModel;
import com.demomvvm.school.util.Preferences;
import com.demomvvm.school.util.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.demomvvm.school.util.Constans.RC_SIGN_IN;


public class LoginActivity extends BaseActivity implements LoginNavigator, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout llContainer;
    private String password;
    private String email;
    private ProgressDialog progress;
    private CallbackManager callbackFbManager;

    private String firstname = "";
    private String lastname = "";
    private String fbID = "";
    private String userName = "";
    private String socialEmail = "";
    private String login_type = "0";
    private boolean gpLoginbtnclick = false;

    private GoogleApiClient mGoogleApiClient;
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        initComponents();
        initFacebook();
        initGoogleSign();


    }


    @Override
    public void initComponents() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        binding.setLoginViewModel(loginViewModel);
        llContainer = binding.activityLlmain;

        Log.d("initComponents", "TOKEN==" + FirebaseInstanceId.getInstance().getToken());


    }


    @Override
    public void onClick(View v) {


    }


    @Override
    public void handleError(Throwable throwable) {
        Utils.hideProgressDialog(this, progress);
        Utils.snackbar(llContainer, "" + throwable.getMessage(), true, LoginActivity.this);
    }

    @Override
    public void login() {


        Utils.hideKeyboard(LoginActivity.this);
        email = binding.activityLoginEtUserName.getText().toString();
        password = binding.activityLoginEtPassword.getText().toString();
        final String checkValidation = loginViewModel.isEmailAndPasswordValid(email, password);

        if (!checkValidation.isEmpty()) {
            Utils.snackbar(llContainer, checkValidation, true, LoginActivity.this);
        } else {
            progress = Utils.showProgressDialog(LoginActivity.this);
            login_type = "0";
            loginViewModel.checkLogin(email, password, login_type);
        }
    }

    @Override
    public void skip() {




    }

    @Override
    public void register() {

        Intent mMenuIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(mMenuIntent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);

    }

    @Override
    public void forgotPassword() {

        Intent mMenuIntent = new Intent(LoginActivity.this, ForgotPswActivity.class);
        startActivity(mMenuIntent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);

    }

    @Override
    public void fbLogin() {

        if (Utils.isOnline(LoginActivity.this, true)) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getApplicationContext());

        }

    }

    @Override
    public void googleLogin() {
        if (Utils.isOnline(LoginActivity.this, true)) {
            if (gpLoginbtnclick) {
                if (!mGoogleApiClient.isConnecting()) {
                    resolveSignInError();
                }
            }
        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getApplicationContext());

        }


    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void loginResponce(LoginResponse responseBase) {


        if (responseBase != null) {
            if (responseBase.getResponsedata().getSuccess() == 1) {
                Preferences.writeString(this, Preferences.IS_SKIPED, "");
                Preferences.writeString(this, Preferences.USER_ID, "");

                String token = FirebaseInstanceId.getInstance().getToken();
                Preferences.writeString(LoginActivity.this, Preferences.USER_ID, responseBase.getResponsedata().getUserData().getId());
                Preferences.writeString(LoginActivity.this, Preferences.USER_EMAIL_ID, responseBase.getResponsedata().getUserData().getEmail());
                Preferences.writeString(LoginActivity.this, Preferences.USER_NAME, responseBase.getResponsedata().getUserData().getFname() + " " + responseBase.getResponsedata().getUserData().getLname());
                Preferences.writeString(LoginActivity.this, Preferences.FIRST_NAME, responseBase.getResponsedata().getUserData().getFname());
                Preferences.writeString(LoginActivity.this, Preferences.LAST_NAME, responseBase.getResponsedata().getUserData().getLname());
                Preferences.writeString(LoginActivity.this, Preferences.USER_PHONE_NO, responseBase.getResponsedata().getUserData().getPhone());
                Preferences.writeString(LoginActivity.this, Preferences.DEVICE_ID, Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                Preferences.writeString(LoginActivity.this, Preferences.DEVICE_TOKEN, token);
                Snackbar.make(llContainer, responseBase.getResponsedata().getMessage(), Snackbar.LENGTH_LONG).show();


                Preferences.writeString(LoginActivity.this, Preferences.LOGIN_TYPE, String.valueOf(login_type));
                loginViewModel.addDeviceLocation(responseBase.getResponsedata().getUserData().getId(), Preferences.readString(LoginActivity.this, Preferences.DEVICE_ID, ""),
                        token, "Android");


            } else {

                Utils.hideProgressDialog(LoginActivity.this, progress);
                Snackbar.make(llContainer, responseBase.getResponsedata().getMessage(), Snackbar.LENGTH_LONG).show();
            }


        }


    }

    @Override
    public void addDeviceResponce(ResponseBase responseBase) {

        Utils.hideProgressDialog(this, progress);

        if (responseBase != null && responseBase.getResponsedata() != null && responseBase.getResponsedata().getSuccess() == 1)
        {
//
//            SchoolApplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_islogin), true);
//            Intent intent = new Intent(LoginActivity.this, SelectLocationActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openHomeActivity();
                } else {
                    // permission denied
                }
            }
        }
    }

    private void openHomeActivity() {

        SchoolApplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_islogin), true);
        // Intent intent = new Intent(getApplicationContext(), MenuBarActivity.class);
        // startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
        finish();
    }

    private void initFacebook() {

        callbackFbManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackFbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final Profile profile = Profile.getCurrentProfile();


                final GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (object.has("email") && object.getString("email") != null) {
                                socialEmail = object.getString("email");
                            }
                            if (profile != null) {
                                firstname = profile.getFirstName();
                                lastname = profile.getLastName();
                                fbID = profile.getId();
                                email = socialEmail;
                                userName = firstname + " " + lastname;
                                String phone = "";

                                progress = Utils.showProgressDialog(LoginActivity.this);
                                login_type = "1";
                                loginViewModel.fbLogin(userName, lastname, email, phone, login_type, fbID);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                final Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

                Log.d("onError", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("onError", "onError" + error.getMessage());

            }
        });
    }


    private void initGoogleSign() {

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();


        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            gpLoginbtnclick = false;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (signInResult != null) {
                    handleSignInResult(signInResult);
                }
            }
        } else {
            callbackFbManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    public void handleSignInResult(GoogleSignInResult signInResult) {
        if (signInResult != null) {

            if (signInResult.isSuccess()) {
                GoogleSignInAccount acct = signInResult.getSignInAccount();
                if (acct != null) {


                    String idToken = acct.getId();
                    String name = acct.getDisplayName();
                    String nameSplit[] = name.split(" ");
                    String firstName = nameSplit[0];
                    String lastName = nameSplit[1];
                    String email = acct.getEmail();

                    progress = Utils.showProgressDialog(LoginActivity.this);
                    login_type = "stra2";
                    loginViewModel.googleLogin(firstName, lastName, email, "", login_type, idToken);
                }
            }

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
    }


}
