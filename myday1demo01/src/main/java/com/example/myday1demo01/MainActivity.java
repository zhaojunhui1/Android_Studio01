package com.example.myday1demo01;

import android.content.SyncRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewAdapter mAdapter;
    private PullToRefreshListView refreshListView;
    private int mPage;
    private String urlStr = "http://www.xieast.com/api/banner.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshListView = findViewById(R.id.refresh_listview);
        mAdapter = new ViewAdapter(this);
        refreshListView.setAdapter(mAdapter);
        mPage = 1;
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
        initData();
    }

    private void initData() {
        NetUtils.getInstance().getRequest(urlStr, ShopBean.class, new NetUtils.CallBack<ShopBean>() {
            @Override
            public void OnSuccess(ShopBean shopBean) {
                if(shopBean == null){
                    Toast.makeText(MainActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
                    refreshListView.onRefreshComplete();
                    return;
                }
                if (mPage == 1){
                    mAdapter.setDatas(shopBean.getData());
                }else {
                    mAdapter.addDatas(shopBean.getData());
                }
                mPage ++;
                refreshListView.onRefreshComplete();
            }
        });
    }
}
