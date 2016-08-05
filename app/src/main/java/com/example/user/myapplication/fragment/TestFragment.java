package com.example.user.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.myapplication.R;

import org.w3c.dom.Text;

/**
 * Created by User on 2016/8/5. define the inflation logic for the fragment of tab content
 */
// In this case, the fragment displays LogFragment based on the page
// Implementing TestFragment

public class TestFragment extends LogFragment {

    //宣告變數
    public static final String contentKey = "TestFragment.content";
    private String mContent;

/*建立fragment樣板
*
* */
    public static TestFragment newInstance(String content){

        TestFragment fragment = new TestFragment();
        //Supply index input as an argument.
        Bundle bundle = new Bundle();
        bundle.putString(contentKey,content);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getArguments().getString(contentKey);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_test,container,false);
        TextView textView = (TextView)fragmentView.findViewById(R.id.textView);
        textView.setText(mContent);



        return fragmentView ;
    }
}
