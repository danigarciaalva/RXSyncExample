package io.dflabs.rxsyncexample;

import android.content.Context;

import java.lang.ref.WeakReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dflabs on 1/15/17.
 * RxSyncExample
 */
class RealmProvider {
    private static RealmConfiguration realmConfiguration;
    private static WeakReference<Realm> realm;

    public static Realm open() {
        if (realmConfiguration == null) {
            throw new RuntimeException("Call first RealmProvider.provide(Context context)");
        }
        if (realm != null && realm.get() != null && !realm.get().isClosed()) {
            throw new IllegalStateException("Close previous realm instance provided first.");
        }
        return (realm = new WeakReference<>(Realm.getInstance(realmConfiguration))).get();
    }

    public static void provide(Context context) {
        Realm.init(context);
        synchronized (RealmConfiguration.class) {
            realmConfiguration = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
        }
    }

    public static void close() {
        if (realm.get() != null && !realm.get().isClosed()) realm.get().close();
    }
}
