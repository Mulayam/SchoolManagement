package com.demomvvm.school.mvvm.storelist;

import android.content.Context;
import androidx.annotation.NonNull;

import com.demomvvm.school.SchoolApplication;
import com.demomvvm.school.model.storeListmodel.StoreResponseData;
import com.demomvvm.school.webservice.UsersService;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Kailash Patel
 */

public class StoreViewModel extends Observable {

    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StoreListNavigator storeListNavigator;


    public StoreViewModel(@NonNull Context context,StoreListNavigator storeListNavigator) {
        this.context = context;
        this.storeListNavigator = storeListNavigator;
    }


    public void getStoreList(final String locationId, final String offset) {



        try {
            SchoolApplication appController = SchoolApplication.getmInstance();
            UsersService usersService = appController.getUserService();


            Disposable disposable = usersService.doGetLocation(locationId, offset)
                    .subscribeOn(appController.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<StoreResponseData>() {
                        @Override
                        public void accept(StoreResponseData userResponse) throws Exception {
                            storeListNavigator.storeResponce(userResponse);

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            storeListNavigator.handleError(throwable);
                        }
                    });

            compositeDisposable.add(disposable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    public void getStoreListLatlng(final String lat,final String lng, final String offset) {


        try {
            SchoolApplication appController = SchoolApplication.getmInstance();
            UsersService usersService = appController.getUserService();


            Disposable disposable = usersService.doGetLocationWithLatLng(lat,lng, offset)
                    .subscribeOn(appController.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<StoreResponseData>() {
                        @Override
                        public void accept(StoreResponseData userResponse) throws Exception {
                            storeListNavigator.storeResponce(userResponse);

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            storeListNavigator.handleError(throwable);
                        }
                    });

            compositeDisposable.add(disposable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


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

