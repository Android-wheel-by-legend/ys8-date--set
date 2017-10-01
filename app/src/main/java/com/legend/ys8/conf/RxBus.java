package com.legend.ys8.conf;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.observable.ObservableDistinct;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 创建一个Rxbus
 * Created by legend on 2017/9/30.
 */

public class RxBus {

    private static volatile RxBus rxBus;

    private Subject<Object> subject;



    public static RxBus getDefault(){
        if (rxBus==null){
            synchronized (RxBus.class){
                rxBus=new RxBus();

                rxBus.subject= PublishSubject.create().toSerialized();

            }
        }

        return rxBus;
    }


    private RxBus(){

    }

    public void post(Object o){
        subject.onNext(o);

    }

    public <T>Observable<T> tObservable(Class<T> tClass){
        return subject.ofType(tClass);
    }

}
