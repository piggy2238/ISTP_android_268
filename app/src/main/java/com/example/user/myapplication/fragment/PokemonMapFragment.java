package com.example.user.myapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by User on 2016/8/24.
 */
public class PokemonMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
