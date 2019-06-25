package converse.heptagon.com.twittertweetstask.Tweets;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;;
import java.util.List;

import converse.heptagon.com.twittertweetstask.BaseViewModel;
import converse.heptagon.com.twittertweetstask.model.Tweet;
import converse.heptagon.com.twittertweetstask.repository.RepoCallback;

public class ViewModelMainActivity extends BaseViewModel {

    private MutableLiveData<Bundle> bundleEvent;
    private MutableLiveData<List<Tweet>> loadList;
    public final static String EVENT = "EVENT";

    public ViewModelMainActivity(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Bundle> getBundleEvent() {
        if (bundleEvent == null) {
            bundleEvent = new MutableLiveData<Bundle>();
        }
        return bundleEvent;
    }

    public MutableLiveData<List<Tweet>> getLoadList() {
        if(loadList == null)
            loadList = new MutableLiveData<List<Tweet>>();
        return loadList;
    }

    public void getTweetList(String query)
   {
       showLoader();
        getRepository().getTweetList(query, new RepoCallback<List<Tweet>>() {
            @Override
            public void onSuccess(List<Tweet> object) {
                hideLoader();
                loadList(object);
            }

            @Override
            public void onFailure(Throwable error) {
                errorDialog();
            }
        });
   }

   private void loadList(List<Tweet> object){
       Log.e("loadList" , object.toString());
       getLoadList().setValue(object);
   }

    private void showLoader(){
        Bundle bundle = new Bundle();
        bundle.putString(EVENT ,"SHOW_LOADER");
        getBundleEvent().setValue(bundle);
   }

    private void hideLoader(){
       Bundle bundle = new Bundle();
       bundle.putString(EVENT ,"HIDE_LOADER");
       getBundleEvent().setValue(bundle);
   }

   private void emptydata(){
       Bundle bundle = new Bundle();
       bundle.putString(EVENT ,"EMPTY_DATA");
       getBundleEvent().setValue(bundle);
   }

   public void errorDialog()
   {
       Bundle bundle = new Bundle();
       bundle.putString(EVENT ,"ERROR_LOADER");
       getBundleEvent().setValue(bundle);
   }

}
