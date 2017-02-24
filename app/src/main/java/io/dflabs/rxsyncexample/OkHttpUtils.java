package io.dflabs.rxsyncexample;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dflabs on 1/13/17.
 * RxSyncExample
 */
class OkHttpUtils {
    static OkHttpClient authorizedClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                final String authorization = "Token 019bf871df578d5a004cdda8df8733dbdd4e6909";
                Headers.Builder headersBuilder = new Headers.Builder();
                headersBuilder.add("Content-Type", "application/json");
                headersBuilder.add("Accept", "application/json");
                headersBuilder.add("Authorization", authorization);

                builder.headers(headersBuilder.build());
                return chain.proceed(builder.build());
            }
        }).build();
    }
}
