package com.example.raternet_isp_app.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raternet_isp_app.Constants;
import com.example.raternet_isp_app.MainActivity2;
import com.example.raternet_isp_app.R;
import com.example.raternet_isp_app.endpoints.GetDataService;
import com.example.raternet_isp_app.network.RetrofitClientInstance;
import com.example.raternet_isp_app.network.RetrofitClientInstance2;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ISPInfo extends Fragment {

    private TextView ISPView;
    private ProgressDialog progressDialog;
    private String ip = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.isp_view, container, false);
        ISPView = view.findViewById(R.id.ISP);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchIPInfo();
    }

    public void fetchIPInfo(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting your Network Info..."); // show progess dialog till server responds
        progressDialog.show();
        GetDataService serviceIP= RetrofitClientInstance.getRetrofitInstance()
                .create(GetDataService.class);
        Call<String> callIP = serviceIP.getIP();
        callIP.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    ip = response.body();
                    GetDataService serviceISP= RetrofitClientInstance2.getRetrofitInstance()
                            .create(GetDataService.class);
                    Call<JsonObject> callISP = serviceISP.getIPInfo(ip);
                    callISP.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressDialog.dismiss();
                            try{
                                JsonObject jsonObject = response.body();
                                //Setting ISP in Constants
                                Constants.ISP_Name=jsonObject.get("isp").getAsString();
                                ISPView.setText(jsonObject.get("isp").getAsString());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                ISPView.setText("Json Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("ISP Error",t.getMessage());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    ISPView.setText("IP Error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("IP Error",t.getMessage());
            }
        });
    }
}
//Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
/*
RESPONSES:
* D/OkHttp: <-- 200 OK https://api.ipify.org/ (1156ms)
D/OkHttp: Server: Cowboy
    Connection: keep-alive
    Content-Type: text/plain
    Vary: Origin
    Date: Fri, 30 Oct 2020 07:49:25 GMT
    Content-Length: 13
    Via: 1.1 vegur
D/OkHttp: 116.74.187.92
    <-- END HTTP (13-byte body)
    *
    *  I/ISP Error: CLEARTEXT communication to ip-api.com not permitted by network security policy
    *
    * D/OkHttp: <-- 200 OK http://ip-api.com/json/116.74.187.92 (538ms)
D/OkHttp: Date: Fri, 30 Oct 2020 10:56:57 GMT
    Content-Type: application/json; charset=utf-8
D/OkHttp: Content-Length: 339
    Access-Control-Allow-Origin: *
    X-Ttl: 60
    X-Rl: 44
D/OkHttp: {"status":"success","country":"India","countryCode":"IN","region":"MH",
"regionName":"Maharashtra","city":"Pune","zip":"411038","lat":18.5196,"lon":73.8554,
"timezone":"Asia/Kolkata","isp":"Hathway IP over Cable Internet Access",
"org":"Hathway Cable and Datacom Pvt Ltd","as":"AS17488 Hathway IP Over Cable Internet",
"query":"116.74.187.92"}
D/OkHttp: <-- END HTTP (339-byte body)
* */
