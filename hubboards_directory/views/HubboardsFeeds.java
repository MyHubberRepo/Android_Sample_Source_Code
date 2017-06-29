package com.myhubber.myhubber.hubboards_directory.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.myhubber.myhubber.ApplicationSingleton;
import com.myhubber.myhubber.R;
import com.myhubber.myhubber.hubboards.data.HubboardsDefault;
import com.myhubber.myhubber.hubboards_directory.adapter.HubBoardJobAdapter;
import com.myhubber.myhubber.hubboards_directory.adapter.HubBoardsFeedsAdapter;
import com.myhubber.myhubber.hubboards_directory.models.AdDetail;
import com.myhubber.myhubber.hubboards_directory.models.HBBProductList;
import com.myhubber.myhubber.hubstore_latest.interfaces.HBSProductClickListner;
import com.myhubber.myhubber.hubstore_latest.views.HBSBaseFragment;
import com.myhubber.myhubber.hubstore_latest.views.HBSDisplayInnerFragment;
import com.myhubber.myhubber.util.Util;
import com.myhubber.myhubber.webservices.ClientConfig;
import com.myhubber.myhubber.webservices.ClientInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Midhun ek on 4/2/17,
 * Ideaprodigies,
 * Dubai, UAE.
 * Class name   : HubboardsFeeds
 * Description  : List Hubboards feeds and jobs List
 */

public class HubboardsFeeds extends HBSBaseFragment implements HBSProductClickListner {


    private RecyclerView            hubboardFeedsRecyclerView;
    private HubBoardsFeedsAdapter   hubBoardsFeedsAdapter;
    private HubBoardJobAdapter      hubBoardJobAdapter;


    private boolean loading         = true;
    protected int lastPage          = 0;
    private int previousTotal       = 0;
    protected static final int limit= 50;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RecyclerView.LayoutManager layoutManager;


    private List<AdDetail> feedsList    = new ArrayList<AdDetail>();
    private List<AdDetail> jobList      = new ArrayList<AdDetail>();


    private ClientInterface clientInterface;
    private String selectedName="";
    private boolean isPageEnd;
    private String userIdString="userId";
    private LinearLayout jobTitleBar;

    private Button lookingForJob;
    private Button lookingForEmployee;
    private LinearLayout emptyLayout;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hbb_display_list_fragment, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hubboardFeedsRecyclerView       = (RecyclerView) view.findViewById(R.id.hubMunchRestuarent);
        jobTitleBar                     = (LinearLayout)view.findViewById(R.id.jobTabLayout);
        lookingForJob                   = (Button)view.findViewById(R.id.huNearbyMapButton);
        lookingForEmployee              = (Button)view.findViewById(R.id.huNearbyListButton);
        emptyLayout                     = (LinearLayout)view.findViewById(R.id.emptyLayout);
        selectedName                    = HubboardsDefault.categoryHeaderName;

        jobTitleBar.setVisibility(View.GONE);


        if (!HubboardsDefault.categoryHeaderName.equals("Jobs")&&!HubboardsDefault.categoryHeaderName.equals("Looking")
                &&!HubboardsDefault.categoryHeaderName.equals("Hiring")) {

//            Initialize Hubboards feeds list
            jobTitleBar.setVisibility(View.GONE);
            hubBoardsFeedsAdapter       = new HubBoardsFeedsAdapter(feedsList, getApplicationContext(),getActivity());
            layoutManager               = new GridLayoutManager(getActivity(), 2);

            hubboardFeedsRecyclerView.setLayoutManager(layoutManager);
            hubboardFeedsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            hubboardFeedsRecyclerView.setNestedScrollingEnabled(true);
            hubboardFeedsRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
            hubboardFeedsRecyclerView.setAdapter(hubBoardsFeedsAdapter);

//            Call webservice to get Feeds list
            requestFeedsWebservice();


        }else {

//            Initialize Hubboards Jobs list
            jobTitleBar.setVisibility(View.VISIBLE);
            hubBoardJobAdapter          = new HubBoardJobAdapter(jobList, getApplicationContext());
            layoutManager               = new LinearLayoutManager(getActivity());
            hubboardFeedsRecyclerView.setLayoutManager(layoutManager);
            hubboardFeedsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            hubboardFeedsRecyclerView.setNestedScrollingEnabled(true);
            hubboardFeedsRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
            hubboardFeedsRecyclerView.setAdapter(hubBoardJobAdapter);

//            Call webservice to get jobs list
            requestJobsWebservice();

        }

//        lookingForJob looking for jobs button click event
        lookingForJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lookingForJob.setBackgroundDrawable(getResources().getDrawable(R.drawable.hubnearby_mapview_bg));
                lookingForEmployee.setBackgroundDrawable(getResources().getDrawable(R.drawable.hubnearby_list_unselected));
                lookingForJob.setTextColor(Color.WHITE);
                lookingForEmployee.setTextColor(Color.BLACK);
                jobList.clear();
                hubBoardJobAdapter.clearAdapter();
                HubboardsDefault.classifiedCategoryID="14";

                /**
                 * @Call webservice to get Feeds list
                 */
                requestJobsWebservice();

            }
        });

//        lookingForEmployee looking for employee button click event
        lookingForEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lookingForJob.setBackgroundDrawable(getResources().getDrawable(R.drawable.hubnearby_map_unselected));
                lookingForEmployee.setBackgroundDrawable(getResources().getDrawable(R.drawable.hubnearby_listview_bg));
                lookingForEmployee.setTextColor(Color.WHITE);
                lookingForJob.setTextColor(Color.BLACK);
                jobList.clear();
                HubboardsDefault.classifiedCategoryID="13";
                jobList.clear();
                hubBoardJobAdapter.clearAdapter();

//                Call webservice to get Jobs list
                requestJobsWebservice();

            }
        });
    }

    /**
     *
     * @param savedInstanceState
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * @Call webservice to get Jobs list
     * @userId id of the user
     * @origLat latitude of the user
     * @origLon longitude of the user
     * @catID selected category id
     * @page required page
     * @per_page total item per page
     * @userIdString logged user id
     */
    private void requestFeedsWebservice() {

        String userId               = "";
        String userLatitude         = "";
        String userLongitude        = "";
        String catId                = "";
        int page                    = 0;

        try {
            userId                  = ApplicationSingleton.LOGGEDIN_USER.getUserID();
            userLatitude            = ApplicationSingleton.userLatitude;
            userLongitude           = ApplicationSingleton.userLongitude;
            page                    = Util.getPageNumber(lastPage, limit, hubBoardJobAdapter.getItemCount());
            catId                   = HubboardsDefault.classifiedCategoryID;

        }catch (Exception e){
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
            map.put("listAllAd",    "1");
            map.put("origLat",       userLatitude);
            map.put("origLon",       userLongitude);
            map.put("catID",         catId);
            map.put("page",          page+"");
            map.put("per_page",      "20");
            map.put(userIdString,    userId);

        clientInterface             = ClientConfig.getRestAdapter().create(ClientInterface.class);
        clientInterface.getHubBoardOthersProduct(map, new Callback<HBBProductList>(){
            @Override
            public void success(HBBProductList achievements, Response response) {

                try {
                    feedsList     = achievements.getAdDetails();

                    if (feedsList != null) {
                        if (feedsList.size()>0) {
                            emptyLayout.setVisibility(View.GONE);

                            /**
                             * Set list into hubboardFeedsAdapter
                             */
                            hubBoardsFeedsAdapter.setList(feedsList);
                            hubBoardsFeedsAdapter.notifyDataSetChanged();
                        }else {
                            if (hubBoardsFeedsAdapter.getItemCount()>0){
                                emptyLayout.setVisibility(View.GONE);
                            }else {
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    emptyLayout.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    /**
     * @Call webservice to get Jobs list
     * @userId id of the user
     * @origLat latitude of the user
     * @origLon longitude of the user
     * @catID selected category id
     * @page required page
     * @per_page total item per page
     * @userIdString logged user id
     */
    private void requestJobsWebservice() {

        String userId               = "";
        String userLatitude         = "";
        String userLongitude        = "";
        String catId                = "";
        int page                    = 0;


        try {
            userId                  = ApplicationSingleton.LOGGEDIN_USER.getUserID();
            userLatitude            = ApplicationSingleton.userLatitude;
            userLongitude           = ApplicationSingleton.userLongitude;
            page                    = Util.getPageNumber(lastPage, limit, hubBoardJobAdapter.getItemCount());
            catId                   = HubboardsDefault.classifiedCategoryID;

        }catch (Exception e){
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();

            map.put("listAllAd",    "1");
            map.put("origLat",      userLatitude);
            map.put("origLon",      userLongitude);
            map.put("catID",        catId);
            map.put("page",         page+"");
            map.put("per_page",     "20");
            map.put(userIdString,   userId);

        clientInterface             = ClientConfig.getRestAdapter().create(ClientInterface.class);
        clientInterface.getHubBoardOthersProduct(map, new Callback<HBBProductList>(){
            @Override
            public void success(HBBProductList achievements, Response response) {

                try {

                    jobList = achievements.getAdDetails();

                    if (jobList != null) {
                        if (jobList.size()>0) {
                            emptyLayout.setVisibility(View.GONE);

//                            Set jobsList into hubboardJobsAdapter
                            hubBoardJobAdapter.setList(jobList);
                            hubBoardJobAdapter.notifyDataSetChanged();
                        }else {
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    /**
     * Scroll listener for the recyclerview
     */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0)
            {
                visibleItemCount        = layoutManager.getChildCount();
                totalItemCount          = layoutManager.getItemCount();
                pastVisiblesItems       = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading         = false;
                        previousTotal   = totalItemCount;
                    }
                }
                if (!loading
                        && (totalItemCount - visibleItemCount) <= (pastVisiblesItems + 9)) {
                    lastPage++;
                    ApplicationSingleton.APPLICATION_POST_PAGE=lastPage;

                    if (!isPageEnd){
                        if (!selectedName.equals("Jobs")){
                            requestFeedsWebservice();
                        }else {
                            requestJobsWebservice();
                        }
                    }
                    loading = true;
                }
            }
        }
    };

    /**
     *
     * @param productId Interface for Hubboard list item click
     */
    @Override
    public void productClickListner(String productId) {
        ((HBSDisplayInnerFragment)getParentFragment()).moveToProductDisplay(productId);
    }

}
