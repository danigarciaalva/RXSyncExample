package io.dflabs.rxsyncexample;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dflabs on 1/16/17.
 * RxSyncExample
 */
interface ApiDefinition {

    @GET
    Observable<BaseApiResponse> get(
            @Url String url,
            @QueryMap HashMap<String, String> options
    );
}
