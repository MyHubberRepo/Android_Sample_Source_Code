package com.myhubber.myhubber.hubboards_directory.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myhubber.myhubber.ApplicationSingleton;
import com.myhubber.myhubber.ClassifiedDetailActivity;
import com.myhubber.myhubber.R;
import com.myhubber.myhubber.chat_latest.hubberitems.HubberItemActivity;
import com.myhubber.myhubber.hubboards_directory.models.AdDetail;
import com.myhubber.myhubber.hubboards.data.HubboardsDefault;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.myhubber.myhubber.ApplicationSingleton.avanirHeavy;

/**
 * Created by Midhun ek on 4/3/17,
 * Ideaprodigies,
 * Dubai, UAE.
 */
/**
 * Class name  : HubBoardsFeedsAdapter
 * Description : Adapter class to handle HubboardFeedsList
 */

public class HubBoardsFeedsAdapter extends
        RecyclerView.Adapter<HubBoardsFeedsAdapter.HubBoardItemsAdapterHolder>  {

    private List<AdDetail> adDetailList = new ArrayList<>();
    private Typeface myTypeFace         = ApplicationSingleton.avanirLtStd;
    private Typeface avenirLtd          = ApplicationSingleton.avanirLtStd;
    private Typeface avenirHeavy        = avanirHeavy;
    private Context mContext;

    Activity activity;


    public HubBoardsFeedsAdapter(List<AdDetail> levelList, Context context, Activity activity){

        this.adDetailList   = levelList;
        this.mContext       = context;
        this.activity       = activity;
    }
    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public HubBoardsFeedsAdapter.HubBoardItemsAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View companyView= LayoutInflater.from(parent.getContext()).inflate(R.layout.hbb_item_list, parent, false);
        return new HubBoardsFeedsAdapter.HubBoardItemsAdapterHolder(companyView);

    }

    /**
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final HubBoardsFeedsAdapter.HubBoardItemsAdapterHolder viewHolder, final int position) {

        final AdDetail product  = adDetailList.get(position);
        String strUrl           = "";
        String videoThumb       = product.getVideothumb();

        viewHolder.addPrice.setTypeface(avenirHeavy);
        viewHolder.addTitle.setTypeface(myTypeFace);


        if((videoThumb!="") && videoThumb!=null && videoThumb.length() > 0) {
            strUrl = HubboardsDefault.HUBBOARDS_IMAGE_URL + product.getVideothumb();
            strUrl = strUrl.replaceAll(" ", "%20");

        } else {

            String imgUrl   = product.getMedia();
            if(imgUrl!=null&&!(imgUrl.equals(""))) {

                strUrl = product.getImageurl();
                strUrl = strUrl.replaceAll(" ", "%20");
                Picasso.with(mContext)
                        .load(strUrl)
                        .centerCrop()
                        .resize(300,300)
                        .placeholder(R.drawable.hubboards_default)
                        .into(viewHolder.addImage);
            }else{

                if(product.getCategory().contains("Classifieds")) {
                    viewHolder.addImage.setImageResource(R.drawable.classifieds_default);
                }else if(product.getCategory().contains("Jobs")){
                    viewHolder.addImage.setImageResource(R.drawable.jobs_default);
                }else if(product.getCategory().contains("Vehicles")) {
                    viewHolder.addImage.setImageResource(R.drawable.vehicles_default);
                }else if(product.getCategory().contains("Property")){
                    viewHolder.addImage.setImageResource(R.drawable.property_default);
                }else if(product.getCategory().contains("Services")) {
                    viewHolder.addImage.setImageResource(R.drawable.services_default);
                }
            }

        }
        viewHolder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ApplicationSingleton.isFromChat){

                    ((HubberItemActivity)activity).showdIALOG("Hubboard",product.getAdID());

                }else {
                    Intent editAds = new Intent(mContext,ClassifiedDetailActivity.class);
                    editAds.putExtra("adsId", product.getAdID());
                    editAds.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(editAds);
                }
            }
        });


        if(product.getPrice().equalsIgnoreCase("0"))
        {
            viewHolder.addPrice.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.addPrice.setVisibility(View.VISIBLE);
            String s= "AED "+product.getPrice();
            viewHolder.addPrice.setText(s);
        }
        viewHolder.addTitle.setText(product.getTitle());
    }

    @Override
    public int getItemCount() {
        return adDetailList.size();
    }

    public void clearAdapter() {

        this.adDetailList.clear();
    }

    public class HubBoardItemsAdapterHolder extends RecyclerView.ViewHolder {


        ImageView   addImage;
        TextView    addTitle;
        TextView    addPrice;

        public HubBoardItemsAdapterHolder(View convertView) {
            super(convertView);

            addImage    = (ImageView)convertView.findViewById(R.id.addImageLatest);
            addPrice    = (TextView)convertView.findViewById(R.id.addPriceLatest);
            addTitle    = (TextView)convertView.findViewById(R.id.addTitleLatest);
        }
    }
    /**
     *
     * @param restaurantsList
     */
    public void setList(List<AdDetail> restaurantsList) {

        if (this.adDetailList != null && restaurantsList != null) {
            this.adDetailList.addAll(restaurantsList);
        } else {
            this.adDetailList = restaurantsList;
        }
    }

    /**
     *
     * @return addDetails list
     */
    public List<AdDetail> getSelection() {
        return adDetailList;
    }

}