package com.demomvvm.school.mvvm.register;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.demomvvm.school.SchoolApplication;
import com.demomvvm.school.R;
import com.demomvvm.school.model.ResponseBase;
import com.demomvvm.school.util.Utils;
import com.demomvvm.school.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class RegisterViewModel extends Observable {


    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RegisterNavigator navigator;



    public RegisterViewModel(@NonNull Context context) {
        this.context = context;
        navigator = (RegisterNavigator) context;

    }

    public String checkvalidation(final String fname, final String lname, final String email, final String mobile, final String password, final String cnfpassword) {
        if (TextUtils.isEmpty(fname)) {
            return context.getString(R.string.str_enter_first_name);
        } else if (TextUtils.isEmpty(lname)) {
            return context.getString(R.string.str_enter_last_name);
        } else if (TextUtils.isEmpty(email)) {
            return context.getString(R.string.str_enter_email);
        } else if (!Utils.isValidEmail(email)) {
            return context.getString(R.string.str_valid_email_enter);
        } else if (TextUtils.isEmpty(mobile)) {
            return context.getString(R.string.str_enter_mobile_number);
        } else if (mobile.length() < 8 || mobile.length() > 13) {
            return context.getString(R.string.str_valid_mobile_number);
        } else if (TextUtils.isEmpty(password)) {
            return context.getString(R.string.str_enter_password);
        } else if (password.length() < 6) {

            return context.getString(R.string.str_enter_password_must_have_six_character);

        } else if (TextUtils.isEmpty(cnfpassword)) {

            return context.getString(R.string.str_confrim_password);

        } else if (cnfpassword.length() < 6) {

            return context.getString(R.string.str_enter_password_must_have_six_character);

        } else if (!password.equals(cnfpassword)) {

            return context.getString(R.string.str_pass_and_conf_pass_does_not_match);

        } else {
            return "";
        }

    }


    public void onClickLogin(View view) {
        navigator.loginClick();
    }

    public void onClickRegister(View view) {
        navigator.register();
    }


    public void doRegister(final String fname, final String lname, final String email, final String mobile, final String password, final String loginType) {


        SchoolApplication appController = SchoolApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.doRegister(fname, lname, email, password, mobile, loginType)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBase>() {
                    @Override
                    public void accept(ResponseBase userResponse) throws Exception {
                        navigator.registerResponce(userResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        navigator.handleError(throwable);
                    }
                });

        compositeDisposable.add(disposable);
    }


    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
}

