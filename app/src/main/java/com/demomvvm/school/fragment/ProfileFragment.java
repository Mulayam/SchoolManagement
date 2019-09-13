package com.demomvvm.school.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demomvvm.school.SchoolApplication;
import com.demomvvm.school.R;
import com.demomvvm.school.databinding.FragmentProfileBinding;
import com.demomvvm.school.model.ResponseBase;
import com.demomvvm.school.mvvm.profile.ProfileNavigator;
import com.demomvvm.school.mvvm.profile.ProfileViewModel;
import com.demomvvm.school.util.Preferences;
import com.demomvvm.school.util.Utils;

/**
 * Created Kailash Patel
 */

public class ProfileFragment extends Fragment implements ProfileNavigator
{


    private static final long MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;

    private LinearLayout llMain;
    private ProgressDialog progress;
    private Activity activity;
    private View rootView;

    private ProfileNavigator profileNavigator;
    private ProfileViewModel viewModel;
    private FragmentProfileBinding binding;

    private String fName;
    private String lName;
    private String email;
    private String phone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        activity = SchoolApplication.getmInstance().getActivity();
        rootView = binding.getRoot();
        initComponents(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }


    public void initComponents(View rootView) {

        profileNavigator = this;
        viewModel = new ProfileViewModel(activity, profileNavigator);
        binding.setProfileViewModel(viewModel);
        llMain = binding.activityLlmain;

        setUpProfileData();


    }

    private void setUpProfileData() {

        String fName = Preferences.readString(activity, Preferences.FIRST_NAME, "");
        String lName = Preferences.readString(activity, Preferences.LAST_NAME, "");
        String email = Preferences.readString(activity, Preferences.USER_EMAIL_ID, "");
        String phone = Preferences.readString(activity, Preferences.USER_PHONE_NO, "");


        binding.fragmentProfileEtFirstName.setText("" + fName);
        binding.fragmentProfileEtLastName.setText("" + lName);
        binding.fragmentProfileEtEmail.setText("" + email);
        binding.fragmentProfileEtMobile.setText("" + phone);


    }


    @Override
    public void SubmitClick() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Utils.hideKeyboard(activity);
        email = binding.fragmentProfileEtEmail.getText().toString();
        phone = binding.fragmentProfileEtMobile.getText().toString();
        fName = binding.fragmentProfileEtFirstName.getText().toString();
        lName = binding.fragmentProfileEtLastName.getText().toString();


        final String checkValidation = viewModel.checkvalidation(fName, lName, email, phone);

        if (!checkValidation.isEmpty()) {
            Utils.snackbar(llMain, checkValidation, true, activity);
        } else {

            if (Utils.isOnline(activity, true)) {

                progress = Utils.showProgressDialog(activity);
                viewModel.doProfileUpfate(fName, lName, email, phone);

            } else {
                Toast.makeText(activity, "" + activity.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

            }


        }


    }


    @Override
    public void handleError(Throwable throwable) {
        Utils.snackbar(llMain, "" + throwable.getMessage(), true, activity);
        Utils.hideProgressDialog(activity, progress);
        Utils.hideKeyboard(activity);
    }


    @Override
    public void profileUpdateResponce(ResponseBase userResponse) {

        Utils.hideKeyboard(activity);
        Utils.hideProgressDialog(activity, progress);
        Utils.snackbar(llMain, "" + userResponse.getResponsedata().getMessage(), true, activity);

        if (userResponse.getResponsedata().getSuccess() == 1)
        {
            Preferences.writeString(activity, Preferences.FIRST_NAME, fName);
            Preferences.writeString(activity, Preferences.LAST_NAME, lName);
            Preferences.writeString(activity, Preferences.USER_EMAIL_ID, email);
            Preferences.writeString(activity, Preferences.USER_PHONE_NO, phone);
            Preferences.writeString(activity, Preferences.USER_NAME, fName + " " + lName);
        }


    }


}
