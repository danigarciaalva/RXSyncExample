package io.dflabs.rxsyncexample;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dflabs on 1/12/17.
 * RxSyncExample
 */
public class ExpenseCategory extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    public String id;
    @SerializedName("date_creation")
    public Date dateCreation;
    @SerializedName("date_modification")
    public Date dateModification;
    @SerializedName("date_deletion")
    public Date dateDeletion;
    @SerializedName("deleted")
    public boolean deleted;
    @SerializedName("name")
    public String name;
    @SerializedName("color")
    public String color;
    @SerializedName("icon")
    public String icon;

    public ExpenseCategory() {

    }

    @Override
    public String toString() {
        return this.name;
    }
}
