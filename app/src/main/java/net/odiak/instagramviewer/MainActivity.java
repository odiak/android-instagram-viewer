package net.odiak.instagramviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private GridView mGridView;
    private ImagesAdapter mImagesAdapter;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mGridView = (GridView) findViewById(R.id.gridView);
        mImagesAdapter = new ImagesAdapter(this);
        mGridView.setAdapter(mImagesAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InstagramImage image = mImagesAdapter.getItem(position);
                Uri uri = Uri.parse(image.getLink());
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        setTag("cat");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.menu_search);

        final SearchView searchView = (SearchView) mToolbar.getMenu().findItem(R.id.search)
                .getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                System.out.println("text submit: " + query);
                setTag(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("text change: " + newText);
                return true;
            }
        });

        return true;
    }

    private void setTag(String tag) {
        mImagesAdapter.clear();
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            setTitle("#" + tag);
        }
        new LoadImagesTask().execute(tag);
    }

    private class LoadImagesTask extends AsyncTask<String, Integer, ArrayList<InstagramImage>> {
        @Override
        protected ArrayList<InstagramImage> doInBackground(String... tags) {
            if (tags.length > 0) {
                try {
                    return loadImageUrls(tags[0]);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return null;
        }

        private ArrayList<InstagramImage> loadImageUrls(String tag) throws IOException, JSONException {
            String url = String.format("%s/tags/%s/media/recent?client_id=%s&count=%d",
                    AppConstants.API_ROOT,
                    tag,
                    getResources().getString(R.string.client_id),
                    30);

            Request req = new Request.Builder()
                    .url(url)
                    .build();

            Response res = client.newCall(req).execute();

            ArrayList<InstagramImage> images = new ArrayList<>();

            JSONObject json = new JSONObject(res.body().string());
            JSONArray data = json.getJSONArray("data");
            for (int i = 0, len = data.length(); i < len; i++) {
                JSONObject medium = data.getJSONObject(i);
                images.add(InstagramImage.fromJson(medium));
            }

            return images;
        }

        @Override
        protected void onPostExecute(ArrayList<InstagramImage> images) {
            if (images == null) return;

            mImagesAdapter.addAll(images);
        }
    }
}
