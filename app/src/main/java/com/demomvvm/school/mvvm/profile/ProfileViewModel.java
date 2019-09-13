package com.demomvvm.school.mvvm.profile;

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

public class ProfileViewModel extends Observable {


    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProfileNavigator navigator;

    public ProfileViewModel(@NonNull Context context,ProfileNavigator profileNavigator) {
        this.context = context;
        navigator = profileNavigator;

    }

    public String checkvalidation(final String fname, final String lname, final String email, final String mobile)
    {
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
        }else {
            return "";
        }

    }


    public void onClickSubmit(View view) {
        navigator.SubmitClick();
    }



    public void doProfileUpfate(final String fname, final String lname, final String email, final String mobile) {


        SchoolApplication appController = SchoolApplication.getmInstance();
        UsersService usersService = appController.getUserService();


        Disposable disposable = usersService.getUserProfileDetailsResponse(fname, lname, email, mobile)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBase>() {
                    @Override
                    public void accept(ResponseBase userResponse) throws Exception {
                        navigator.profileUpdateResponce(userResponse);

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

