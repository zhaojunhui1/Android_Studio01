package com.example.myday1demo01;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends BaseAdapter {
    private List<ShopBean.DataBean> mData;
    private Context mContext;

    public ViewAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }


    public void setDatas(List<ShopBean.DataBean> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addDatas(List<ShopBean.DataBean> data) {
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    private boolean hasData() {
        return mData.size() > 0;
    }

    @Override
    public int getCount() {
        return hasData() ? mData.size() + 1 : mData.size();
    }

    @Override
    public ShopBean.DataBean getItem(int position) {
        return mData.get(hasData() ? position - 1 : position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private final int COUNT_ITEM = 2;
    private final int BANNER_ITEM = 0;
    private final int NEWS_ITEM = 1;

    @Override
    public int getViewTypeCount() {
        return COUNT_ITEM;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && hasData() ? BANNER_ITEM : NEWS_ITEM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == BANNER_ITEM) {
            BannerHolder mHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.module_banner, parent, false);
                mHolder = new BannerHolder(convertView);
            } else {
                mHolder = (BannerHolder) convertView.getTag();
            }
            mHolder.bindData(mContext, mData);
        } else {
            NewsHolder mHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.module_news, parent, false);
                mHolder = new NewsHolder(convertView);
            } else {
                mHolder = (NewsHolder) convertView.getTag();
            }
            mHolder.bindData(getItem(position));
        }
        return convertView;
    }


    class BannerHolder {
        Banner banner;
        public BannerHolder(View itemView) {
            banner = itemView.findViewById(R.id.banner);
            itemView.setTag(this);
        }
        public void bindData(Context mContext, List<ShopBean.DataBean> mBanner) {
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            //设置图图片的加载器
            banner.setImageLoader(new ImageLoaderInterface<ImageView>() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    ShopBean.DataBean mBanner = (ShopBean.DataBean) path;
                    ImageLoader.getInstance().displayImage(mBanner.getImg(), imageView);
                }
                @Override
                public ImageView createImageView(Context context) {
                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return imageView;
                }
            }); initData(mBanner);
        }
        private void initData(List<ShopBean.DataBean> mBanner) {
            banner.setImages(mBanner);
            banner.setBannerTitles(getTitles(mData));
            banner.start();
        }
        private List<String> getTitles(List<ShopBean.DataBean> mBanner) {
            List<String> result = new ArrayList<>();
            for (ShopBean.DataBean banner : mBanner){
                result.add(banner.getTitle());
            }
            return result;
        }
    }

        class NewsHolder {
            TextView textView1, textView2, textView3;

            public NewsHolder(View itemView) {
                textView1 = itemView.findViewById(R.id.text1);
                textView2 = itemView.findViewById(R.id.text2);
                textView3 = itemView.findViewById(R.id.text3);
                itemView.setTag(this);
            }

            public void bindData(ShopBean.DataBean item) {
                textView1.setText(item.getCategory());
                textView2.setText(item.getAuthor_name());
                textView3.setText(item.getTitle());
                //中划线
                textView2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

    }
