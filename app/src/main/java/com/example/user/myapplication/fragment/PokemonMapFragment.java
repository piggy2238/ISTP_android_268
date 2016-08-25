package com.example.user.myapplication.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.myapplication.GeoCodingTask;
import com.example.user.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by User on 2016/8/24.
 */
public class PokemonMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GeoCodingTask.GeoCodingResponse, GoogleApiClient.ConnectionCallbacks {

    //宣告變數
    View fragmentView;
    GoogleMap map;

    GoogleApiClient googleApiClient;
    public final static int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;

    //初始化-1 建立 fragment 物件
    public static PokemonMapFragment newInstance() {

        Bundle args = new Bundle();

        PokemonMapFragment fragment = new PokemonMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //初始化-2 建立 物件
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //初始化-3 建立 View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(fragmentView == null){

            fragmentView = inflater.inflate(R.layout.fragment_map,container,false);

            setHasOptionsMenu(true);
            setMenuVisibility(true);
        }

        return fragmentView;
    }

    //初始化-4 綁定 ChildFragment
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment mapFragment = MapFragment.newInstance(); //直接使用 google 製作的 Instance
        mapFragment.getMapAsync(this); // background 執行 取得google map 的服務, 告訴他由此 fragment 來處理callback 之後的資料處理及呈現
        getChildFragmentManager().beginTransaction()
                .replace(R.id.childFragmentContainer, mapFragment) //管理 fragment_map(父) 底下的 childFragmentContainer (子)
                .commit();

    }

    //當 Map 準備好後 一開始要開通給使用者的功能 (UI control Function)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);

        //UI 控制功能
        UiSettings mapSettings = googleMap.getUiSettings(); //從 googleMap 拿 UI設定功能
        mapSettings.setZoomControlsEnabled(true); // controlled by UI widgets. 滑鼠或觸控筆 等
        mapSettings.setZoomGesturesEnabled(true); // 用手勢控制

        //預設的視窗中心
        (new GeoCodingTask(PokemonMapFragment.this)).execute("台北市羅斯福路四段一號");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /*拿到經緯度以後 要執行下面這件事
    * 拿到經緯度以後, 就是要move到地圖中間
    * 這邊先以固定的地圖中心為例: 國立台灣大學
    * 設定好後別忘了到 onMapReady 加上去 不然就失去預設值的意義*/
    @Override
    public void callbackWithGeoCodingResult(LatLng latLng) {

        //移動台大到視窗的視野中間_參數:經緯度,zoom-in比例
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        map.moveCamera(cameraUpdate);

        //插旗子
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("NTU")
                .snippet("National Taiwan University");
        map.addMarker(markerOptions);

        //顯示地圖後, 能夠順便要到 使用者的位置資訊
        createGoogleApiClient();
    }

    /*透過google API 去存取 google 各種服務 EX.位置資訊
    * 1. 先 build 一個 googleapiClient 物件 取得所在位置
    * 2. 連接 API 跟他要求我們想要做的事情
    * 3. 詢問使用者是否允許使用位置資訊的權限
    * 4. 判定使用者是否同意，若同意則進行存取當前位置
    * 5. 設定並存取使用者當前位置
    * genymotion 必須要開 GPS 設定位置 才能測試這部分的功能
    * */

    private void createGoogleApiClient(){

        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }

    //連上以後判定是否給予過權限
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            //未給過 要重新詢問是否給予權限 android 6.0.0 每次定位前都要問
            requestLocationPermissions(ACCESS_FINE_LOCATION_REQUEST_CODE);
        }else{
            //給過 就直接 設定當前位置
            SetMyLocationButtonEnabled();
        }
    }

    //3. 詢問使用者是否允許使用位置資訊的權限
    private void requestLocationPermissions(int requestCode) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(
                    new String[]
                            {
                                Manifest.permission.ACCESS_FINE_LOCATION
                            },
                    requestCode);
        }
    }

    //4. 判定使用者是否同意，若同意則進行存取當前位置
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                SetMyLocationButtonEnabled();
            }
        }
    }

    //5. 設定並存取使用者當前位置
    private void SetMyLocationButtonEnabled() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //若不同意就要再問一次 鬼打牆@@?
            requestLocationPermissions(ACCESS_FINE_LOCATION_REQUEST_CODE);
        } else {
            //存取當前位置
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}
}