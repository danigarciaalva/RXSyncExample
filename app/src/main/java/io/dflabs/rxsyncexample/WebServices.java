package io.dflabs.rxsyncexample;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by dflabs on 1/16/17.
 * RxSyncExample
 */
class WebServices {
    private static ApiDefinition api;

    static ApiDefinition api() {
        if (api == null) {
            synchronized (ApiDefinition.class) {
                api = new Retrofit.Builder()
                        .baseUrl("http://emp-qa.dflabs.io/")
                        .addConverterFactory(GsonConverterFactory.create(GsonUtils.defaultGson()))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                        .build()
                        .create(ApiDefinition.class);
            }
        }
        return api;
    }
}
