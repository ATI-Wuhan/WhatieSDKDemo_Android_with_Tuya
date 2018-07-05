package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.utils.BaseRecyclerAdapter;
import com.whatie.ati.androiddemo.utils.RecyclerViewHolder;
import com.d9lab.ati.whatiesdk.bean.BaseListResponse;
import com.d9lab.ati.whatiesdk.bean.Product;
import com.d9lab.ati.whatiesdk.callback.ProductionCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by liz on 2018/4/24.
 */

public class ProductionListActivity extends BaseActivity {
    private static final String TAG = "ProductionListActivity";
    @BindView(R.id.rv_product_list)
    RecyclerView rvProductList;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    private BaseRecyclerAdapter<Product> mAdapter;
    private List<Product> mProductions = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.act_product_list;
    }

    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.production_list));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvProductList.setLayoutManager(layoutManager);
        EHomeInterface.getINSTANCE().getProductTypePage(mContext, 1, 10, new ProductionCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<Product>> response) {
                if(response.body().isSuccess()){
                    mProductions.addAll(response.body().getPage().getList());
                    mAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(mContext,getString(R.string.production_list_get_fail),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<BaseListResponse<Product>> response) {
                super.onError(response);
                Toast.makeText(mContext,getString(R.string.production_list_get_fail),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initEvents() {

        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initDatas() {
        mAdapter = new BaseRecyclerAdapter<Product>(mContext, mProductions) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_product;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, final Product item) {
                holder.setText(R.id.tv_product_name, item.getName());
                holder.setClickListener(R.id.rl_product_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ProductionListActivity.this, ConfirmDeviceActivity.class);
//                        intent.putExtra("productType",item.getProductType());
                        startActivity(intent);
                    }
                });
            }
        };
        rvProductList.setAdapter(mAdapter);
    }

}
