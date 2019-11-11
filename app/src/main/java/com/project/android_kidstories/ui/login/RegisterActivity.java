package com.project.android_kidstories.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.*;
import com.google.android.material.snackbar.Snackbar;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.Repository;
import com.project.android_kidstories.data.model.User;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.data.source.remote.response_models.BaseResponse;
import com.project.android_kidstories.data.source.remote.response_models.loginRegister.RegistrationDataResponse;
import com.project.android_kidstories.ui.MainActivity;
import com.project.android_kidstories.ui.base.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.security.MessageDigest;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    public static final int LOGIN_TEXT_REQUEST_CODE = 11;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String AUTH_TYPE = "rerequest";

    EditText emailET;
    EditText phone;
    EditText firstName, lastName;
    EditText password, confirmPassword;
    Button regGoogle, signUp;
    TextView loginText;
    ProgressBar progressBar;
    //ProgressDialog regProgress;

    Repository repository;
    SharedPreferences sharedPreferences;
    SharePref sharePref;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_TEXT_REQUEST_CODE) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repository = Repository.getInstance(getApplication());

        printHashKey(this);  /*Debug: OnF8yB2LEowq5sp9VXjyI6p3s3Q= */ /*Release: KbIHpXw9Aq9VPKwfTvhk8qsfx+Q= */
        checkLoginStatus();

        repository = Repository.getInstance(getApplication());
        phone = findViewById(R.id.reg_contact);
        password = findViewById(R.id.reg_password);
        firstName = findViewById(R.id.reg_first_name);
        progressBar = findViewById(R.id.reg_progress_bar);

        lastName = findViewById(R.id.reg_last_name);

        emailET = findViewById(R.id.reg_email);
        confirmPassword = findViewById(R.id.reg_confirm_password);

        // regProgress = new ProgressDialog(RegisterActivity.this);

//        regFacebook = findViewById(R.id.reg_facebook);
//        regGoogle = findViewById(R.id.reg_google);
        signUp = findViewById(R.id.sign_up_button);
        loginText = findViewById(R.id.create_act);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        sharedPreferences = getSharedPreferences("API DETAILS", Context.MODE_PRIVATE);
        sharePref = getSharePref();


        // if user is already registered
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, LOGIN_TEXT_REQUEST_CODE);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstName = this.firstName.getText().toString();
        String lastName = this.lastName.getText().toString();
        String email = emailET.getText().toString();
        String phone = this.phone.getText().toString();
        String password = this.password.getText().toString();
        String confirmPassword = this.confirmPassword.getText().toString();
        User newUser;


        if (firstName.isEmpty()) {
            this.firstName.setError("Please enter your first name");
        } else if (lastName.isEmpty()) {
            this.lastName.setError("Please enter your last name");
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Please enter a valid email");
        } else if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            this.phone.setError("Please enter a valid phone number");
        } else if (password.isEmpty() || password.length() < 8) {
            this.password.setError("Password should be at least 8 characters");
        } else if (confirmPassword.isEmpty() || !confirmPassword.contentEquals(password)) {
            this.confirmPassword.setError("Passwords do not match");

        } else {
            // regProgress.setTitle("Creating new user");
            // regProgress.setMessage("Please wait while we create your account");
            // regProgress.setCanceledOnTouchOutside(false);
            // regProgress.show();
            progressBar.setVisibility(View.VISIBLE);
            newUser = new User(firstName, lastName, email);
            newUser.setPhoneNumber(phone);
            newUser.setPassword(confirmPassword);
            repository.getStoryApi().registerUser(newUser).enqueue(new Callback<BaseResponse<RegistrationDataResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<RegistrationDataResponse>> call, Response<BaseResponse<RegistrationDataResponse>> response) {
                    if (response.isSuccessful()) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        RegistrationDataResponse rdr = response.body().getData();
                        getSharePref().saveLoginDetails(
                                rdr.getToken(),
                                rdr.getFirstName() + " " + rdr.getLastName(),
                                rdr.getEmail());

                        editor.putString("Token", rdr.getToken());
                        editor.putString("Username", firstName + " " + lastName);
                        editor.apply();
                        sharePref.setIsUserLoggedIn(true);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();
                        Toast.makeText(getApplicationContext(), "User Successfully Created", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        //  regProgress.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is already registered", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<RegistrationDataResponse>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Network Failure", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.registration_parent_layout),
                            "Network Failure", Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    //  regProgress.hide();
                }
            });
        }
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                firstName.setText("");
                lastName.setText("");

                emailET.setText("");
                Toast.makeText(RegisterActivity.this, "User Logged Out", Toast.LENGTH_LONG).show();

            } else {
                //loaduserprofile(currentAccessToken);
            }

        }
    };


    private void loaduserprofile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String First_Name = object.getString("First_Name");
                    String Last_Name = object.getString("Last_Name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    emailET.setText(email);
                    firstName.setText(First_Name + " " + Last_Name);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "First_Name,Last_Name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
            // user already signed in
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }



     /*  email = findViewById(R.id.reg_email);
        phone = findViewById(R.id.reg_contact);
        fullName = findViewById(R.id.reg_full_name);
        password = findViewById(R.id.reg_password);
        btn = findViewById(R.id.sign_up_button);
        progressBar =  findViewById(R.id.reg_progress_bar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });

        googleSignInButton = findViewById(R.id.reg_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        String idToken = account.getIdToken();
                    /*
                      send this id token to server using HTTPS
                     */

//                    } catch (ApiException e) {
//                        // The ApiException status code indicates the detailed failure reason.
//                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//                    }
//                    break;
//            }
//
//    }

   /* private void signInUser() {
        String email_string = emailET.getText().toString();
        String phone_string = phone.getText().toString();

        String firstname_string = firstName.getText().toString();
        String Lastname_string = Lastname.getText().toString();

        String password_string = password.getText().toString();

        //validating text fields

        if (TextUtils.isEmpty(email_string) || (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())) {
            emailET.setError("Please enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(phone_string) || (!Patterns.PHONE.matcher(phone_string).matches())) {
            phone.setError("Please enter a valid phone number");
            return;
        }


        if (TextUtils.isEmpty(firstname_string) || TextUtils.isEmpty(password_string)) {

            firstName.setError("Please enter a valid phone number");
            password.setError("Enter a password");
            return;
        }


        User user = new User(firstname_string, Lastname_string, email_string);
        user.setPassword(password_string);
        user.setPhoneNumber(phone_string);
        Repository repository = new Repository(this.getApplication());
        Call<BaseResponse<DataResponse>> call = repository.getStoryApi().registerUser(user);


        call.enqueue(new Callback<BaseResponse<DataResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DataResponse>> call, Response<BaseResponse<DataResponse>> response) {


                if (response.code() == 201) {
                    String s = response.message();
                    Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();


                } else {

                    try {
                        String s = response.errorBody().string();
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            //This is weird, but the Method below actually registers the user.

            @Override
            public void onFailure(Call<BaseResponse<DataResponse>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }  */

    // Getting app hash key for facebook login registration
    private static void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey: " + hashKey + "=");
            }
        } catch (Exception e) {
            Log.e(TAG, "printHashKey: Error: " + e.getMessage());
        }
    }
}
