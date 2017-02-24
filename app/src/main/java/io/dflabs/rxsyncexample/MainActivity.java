package io.dflabs.rxsyncexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    ArrayAdapter<ExpenseCategory> mAdapter;
    private Realm mRealm;
    private CompositeSubscription mComposite = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.act_main_list);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        RealmProvider.provide(this);


//        PlatformsRequestData requestData = new PlatformsRequestData(PlatformsRequestData.EXPENSE_CATEGORIES,
//                new HashMap<>(), PlatformsRequestData.OrderBy.DATE_CREATION);
//        Observable<RealmResults<ExpenseCategory>> observable = requestData.listOf(ExpenseCategory.class);
//        observable.subscribe(this::updateAdapter);

        mRealm = Realm.getDefaultInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.defaultGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(OkHttpUtils.authorizedClient())
                .baseUrl("http://emp-qa.dflabs.io/")
                .build();

        ExpensesDefinition expensesDefinition = retrofit.create(ExpensesDefinition.class);

        Observable<RealmList<ExpenseCategory>> realmResultsObservable = mRealm.where(ExpenseCategory.class)
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .map(expenseCategories -> {
                    RealmList<ExpenseCategory> list = new RealmList<>();
                    for (ExpenseCategory category : expenseCategories) {
                        list.add(category);
                    }
                    return list;
                })
                .doOnUnsubscribe(mRealm::close)
                .subscribeOn(AndroidSchedulers.mainThread());

        Subscription subscriptionRealm = realmResultsObservable.subscribe(results -> {
            //View with fetched db results
            updateAdapter(results);
            Observable<BaseApiResponse<ExpenseCategory>> expenseCategoriesResponseObservable = expensesDefinition.list();
            Subscription subscription = expenseCategoriesResponseObservable
                    .subscribe(result -> {
                        if (!result.results.isEmpty()) {
                            mRealm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(result.results));
                            updateAdapter(result.results);
                        }
                    }, Throwable::printStackTrace);
            mComposite.add(subscription);
        }, Throwable::printStackTrace);
        mComposite.add(subscriptionRealm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComposite.unsubscribe();
    }

    private void updateAdapter(RealmList<ExpenseCategory> results) {
        for (ExpenseCategory expenseCategory : results) {
            mAdapter.insert(expenseCategory, 0);
        }
        mAdapter.notifyDataSetChanged();
    }
}
