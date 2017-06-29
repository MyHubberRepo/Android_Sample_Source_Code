package com.myhubber.myhubber.hubboards_directory.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.myhubber.myhubber.ApplicationSingleton;
import com.myhubber.myhubber.ClassifiedDetailActivity;
import com.myhubber.myhubber.R;
import com.myhubber.myhubber.hubboards_directory.models.AdDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Midhun ek on 4/3/17,
 * Ideaprodigies,
 * Dubai, UAE.
 */
/**
 * Class name  : HubBoardJobAdapter
 * Description : Adapter class to handle HubboardJobsList
 */

public class HubBoardJobAdapter extends
        RecyclerView.Adapter<HubBoardJobAdapter.HubBoardJobAdapterHolder>  {

    private List<AdDetail> jobsList         = new ArrayList<>();
    private Typeface myTypeFace             = ApplicationSingleton.avanirLtStd;
    private Typeface avanirLtd              = ApplicationSingleton.avanirLtStd;
    private Typeface avanirHeavy            = ApplicationSingleton.avanirHeavy;

    private Context mContext;

    /**
     *
     * @param jobsList
     * @param context
     */
    public HubBoardJobAdapter(List<AdDetail> jobsList, Context context){
        this.jobsList=jobsList;
        this.mContext=context;
    }
    /**
     *
     * @param parent
     * @param viewType
     * @return view
     */
    @Override
    public HubBoardJobAdapter.HubBoardJobAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View companyView    = LayoutInflater.from(parent.getContext()).inflate(R.layout.hbb_job_list_item, parent, false);
        return new HubBoardJobAdapter.HubBoardJobAdapterHolder(companyView);

    }
    /**
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final HubBoardJobAdapter.HubBoardJobAdapterHolder viewHolder, final int position) {

        final AdDetail product  = jobsList.get(position);

        viewHolder.jobSalary.setTypeface(avanirHeavy);
        viewHolder.jobName.setTypeface(myTypeFace);
        viewHolder.jobPosition.setTypeface(myTypeFace);
        viewHolder.jobSalaryTitle.setTypeface(myTypeFace);


        Glide.with(mContext).load(product.getImageurl())
                .asBitmap().centerCrop()
                .error(R.drawable.hubboard_job_image)
                .into(new BitmapImageViewTarget(viewHolder.jobImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder.jobImageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

        if(product.getPrice().equalsIgnoreCase("0")) {
            viewHolder.jobSalary.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.jobSalary.setVisibility(View.VISIBLE);
            String s= "AED "+product.getPrice();
            viewHolder.jobSalary.setText(s);
        }

        viewHolder.jobName.setText(product.getTitle());
        viewHolder.jobSalaryTitle.setText("Salary");
        viewHolder.jobPosition.setText(Html.fromHtml(product.getDesc()));
        viewHolder.jobImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.jobImageView.performClick();
            }
        });
        viewHolder.baseLaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.jobName.performClick();
            }
        });
        viewHolder.jobName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToDetail=new Intent(mContext, ClassifiedDetailActivity.class);
                moveToDetail.putExtra("adsId", product.getAdID());
                moveToDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(moveToDetail);
            }
        });
    }

    /**
     *
     * @return jobsList
     */
    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public void clearAdapter() {
        this.jobsList.clear();
        notifyDataSetChanged();
    }

    /**
     * Recycler viewholder class for hubboardJobsAdapter.
     */
    public class HubBoardJobAdapterHolder extends RecyclerView.ViewHolder {


        private TextView    jobName;
        private TextView    jobPosition;
        private TextView    jobSalary;
        private TextView    jobSalaryTitle;
        private ImageView   jobImageView;
        private LinearLayout baseLaout;

        /**
         *
         * @param convertView
         */
        public HubBoardJobAdapterHolder(View convertView) {
            super(convertView);

            jobName         = (TextView)convertView.findViewById(R.id.jobName);
            jobPosition     = (TextView)convertView.findViewById(R.id.jobPosition);
            jobSalary       = (TextView)convertView.findViewById(R.id.jobSalary);
            jobSalaryTitle  = (TextView)convertView.findViewById(R.id.jobSalaryTitle);
            jobImageView    = (ImageView) convertView.findViewById(R.id.jobImage);
            baseLaout       = (LinearLayout)convertView.findViewById(R.id.jobBaseLayout);
        }

    }
    /**
     *
     * @param restaurantsList
     */
    public void setList(List<AdDetail> restaurantsList) {

        if (this.jobsList != null && restaurantsList != null) {
            this.jobsList.addAll(restaurantsList);
        } else {
            this.jobsList = restaurantsList;
        }
    }

    public List<AdDetail> getSelection() {
        return jobsList;
    }

}