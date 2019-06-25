package converse.heptagon.com.twittertweetstask;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import converse.heptagon.com.twittertweetstask.repository.TwitterRepository;
import converse.heptagon.com.twittertweetstask.repository.TwittertRepo;

public abstract class BaseViewModel extends AndroidViewModel {

    private TwittertRepo repository;

    public BaseViewModel(@NonNull Application application) {
        super(application);

        repository = ((BaseApplication) application).getRepository();

    }

    public TwittertRepo getRepository(){
        return repository;
    }

}
