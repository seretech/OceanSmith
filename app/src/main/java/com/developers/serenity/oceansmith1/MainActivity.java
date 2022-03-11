package com.developers.serenity.oceansmith1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private ListView ls;
    private ProgressBar prog;
    private RequestQueue requestQueue;

    private ArrayList<InfoClass> arrayList;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ls = findViewById(R.id.ls);
        prog = findViewById(R.id.prog);

        requestQueue = Volley.newRequestQueue(this);

        prog.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue_dark), PorterDuff.Mode.SRC_IN);

        loadData();

        ls.setOnItemClickListener((adapterView, view, i, l) -> {
            InfoClass infoClass = arrayList.get(i);
            Intent intent = new Intent(getApplicationContext(), InfoMore.class);
            intent.putExtra("name", infoClass.getName());
            intent.putExtra("id", infoClass.getId());
            startActivity(intent);
        });

    }

    private void loadData() {
        String url = "https://www.fishwatch.gov/api/species";
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject1 = jsonArray.optJSONObject(k);
                            String id = ""+k;
                            String nam = jsonObject1.optString("Species Name", "na");
                            String hb = jsonObject1.optString("Habitat Impacts", "na");
                            String img= jsonObject1.getJSONObject("Species Illustration Photo").optString("src", "na");

                            InfoClass cl = new InfoClass();
                            cl.setId(id);
                            cl.setName(nam);
                            cl.setHabitat(String.format("%s\n%s", Html.fromHtml("<u>Habitat</u>"),hb));
                            cl.setImg(img);
                            arrayList.add(cl);
                        }

                        InfoAdapter adapter = new InfoAdapter(this, R.layout.info_adapter, arrayList);
                        adapter.setListData(arrayList);
                        ls.setAdapter(adapter);
                        prog.setVisibility(View.INVISIBLE);

                    } catch (JSONException e) {
                        loadData0();
                    }
                },
                e -> loadData0());
        requestQueue.add(request);
    }

    private void loadData0() {
        String url = "https://www.fishwatch.gov/api/species";
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject1 = jsonArray.optJSONObject(k);
                            String id = ""+k;
                            String nam = jsonObject1.optString("Species Name", "na");
                            String hb = jsonObject1.optString("Habitat Impacts", "na");
                            String img= jsonObject1.getJSONObject("Species Illustration Photo").optString("src", "na");

                            InfoClass cl = new InfoClass();
                            cl.setId(id);
                            cl.setName(nam);
                            cl.setHabitat(String.format("%s\n%s", Html.fromHtml("<u>Habitat</u>"),hb));
                            cl.setImg(img);
                            arrayList.add(cl);
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
            loadData();
        });


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(getString(R.string.sure_to_exit))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.no),
                        (dialog, which) -> dialog.dismiss())
                .setNegativeButton(getString(R.string.yes_exit),
                        (dialog, which) -> finishAffinity());
        dialog = alertDialogBuilder.create();
        dialog.show();

    }

}