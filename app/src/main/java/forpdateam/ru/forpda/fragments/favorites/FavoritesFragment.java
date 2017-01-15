package forpdateam.ru.forpda.fragments.favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import forpdateam.ru.forpda.R;
import forpdateam.ru.forpda.api.Api;
import forpdateam.ru.forpda.api.favorites.Favorites;
import forpdateam.ru.forpda.api.favorites.models.FavData;
import forpdateam.ru.forpda.api.favorites.models.FavItem;
import forpdateam.ru.forpda.fragments.TabFragment;
import forpdateam.ru.forpda.utils.AlertDialogMenu;
import forpdateam.ru.forpda.utils.IntentHandler;
import forpdateam.ru.forpda.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by radiationx on 22.09.16.
 */

public class FavoritesFragment extends TabFragment {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private FavoritesAdapter.OnItemClickListener onItemClickListener =
            favItem -> {
                Bundle args = new Bundle();
                args.putString(TabFragment.TITLE_ARG, favItem.getTopicTitle());
                IntentHandler.handle("http://4pda.ru/forum/index.php?showtopic=" + favItem.getTopicId() + "&view=getnewpost", args);
            };
    private AlertDialogMenu<FavoritesFragment, FavItem> favoriteDialogMenu;
    private FavoritesAdapter.OnLongItemClickListener onLongItemClickListener =
            favItem -> {
                if (favoriteDialogMenu == null) {
                    favoriteDialogMenu = new AlertDialogMenu<>();
                    favoriteDialogMenu.addItem("Скопировать ссылку", (context, data) -> Utils.copyToClipBoard("http://4pda.ru/forum/index.php?showtopic=".concat(Integer.toString(data.getTopicId()))));
                    favoriteDialogMenu.addItem("Вложения", (context, data) -> IntentHandler.handle("http://4pda.ru/forum/index.php?act=attach&code=showtopic&tid=" + data.getTopicId()));
                    favoriteDialogMenu.addItem("Открыть форум темы", (context, data) -> IntentHandler.handle("http://4pda.ru/forum/index.php?showforum=" + data.getForumId()));
                    favoriteDialogMenu.addItem("Изменить тип подписки", (context, data) -> {
                        new AlertDialog.Builder(context.getContext())
                                .setItems(Favorites.SUB_NAMES, (dialog1, which1) -> context.changeFav(0, Favorites.SUB_TYPES[which1], data.getFavId()))
                                .show();
                    });
                    favoriteDialogMenu.addItem(getPinText(favItem.isPin()), (context, data) -> context.changeFav(1, data.isPin() ? "unpin" : "pin", data.getFavId()));
                    favoriteDialogMenu.addItem("Удалить", (context, data) -> context.changeFav(2, null, data.getFavId()));
                }

                int index = favoriteDialogMenu.containsIndex(getPinText(!favItem.isPin()));
                if (index != -1)
                    favoriteDialogMenu.changeTitle(index, getPinText(favItem.isPin()));

                new AlertDialog.Builder(getContext())
                        .setItems(favoriteDialogMenu.getTitles(), (dialog, which) -> {
                            Log.d("kek", "ocnlicl " + favItem + " : " + favItem.getFavId());
                            favoriteDialogMenu.onClick(which, FavoritesFragment.this, favItem);
                        })
                        .show();
            };

    private Realm realm;
    private RealmResults<FavItem> results;
    private FavoritesAdapter adapter;
    private Subscriber<FavData> mainSubscriber = new Subscriber<>();
    private Subscriber<Boolean> helperSubscriber = new Subscriber<>();
    boolean markedRead = false;

    private CharSequence getPinText(boolean b) {
        return b ? "Открепить" : "Закрепить";
    }

    @Override
    public String getDefaultTitle() {
        return "Избранное";
    }

    @Override
    public boolean isAlone() {
        return true;
    }

    @Override
    public boolean isUseCache() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initBaseView(inflater, container);
        setWhiteBackground();
        baseInflateFragment(inflater, R.layout.fragment_qms_themes);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) findViewById(R.id.qms_list_themes);
        viewsReady();
        refreshLayout.setOnRefreshListener(this::loadData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        adapter = new FavoritesAdapter();
        adapter.setOnItemClickListener(onItemClickListener);
        adapter.setOnLongItemClickListener(onLongItemClickListener);
        recyclerView.setAdapter(adapter);

        bindView();
        return view;
    }

    @Override
    public void loadData() {
        if (refreshLayout != null)
            refreshLayout.setRefreshing(true);
        mainSubscriber.subscribe(Api.Favorites().get(), this::onLoadThemes, new FavData(), v -> loadData());
    }

    private void onLoadThemes(FavData data) {
        Log.d("kek", "loaded itms " + data.getFavItems().size() + " : " + results.size());
        if (refreshLayout != null)
            refreshLayout.setRefreshing(false);

        if (data.getFavItems().size() == 0)
            return;

        realm.executeTransactionAsync(r -> {
            r.delete(FavItem.class);
            r.copyToRealmOrUpdate(data.getFavItems());
        }, this::bindView);
    }

    private void bindView() {
        results = realm.where(FavItem.class).findAll();
        if (results.size() != 0) {
            adapter.addAll(results);
        }
        Api.get().notifyObservers();
    }

    public void changeFav(int act, String type, int id) {
        helperSubscriber.subscribe(Api.Favorites().changeFav(act, type, id), this::onChangeFav, false);
    }

    public void markRead(int topicId) {
        realm.executeTransactionAsync(realm1 -> {
            FavItem favItem = realm1.where(FavItem.class).equalTo("topicId", topicId).findFirst();
            if (favItem != null) {
                favItem.setNewMessages(false);
            }
        });
        markedRead = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (markedRead) {
            bindView();
            markedRead = false;
        }
    }

    private void onChangeFav(boolean v) {
        if (!v)
            Toast.makeText(getContext(), "При выполнении операции произошла ошибка", Toast.LENGTH_SHORT).show();
        loadData();
    }
}
