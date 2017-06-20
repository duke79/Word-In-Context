package com.baliyaan.android.wordincontext;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Pulkit Singh on 6/18/2017.
 */

/*Usage:
Observable<String> observable = new Observable<String>() {
            @Override
            protected void subscribeActual(Observer observer) {
                observer.onNext("someText");
            }
        }
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new ObserverAdapter(){

            @Override
            public void onNext(@NonNull Object o) {
                super.onNext(o);
                System.out.println(Thread.currentThread().getName());
            }
        });
 */

public class ObserverAdapter implements Observer {

    @Override
    public void onSubscribe(@NonNull Disposable d){

    }

    @Override
    public void onNext(@NonNull Object o){

    }

    @Override
    public void onError(@NonNull Throwable e){

    }

    @Override
    public void onComplete(){

    }
}
