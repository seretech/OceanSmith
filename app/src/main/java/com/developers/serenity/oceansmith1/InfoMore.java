package com.developers.serenity.oceansmith1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoMore extends AppCompatActivity {

    private ListView ls;
    private ProgressBar prog;
    private RequestQueue requestQueue;

    private ArrayList<InfoClass> arrayList;

    private int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_more);

        ls = findViewById(R.id.ls);
        prog = findViewById(R.id.prog);
        Toolbar toolbar = findViewById(R.id.toolBar);

        requestQueue = Volley.newRequestQueue(this);

        prog.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue_dark), PorterDuff.Mode.SRC_IN);

        Intent intent = getIntent();
        toolbar.setSubtitle(intent.getStringExtra("name"));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(intent.getStringExtra("id") != null){
            x =  Integer.parseInt(intent.getStringExtra("id"));
            loadData(x);
        }

    }

    private void loadData(int id) {
        String url = "https://www.fishwatch.gov/api/species";
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray0 = new JSONArray(response);
                JSONObject js = jsonArray0.optJSONObject(id);
                if(js.optJSONArray("Image Gallery") == null){
                    errorSheet();
                } else {
                    JSONArray jsonArray = js.getJSONArray("Image Gallery");
                    for (int k = 0; k < jsonArray.length(); k++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(k);
                        String nam = jsonObject1.optString("title", "na");
                        String img= jsonObject1.optString("src", "na");

                        InfoClass cl = new InfoClass();
                        cl.setName(nam);
                        cl.setHabitat("non");
                        cl.setImg(img);
                        arrayList.add(cl);
                    }
                }


                InfoAdapter adapter = new InfoAdapter(this, R.layout.info_adapter, arrayList);
                adapter.setListData(arrayList);
                ls.setAdapter(adapter);
                prog.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                loadData0(x);
            }
        },
                e -> loadData0(x));
        requestQueue.add(request);
    }

    private void loadData0(int id) {
        String url = "https://www.fishwatch.gov/api/species";
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray0 = new JSONArray(response);
                JSONObject js = jsonArray0.optJSONObject(id);
                if(js.optJSONArray("Image Gallery") == null){
                    errorSheet();
                } else {
                    JSONArray jsonArray = js.getJSONArray("Image Gallery");
                    for (int k = 0; k < jsonArray.length(); k++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(k);
                        String nam = jsonObject1.optString("title", "na");
                        String img= jsonObject1.optString("src", "na");

                        InfoClass cl = new InfoClass();
                        cl.setName(nam);
                        cl.setHabitat("non");
                        cl.setImg(img);
                        arrayList.add(cl);
                    }
                }

                InfoAdapter adapter = new InfoAdapter(this, R.layout.info_adapter, arrayList);
                adapter.setListData(arrayList);
                ls.setAdapter(adapter);
                prog.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                Toast.makeText(this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        },
                e -> netRetry());
        requestQueue.add(request);
    }

    private void errorSheet() {
        prog.setVisibility(View.INVISIBLE);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_single, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        bottomSheetDialog.show();

        TextView txt1 = view.findViewById(R.id.txt);
        txt1.setText(R.string.empty_gallery);

        txt1.setOnClickListener(v-> {
            onBackPressed();
            loadData(x);
        });


    }

    private void netRetry() {
        prog.setVisibility(View.INVISIBLE);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_single, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        bottomSheetDialog.show();

        TextView txt1 = view.findViewById(R.id.txt);
        txt1.setText(String.format("%s\n%s", getString(R.string.net_err), getString(R.string.click_to_retry)));

        txt1.setOnClickListener(v-> {
            prog.setVisibility(View.VISIBLE);
            bottomSheetDialog.dismiss();
            loadData(x);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}