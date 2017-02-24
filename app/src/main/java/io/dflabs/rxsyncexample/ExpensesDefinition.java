package io.dflabs.rxsyncexample;

import java.util.HashMap;

import io.realm.RealmObject;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by dflabs on 1/12/17.
 * RxSyncExample
 */
interface ExpensesDefinition {

    @GET("/expenses/api/v1/expense-categories/")
    Observable<BaseApiResponse<ExpenseCategory>> list();

}
