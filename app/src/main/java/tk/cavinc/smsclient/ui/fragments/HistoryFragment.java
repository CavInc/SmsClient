package tk.cavinc.smsclient.ui.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.database.DBConnect;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.ui.adapters.HistoryAdapter;
import tk.cavinc.smsclient.utils.App;

import static android.content.ContentValues.TAG;

/**
 * Created by cav on 25.07.20.
 */

public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,Observer {

    private DataManager mDataManager;

    private HistoryAdapter mAdapter;
    private ListView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        mAdapter = new HistoryAdapter(getActivity(),null,0);
        setHasOptionsMenu(true);
        //getActivity().setTitle("История сообщений");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        mListView = rootView.findViewById(R.id.history_lv);
        mListView.setAdapter(mAdapter);
        mDataManager.getDB().open();
        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_delete:
                mDataManager.getDB().deleteHistory();
                getLoaderManager().getLoader(0).forceLoad();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getChangeHistoryManager().addObserver(this);
        setTitleCount();
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getChangeHistoryManager().deleteObserver(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
    }

    private void setTitleCount(){
        //long count = mDataManager.getDB().getHistoryCount();
        //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("отправлено сообщений : "+count);
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
         return new MyCursorLoader(getActivity(), mDataManager.getDB());
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //этот метод вызывается после получения данных из БД. Адаптеру
        //посылаются новые данные в виде Cursor и сообщение о том, что
        //данные обновились и нужно заново отобразить список.
        mAdapter.swapCursor(data);
        Log.d(TAG,"Получили");
        int count = data.getCount();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("отправлено сообщений : "+count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        //если в полученном результате sql-запроса нет никаких строк,
        //то говорим адаптеру, что список нужно очистить
        mAdapter.swapCursor(null);
    }

    @Override
    public void update(Observable observable, Object o) {
        getLoaderManager().getLoader(0).forceLoad();
        setTitleCount();
    }

    static class MyCursorLoader extends CursorLoader {
        private DBConnect mDBConnect;

        public MyCursorLoader(@NonNull Context context, DBConnect db) {
            super(context);
            mDBConnect = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = mDBConnect.getHistory();
            return cursor;
        }
    }
}
