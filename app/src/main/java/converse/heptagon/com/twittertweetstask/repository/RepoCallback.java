package converse.heptagon.com.twittertweetstask.repository;

public interface RepoCallback<T>
{
    void onSuccess(T object);
    void onFailure(Throwable error);
}
