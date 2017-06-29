package com.myhubber.myhubber.hubboards_directory.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myhubber.myhubber.ApplicationSingleton;
import com.myhubber.myhubber.R;
import com.myhubber.myhubber.hubberapps.container.HubStoreContainer;
import com.myhubber.myhubber.hubberapps.hubstore.data.CommonData;
import com.myhubber.myhubber.hubberapps.hubstore.fragment.EditorItemsTabs;
import com.myhubber.myhubber.hubboards.data.HubboardsAPI;
import com.myhubber.myhubber.hubboards.data.HubboardsAPIListener;
import com.myhubber.myhubber.hubboards.data.HubboardsDefault;
import com.myhubber.myhubber.hubboards.fragment.PlaceAnAdFragment;
import com.myhubber.myhubber.hubboards.model.MyAdsItem;
import com.myhubber.myhubber.hubboards.rangeseekbar.RangeSeekBar;
import com.myhubber.myhubber.hubboards_directory.models.AdDetail;
import com.myhubber.myhubber.hubboards_directory.models.HubboardsConstants;
import com.myhubber.myhubber.hubstore_latest.adapters.topbaradapter.HBSTopbarAdapter;
import com.myhubber.myhubber.hubstore_latest.adapters.topbaradapter.HBSTopbarAdapterOne;
import com.myhubber.myhubber.hubstore_latest.adapters.topbaradapter.HBSTopbarAdapterTwo;
import com.myhubber.myhubber.hubstore_latest.interfaces.HBSChildChildrenClick;
import com.myhubber.myhubber.hubstore_latest.interfaces.HBSChildClick;
import com.myhubber.myhubber.hubstore_latest.interfaces.HBSMainCategoryClick;
import com.myhubber.myhubber.hubstore_latest.models.topbar_category.Child;
import com.myhubber.myhubber.hubstore_latest.models.topbar_category.Child_;
import com.myhubber.myhubber.hubstore_latest.models.topbar_category.HBSMenuCategory;
import com.myhubber.myhubber.hubstore_latest.views.HBSBaseFragment;
import com.myhubber.myhubber.models.HubstoreCategory;
import com.myhubber.myhubber.settings.SettingManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Midhun ek on 4/2/17.,
 * Ideaprodigies,
 * Dubai, UAE.
 * Class name   : HubBoardMainView
 * Description  : Base fragment for Hubboards feeds
 */

public class HubBoardMainView extends HBSBaseFragment implements HBSMainCategoryClick,HBSChildClick,HBSChildChildrenClick {


    private List<HBSMenuCategory>       hbsMenuCategoryList=new ArrayList<>();
    private RecyclerView.LayoutManager  mLayoutManager;


    private TextView tpBarBack;
    private TextView moduleName;
    private TextView subName;

    private Typeface myTypeFace  = ApplicationSingleton.milkShakeFont;
    private Typeface avanirHeavy = ApplicationSingleton.avanirBlack;
    private RecyclerView hbbTopBarRecyclerView;

    private HBSTopbarAdapter        hbsTopbarAdapter;
    private HBSTopbarAdapterOne     hbsTopbarAdapterOne;
    private HBSTopbarAdapterTwo     hbsTopbarAdapterTwo;
    private FloatingActionButton    addnewAdsButton;

    private RangeSeekBar<Integer>   rangeSeekBar;
    private int                     maxValuePrice =0, minValuePrice=0;

    private ImageView    backImage;
    private ImageView    searchIcon;
    private LinearLayout topBarLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.hbb_homedispaly, container, false);


        // Initialize Views
        initializeView(view);

        // Call webservice to get categories
        getNewCategories();


        // Add side drawer fragment
        if (savedInstanceState == null) {
            FragmentTransaction t = getFragmentManager().beginTransaction();
            HubboardsFeeds mSlidingMenuFragment = new HubboardsFeeds();
            t.replace(R.id.frame_innescreen, mSlidingMenuFragment);
            t.commit();
        }

        return view;
    }

    /**
     *
     * @param view
     */
    private void initializeView(View view){

        tpBarBack               = (TextView) view.findViewById(R.id.tpbarBackText);
        backImage               = (ImageView) view.findViewById(R.id.hubboardDrawer);
        moduleName              = (TextView)view.findViewById(R.id.tv_main_title);
        subName                 = (TextView)view.findViewById(R.id.tv_main_tiddtle);
        searchIcon              = (ImageView)view.findViewById(R.id.hubboardSearch);
        addnewAdsButton         = (FloatingActionButton)view.findViewById(R.id.addnewAdsButton);
        topBarLayout            = (LinearLayout)view.findViewById(R.id.topBarLayout);
        hbbTopBarRecyclerView   = (RecyclerView)view.findViewById(R.id.recyclerViewTpBar);


        HubStoreContainer.currentFrg = EditorItemsTabs.class.getSimpleName();

        tpBarBack.setVisibility(View.GONE);
        moduleName.setTypeface(avanirHeavy);
        subName.setTypeface(myTypeFace);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Open filter dialog for item search
                showSearchDialog();
            }
        });


        if (ApplicationSingleton.isFromChat){
            topBarLayout.setVisibility(View.GONE);
            addnewAdsButton.setVisibility(View.INVISIBLE);
        }else {

            topBarLayout.setVisibility(View.VISIBLE);
            addnewAdsButton.setVisibility(View.VISIBLE);
        }


        moduleName.setText(HubboardsConstants.hubStr);
        subName.setText(HubboardsConstants.boardStr);


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        addnewAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PlaceAnAdFragment.class);
                getActivity().startActivity(intent);
            }
        });


        tpBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tpBarBack.setVisibility(View.GONE);
                hbsTopbarAdapter    = new HBSTopbarAdapter(hbsMenuCategoryList, getApplicationContext(), HubBoardMainView.this);
                mLayoutManager      = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                hbbTopBarRecyclerView.setLayoutManager(mLayoutManager);
                hbbTopBarRecyclerView.setItemAnimator(new DefaultItemAnimator());
                hbbTopBarRecyclerView.setNestedScrollingEnabled(true);
                hbbTopBarRecyclerView.setAdapter(hbsTopbarAdapter);

            }
        });

//        Initialize seekbar (Price range)
        rangeSeekBar = new RangeSeekBar<Integer>(getActivity());
        rangeSeekBar.setRangeValues(minValuePrice, maxValuePrice);
        rangeSeekBar.setSelectedMinValue(minValuePrice);
        rangeSeekBar.setSelectedMaxValue(maxValuePrice);
        rangeSeekBar.setPadding(20,0,20,0);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                rangeSeekBar.setEnabled(true);
                rangeSeekBar.setSelectedMinValue(minValue);
                rangeSeekBar.setSelectedMaxValue(maxValue);

            }
        });
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Call webservice to get Hubboard categories
     * @getMethod
     */
    public void getNewCategories() {

        HubboardsAPI.getHubboardsCategory(getActivity(), new HubboardsAPIListener() {

            @Override
            public void onSuccess(String response) {

                Gson gson = new Gson();
                Type collectionType = new TypeToken<List<HBSMenuCategory>>() {
                }.getType();

                hbsMenuCategoryList = gson.fromJson(response.toString(), collectionType);
                if (hbsMenuCategoryList!=null){

//                    Populate category on top recyclerview
                    populateTbas(hbsMenuCategoryList);
                }

//                Parse category response json and store in to Common data
                parseJson(response);
                try {
                    SettingManager.setStoreCategories(getApplicationContext(), response);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure() {

            }
        });
    }
    /**
     *
     * @param response category response string
     */
    public void parseJson(String response) {

        try {

            JSONArray jObj = new JSONArray(response);
            CommonData.categoryItemsHub.clear();

            for (int i = 0; i < jObj.length(); i++) {
                HubstoreCategory categoryItem = new HubstoreCategory();
                JSONObject actor = jObj.getJSONObject(i);

                String id       = actor.getString("id");
                String text     = actor.getString("text");
                JSONArray array = actor.getJSONArray("children");
                String catLevel = actor.getString("catlevel");

                categoryItem.setId(id);
                categoryItem.setText(text);
                categoryItem.setItems(array);
                categoryItem.setCatLevel(catLevel);
            }

        }catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param hbsCategoryList top bat category list
     */
    private void populateTbas(List<HBSMenuCategory> hbsCategoryList) {

        hbsTopbarAdapter    = new HBSTopbarAdapter(hbsCategoryList, getApplicationContext(),HubBoardMainView.this);
        mLayoutManager      = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        hbbTopBarRecyclerView.setLayoutManager(mLayoutManager);
        hbbTopBarRecyclerView.setItemAnimator(new DefaultItemAnimator());
        hbbTopBarRecyclerView.setNestedScrollingEnabled(true);
        hbbTopBarRecyclerView.setAdapter(hbsTopbarAdapter);
    }

    /**
     *
     * @param text selected category name
     * @param menuCategory selected category model
     */

    @Override
    public void clickListner(String text, HBSMenuCategory menuCategory) {

        if (menuCategory!=null) {
            if (menuCategory.getChildren()!=null&&menuCategory.getChildren().size() > 0) {

                tpBarBack.setVisibility(View.VISIBLE);
                hbsTopbarAdapterOne = new HBSTopbarAdapterOne(menuCategory.getChildren(), getApplicationContext(), HubBoardMainView.this);
                mLayoutManager      = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                hbbTopBarRecyclerView.setLayoutManager(mLayoutManager);
                hbbTopBarRecyclerView.setItemAnimator(new DefaultItemAnimator());
                hbbTopBarRecyclerView.setNestedScrollingEnabled(true);
                hbbTopBarRecyclerView.setAdapter(hbsTopbarAdapterOne);
                hbsTopbarAdapterOne.notifyDataSetChanged();


                try {
                    HubboardsDefault.categoryHeaderName     = text;
                    HubboardsDefault.classifiedCategoryID   = menuCategory.getId();

                    changeFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            tpBarBack.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param text selected category name
     * @param child selected category child array
     */

    @Override
    public void childClickListner(String text, Child child) {


        if (child!=null) {
            if (child.getChildren()!= null&&child.getChildren().size()>0) {

                hbsTopbarAdapterTwo = new HBSTopbarAdapterTwo(child.getChildren(), getApplicationContext(), HubBoardMainView.this);
                mLayoutManager      = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                hbbTopBarRecyclerView.setLayoutManager(mLayoutManager);
                hbbTopBarRecyclerView.setItemAnimator(new DefaultItemAnimator());
                hbbTopBarRecyclerView.setNestedScrollingEnabled(true);
                hbbTopBarRecyclerView.setAdapter(hbsTopbarAdapterTwo);
                hbsTopbarAdapterTwo.notifyDataSetChanged();


                HubboardsDefault.categoryHeaderName     = text;
                HubboardsDefault.classifiedCategoryID   = child.getId();
                changeFragment();


            }else {
                try{
                    HubboardsDefault.categoryHeaderName     = text;
                    HubboardsDefault.classifiedCategoryID   = child.getId();
                    changeFragment();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param text selected category name
     * @param child_ selected category child object
     */

    @Override
    public void childChildrenClick(String text, Child_ child_) {

        if (child_!=null) {

            try {
                HubboardsDefault.categoryHeaderName = text;
                HubboardsDefault.classifiedCategoryID = child_.getId();
                changeFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try{
                HubboardsDefault.categoryHeaderName = text;
                HubboardsDefault.classifiedCategoryID = child_.getId();
                changeFragment();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Replace child fragment with slected fragment
     */
    public void changeFragment(){

        FragmentTransaction t = getFragmentManager().beginTransaction();
        HubboardsFeeds mSlidingMenuFragment = new HubboardsFeeds();
        t.replace(R.id.frame_innescreen, mSlidingMenuFragment);
        t.commit();
    }

    /**
     *  Dialog for filter search
     */
    private void showSearchDialog(){

        final String[] isLow = {""};
        final String[] categoryId = {""};
        final Dialog myDialog=new Dialog(getActivity());
        myDialog.setContentView(R.layout.hbb_search_dialog);

        final EditText keyWord=(EditText)myDialog.findViewById(R.id.searchEditText);
        final TextView sealectCategory=(TextView)myDialog.findViewById(R.id.sealectCategoryText);
        final ImageView selectCategoryImage=(ImageView)myDialog.findViewById(R.id.selectCategoryimage);
        final TextView priceAedText=(TextView)myDialog.findViewById(R.id.selectPriceText);
        final ImageView priceAedImage=(ImageView)myDialog.findViewById(R.id.selectPriceImage);
        final TextView searchTextView=(TextView)myDialog.findViewById(R.id.searchTextview);
        final LinearLayout seekBarLayout=(LinearLayout)myDialog.findViewById(R.id.seekBarDialog);

        final ImageView closeImage=(ImageView)myDialog.findViewById(R.id.searchDialogClose);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        sealectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategoryImage.performClick();
            }
        });

        if(rangeSeekBar.getParent()!=null)

            ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar);

        seekBarLayout.addView(rangeSeekBar);
        selectCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String[] array = new String[hbsMenuCategoryList.size()];

                for (int i=0;i<hbsMenuCategoryList.size();i++){
                    array[i]=hbsMenuCategoryList.get(i).getText();
                }

                builder.setTitle("Select Category")
                        .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                categoryId[0]   = hbsMenuCategoryList.get(which).getId();
                                String maxValue = HubboardsDefault.allPriceList.get(which).getMaxprice();
                                double d        = Double.parseDouble(maxValue);
                                int maxPrice    = (int) d;
                                maxValuePrice   = maxPrice;

                                sealectCategory.setText(hbsMenuCategoryList.get(which).getText());
                                HubboardsDefault.categoryHeaderName=hbsMenuCategoryList.get(which).getText();



                                rangeSeekBar.setRangeValues(minValuePrice, maxValuePrice);
                                rangeSeekBar.setSelectedMinValue(minValuePrice);
                                rangeSeekBar.setSelectedMaxValue(maxValuePrice);
                                rangeSeekBar.setSelectedMaxValue(maxValuePrice);

                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                if(rangeSeekBar.getParent()!=null)
                                    ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar);
                                seekBarLayout.addView(rangeSeekBar);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        priceAedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priceAedText.performClick();
            }
        });

        priceAedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final String[] array = {"Price - Low to High","Price - High to Low"};
                builder.setTitle("Select Category")
                        .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                priceAedText.setText(array[which]);

                                if (which==0){
                                    isLow[0] ="1";
                                }else {
                                    isLow[0] ="0";
                                }
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  searchText  = keyWord.getText().toString();

//                Search add as per the selection
                searchAds(isLow[0], categoryId[0],rangeSeekBar.getSelectedMaxValue()+"",rangeSeekBar.getSelectedMinValue()+"",searchText);
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

    /**
     *
     * @param isLow
     * @param categoryId selected category id
     * @param highPrice highest price range
     * @param lowPrice lowest price range
     * @param searchValue search keyword
     */
    public void searchAds(String isLow,String categoryId,String highPrice,String lowPrice,String searchValue) {

        String highPriceString="";
        if (highPrice       == "0"){
            highPriceString = null;
        }else {
            highPriceString = highPrice;
        }
        HubboardsAPI.searchAds(getActivity(),isLow,categoryId,highPriceString,lowPrice, searchValue,new HubboardsAPIListener() {
            @Override
            public void onSuccess(String response) {

                if (response != null && !response.isEmpty()) {
                    HubboardsDefault.searchItems.clear();
                    try {
                        JSONObject mainObj = new JSONObject(response);
                        boolean isSuccess = mainObj.has("is_success")&& mainObj.getBoolean("is_success");

                        if(isSuccess) {
                            HubboardsDefault.searchItems.clear();
                            JSONArray jsonAdsDetails    = mainObj.has("adDetails") ? mainObj.getJSONArray("adDetails") : null;
                            if (jsonAdsDetails != null) {
                                for (int i = 0; i < jsonAdsDetails.length(); i++) {
                                    JSONObject jsonObj  = jsonAdsDetails.getJSONObject(i);
                                    MyAdsItem newItem   = (new Gson()).fromJson(jsonObj.toString(), MyAdsItem.class);
                                    AdDetail newItems   = (new Gson()).fromJson(jsonObj.toString(), AdDetail.class);
                                    String videoString  = jsonObj.has("videothumb") ? jsonObj.getString("videothumb") : null;
                                    newItem.setVideothumb(videoString);
                                    HubboardsDefault.searchItems.add(newItem);
                                    HubboardsDefault.searcHubBoardList.add(newItems);
                                }
//                                Replace current fragment with SearchResultFragment
                                replaceFragment(new SearchResultFragment());
                            }
                        }else{

                            Toast.makeText(getActivity(),"There is no ads found",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure() {
            }
        });
    }

    /**
     *
     * @param fragment replace fragment with current
     */

    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.frame_fragment_containers, fragment, fragment.getClass()
                .getSimpleName());
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();


    }
}