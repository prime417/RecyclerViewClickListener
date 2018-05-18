package primer.com.recyclerviewmaster;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Movie;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

private static final String TAG = MainActivity.class.getSimpleName();
    // url to fetch shopping items
    private static final String URL = "https://api.androidhive.info/json/movies_2017.json";

    private RecyclerView recyclerView;
    private List<Pojo1> itemsList;
    private CustomAdapter mAdapter;
    private   Pojo1 pojo1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        itemsList = new ArrayList<>();
        mAdapter = new CustomAdapter(getApplicationContext(), itemsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
   //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(16), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchStoreItems();
    }

    private void fetchStoreItems() {



        final ImageLoader imageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
        final  CacheRequest cacheRequest = new CacheRequest(Request.Method.GET, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                try {

                    final String stringResponse = new String(response.data, HttpHeaderParser.parseCharset( response.headers));

                    JSONArray jsonArray = new JSONArray(stringResponse);

                    for (int i=0 ;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        pojo1 = new Pojo1();
                        Log.i("my",jsonObject.toString());
                        pojo1.setTitle(jsonObject.getString("title"));
                        pojo1.setPrice(jsonObject.getString("price"));
                        pojo1.setImage(jsonObject.getString("image"));
                        pojo1.setImageLoader(imageLoader);
                        itemsList.add(pojo1);

                        if (jsonArray.length()>0){
                onClickListener();
                        }
                    }



                    //    itemsList.clear();

// refreshing recycler view
                    mAdapter.notifyDataSetChanged();

                }catch (UnsupportedEncodingException |JSONException e){
                    Log.e(TAG, "Error: " + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(cacheRequest);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

   public void onClickListener(){
      mAdapter.setOnCardClickListener(new CustomAdapter.OnCardClickListener() {
          @Override
          public void OnCardClicked(View view, int position) {
              Toast.makeText(getApplicationContext(),itemsList.get(position).getTitle(),Toast.LENGTH_SHORT).show();
          }
      });

      mAdapter.setOnTextClickListener(new CustomAdapter.OnTextClickListener() {
          @Override
          public void OnCardClicked(View view, int position) {
              Toast.makeText(getApplicationContext(),itemsList.get(position).getPrice(),Toast.LENGTH_SHORT).show();
          }
      });
   }

}
