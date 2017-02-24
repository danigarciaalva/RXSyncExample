package io.dflabs.rxsyncexample;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by dflabs on 1/12/17.
 * RxSyncExample
 */
class BaseApiResponse<T extends RealmObject> {

    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("next")
    @Expose
    public String next;
    @SerializedName("previous")
    @Expose
    public String previous;
    @SerializedName("results")
    RealmList<ExpenseCategory> results;

}
