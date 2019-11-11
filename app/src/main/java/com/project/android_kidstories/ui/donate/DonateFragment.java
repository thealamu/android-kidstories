package com.project.android_kidstories.ui.donate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;
import com.project.android_kidstories.R;
import com.project.android_kidstories.ui.base.BaseFragment;

import java.util.UUID;


public class DonateFragment extends BaseFragment {
    private static final String publicKey = "FLWPUBK_TEST-22b228a9a67e55b0ae4a5208760d776f-X"; //Get your public key from your account
    private static final String encryptionKey = "FLWSECK_TEST81b6971aacc5"; //Get your encryption key from your account

    private static final String country = "NG";
    private static final String currency = "NGN";
    private static final String narration = "Donation";


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_donate, container, false);

        // Update Activity's toolbar title
        try {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Donate");
        } catch (NullPointerException npe) {
            Log.d("GLOBAL_SCOPE", "Can't set toolbar title");
        }

        Button button = root.findViewById(R.id.button_donate);
        EditText editText = root.findViewById(R.id.editText_amount);

        String email = "Igboanyika19.com"; // Bsckend code to get email name goes here
        String fName = "Nnaemeka"; // Bsckend code to get first name goes here
        String lName = "Igboanyika"; // Bsckend code to get last name goes here

        String txRef = email + " " + UUID.randomUUID().toString();

        button.setOnClickListener(v -> {
            String amount = editText.getText().toString().trim();

            if (TextUtils.isEmpty(amount)) {
                showToast("Donation should not be empty");
                return;
            }
            if (Double.valueOf(amount) < 100.00) {
                showToast("The minimum donation is " + currency + "100.00");
                return;
            }

            new RavePayManager(DonateFragment.this.getActivity())
                    .setAmount(Double.valueOf(amount))
                    .setCountry(country)
                    .setCurrency(currency)
                    .setEmail(email)
                    .setfName(fName)
                    .setlName(lName)
                    .setNarration(narration)
                    .setPublicKey(publicKey)
                    .setEncryptionKey(encryptionKey)
                    .setTxRef(txRef)
                    .acceptAccountPayments(true)
                    .acceptCardPayments(true)
                    .acceptMpesaPayments(false)
                    .onStagingEnv(false)
                    .withTheme(R.style.DefaultTheme)
                    .allowSaveCardFeature(true)
                    .initialize();
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                showToast("SUCCESS");
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                showToast("ERROR: " + message);
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                showToast("CANCELLED: " + message);
            }
        }
    }
}