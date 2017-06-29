package com.myhubber.myhubber.hubboards_directory.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myhubber.myhubber.ApplicationSingleton;
import com.myhubber.myhubber.R;
import com.myhubber.myhubber.hubboards.data.HubboardsAPI;
import com.myhubber.myhubber.hubboards.data.HubboardsAPIListener;
import com.myhubber.myhubber.hubboards.data.HubboardsDefault;
import com.myhubber.myhubber.hubboards.fragment.BoardsMainMenuFragment;
import com.myhubber.myhubber.hubboards.fragment.MyAdsFragment;
import com.myhubber.myhubber.hubboards.fragment.SavedAdsFragment;
import com.myhubber.myhubber.hubboards.model.MakenModelItem;
import com.myhubber.myhubber.hubboards.model.MyAdsItem;
import com.myhubber.myhubber.hubboards.model.PriceListItem;
import com.myhubber.myhubber.hubboards.rangeseekbar.RangeSeekBar;
import com.myhubber.myhubber.hubboards_directory.models.AdDetail;
import com.myhubber.myhubber.hubboards_directory.models.HubboardsConstants;
import com.myhubber.myhubber.hubmunch.models.HubMunchHomeModel;
import com.myhubber.myhubber.webservices.ClientConfig;
import com.myhubber.myhubber.webservices.ClientInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.myhubber.myhubber.hubboards_directory.models.HubboardsConstants.addDetailsStr;
import static com.myhubber.myhubber.hubboards_directory.models.HubboardsConstants.cancelStr;
import static com.myhubber.myhubber.hubboards_directory.models.HubboardsConstants.okStr;
import static com.myhubber.myhubber.hubboards_directory.models.HubboardsConstants.selectCategoryStr;

/**
 * Created by Midhun ek on 4/2/17,
 * Ideaprodigies,
 * Dubai, UAE.
 * Class name  : HubBoardHome
 * Description : Home page fragment for Hubboards and display Hubboards Categories
 */
public class HubBoardHome extends Fragment implements View.OnClickListener {


//    Global deceleration for view inside xml

    private ImageView  allCategoryImage;
    private ImageView  vehiclesPartsImage;
    private ImageView  propertyImage;
    private ImageView  jobsImage;
    private ImageView  servicesImage;
    private ImageView  classifiedsImage;

    private TextView   allCategoryTxt;
    private TextView   vehiclePartsTxt;
    private TextView   propertyTxt;
    private TextView   jobsTxt;
    private TextView   servicesTxt;
    private TextView   classifiedsTxt;

    private RelativeLayout  allCategoryRelative;
    private RelativeLayout  vehiclePartsRelative;
    private RelativeLayout  propertyRelative;
    private RelativeLayout  jobsRelative;
    private RelativeLayout  servicesRelative;
    private RelativeLayout  classifiedsRelative;

    private TextView        heavyTextView;
    private TextView        milkShakeText;

    private Typeface        heavyFont=ApplicationSingleton.avanirHeavy;
    private Typeface        milkshakeFont=ApplicationSingleton.milkShakeFont;

    private ImageView       ivDrawerMenu, ivSearch;
    private DrawerLayout    drawer;



    private RangeSeekBar<Integer> rangeSeekBar;
    private int maxValuePrice =0, minValuePrice=0;
    private List<HubMunchHomeModel> hubMunchHomeModel=new ArrayList<>();

    public static String    currentFrg="home";
    private ClientInterface clientInterface;

    public HubBoardHome() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Inflate the layout for this fragment
        return inflater.inflate(R.layout.hbb_homescreen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Initialize view from xml using Ids
        initializeView(view);

//        Call webservice to get price range list
        getPriceRange();

//        Call webservice to get saved adds of the user
        getSavedAds();

//        Call webservice to get list of model year
        getMakenModel();

//        Add side drawer fragment
        if (savedInstanceState == null) {
            FragmentTransaction t = getFragmentManager().beginTransaction();
            BoardsMainMenuFragment mSlidingMenuFragment = new BoardsMainMenuFragment();
            t.replace(R.id.menu_frame, mSlidingMenuFragment);
            t.commit();
        }

//        Call webservice to get HubBoard Home Category
        requestWebserviceForCategory();
    }

    /**
     * Initialize views
     * @param view
     */
    private void initializeView(View view) {

        allCategoryImage        = (ImageView)view.findViewById(R.id.allCategoryImage);
        vehiclesPartsImage      = (ImageView)view.findViewById(R.id.vehiclePartsImage);
        propertyImage           = (ImageView)view.findViewById(R.id.propertyImage);
        jobsImage               = (ImageView)view.findViewById(R.id.jobsImage);
        servicesImage           = (ImageView)view.findViewById(R.id.servicesImage);
        classifiedsImage        = (ImageView)view.findViewById(R.id.classifiedsImage);

        allCategoryTxt          = (TextView)view.findViewById(R.id.allCategoryTxt);
        vehiclePartsTxt         = (TextView)view.findViewById(R.id.vehiclePartsTxt);
        propertyTxt             = (TextView)view.findViewById(R.id.propertyTxt);
        jobsTxt                 = (TextView)view. findViewById(R.id.jobsTxt);
        servicesTxt             = (TextView)view. findViewById(R.id.servicesTxt);
        classifiedsTxt          = (TextView)view. findViewById(R.id.classifiedsTxt);

        allCategoryRelative     = (RelativeLayout)view.findViewById(R.id.allCategoryRelative);
        vehiclePartsRelative    = (RelativeLayout)view.findViewById(R.id.vehiclePartsRelative);
        propertyRelative        = (RelativeLayout)view.findViewById(R.id.propertyRelative);
        jobsRelative            = (RelativeLayout)view.findViewById(R.id.jobsRelative);
        servicesRelative        = (RelativeLayout)view.findViewById(R.id.servicesRelative);
        classifiedsRelative     = (RelativeLayout)view.findViewById(R.id.classifiedsRelative);

        heavyTextView           = (TextView)view.findViewById(R.id.tv_main_tiddtle);
        milkShakeText           = (TextView)view.findViewById(R.id.tv_main_title);
        ivDrawerMenu            = (ImageView)view.findViewById(R.id.hubboardDrawer);
        ivSearch                = (ImageView)view.findViewById(R.id.hubboardSearch);
        drawer                  = (DrawerLayout)view.findViewById(R.id.drawer_layout);

        ivDrawerMenu.setOnClickListener(this);
        ivSearch.setOnClickListener(this);

        allCategoryRelative.setOnClickListener(this);
        vehiclePartsRelative.setOnClickListener(this);
        propertyRelative.setOnClickListener(this);
        jobsRelative.setOnClickListener(this);
        servicesRelative.setOnClickListener(this);
        classifiedsRelative.setOnClickListener(this);

        // Initialize seekbar (Price range)
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

        heavyTextView.setTypeface(heavyFont);
        milkShakeText.setTypeface(milkshakeFont);
        heavyTextView.setText(HubboardsConstants.hubStr);
        milkShakeText.setText(HubboardsConstants.boardStr);

//        Set background of category block from drawable
        setBackgroundImage();

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
     *
     * @param view Onclick listener
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hubboardDrawer:

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);

                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;

            case R.id.hubboardSearch:

                showSearchDialog();

                break;

//            Category click listener
            case R.id.allCategoryRelative:


                if (hubMunchHomeModel!=null) {
                    if (hubMunchHomeModel.size()>0) {

                        replaceFragment(new HubBoardMainView());
                        HubboardsDefault.classifiedCategoryID=hubMunchHomeModel.get(0).getId();
                        HubboardsDefault.categoryHeaderName=hubMunchHomeModel.get(0).getText();
                    }
                }


                break;
            case R.id.vehiclePartsRelative:


                if (hubMunchHomeModel!=null) {
                    if (hubMunchHomeModel.size()>0) {

                        replaceFragment(new HubBoardMainView());
                        HubboardsDefault.classifiedCategoryID=hubMunchHomeModel.get(1).getId();
                        HubboardsDefault.categoryHeaderName=hubMunchHomeModel.get(1).getText();
                    }
                }

                break;
            case R.id.propertyRelative:


                if (hubMunchHomeModel!=null) {
                    if (hubMunchHomeModel.size()>0) {
                        replaceFragment(new HubBoardMainView());
                        HubboardsDefault.classifiedCategoryID = hubMunchHomeModel.get(2).getId();
                        HubboardsDefault.categoryHeaderName = hubMunchHomeModel.get(2).getText();
                    }
                }

                break;
            case R.id.jobsRelative:


                if (hubMunchHomeModel!=null) {
                    if (hubMunchHomeModel.size()>0) {
                        replaceFragment(new HubBoardMainView());
                        HubboardsDefault.classifiedCategoryID=hubMunchHomeModel.get(3).getId();
                        HubboardsDefault.categoryHeaderName=hubMunchHomeModel.get(3).getText();
                    }
                }

                break;
            case R.id.servicesRelative:

                if (hubMunchHomeModel.size()>0) {
                    if (hubMunchHomeModel!=null) {
                        replaceFragment(new HubBoardMainView());
                        HubboardsDefault.classifiedCategoryID=hubMunchHomeModel.get(4).getId();
                        HubboardsDefault.categoryHeaderName=hubMunchHomeModel.get(4).getText();
                    }
                }


                break;
            case R.id.classifiedsRelative:


                if (hubMunchHomeModel!=null) {
                    if (hubMunchHomeModel.size()>0) {
                        replaceFragment(new HubBoardMainView());
                        HubboardsDefault.classifiedCategoryID=hubMunchHomeModel.get(5).getId();
                        HubboardsDefault.categoryHeaderName=hubMunchHomeModel.get(5).getText();
                    }
                }
                break;

        }
    }

    /**
     * @getHomeCategory webservice method name
     * @userId logged userId
     * @lang selected language
     */
    private void requestWebserviceForCategory() {

        String userId   ="";
        try {
            userId      = ApplicationSingleton.LOGGEDIN_USER.getUserID();
        }catch (Exception e){
            e.printStackTrace();
        }

        clientInterface = ClientConfig.getRestAdapter().create(ClientInterface.class);
        clientInterface.getHubBoardHome("1",userId,"en", new Callback<List<HubMunchHomeModel>>(){
            @Override
            public void success(List<HubMunchHomeModel> achievements, Response response) {

                hubMunchHomeModel       =achievements;
                if (hubMunchHomeModel   !=null){
                    intializeViews(hubMunchHomeModel);
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }


    /**
     *
     * @param hubboardCategoryList top bar category list
     *
     */
    private void intializeViews(List<HubMunchHomeModel> hubboardCategoryList) {

        for (int i=0;i<hubboardCategoryList.size();i++){

            if (i==0) {

                Picasso.with(getActivity()).load(hubboardCategoryList.get(i).getImage())
                        .placeholder(R.drawable.hbb_allcategory)
                        .error(R.drawable.hbb_allcategory)
                        .into(allCategoryImage);
                allCategoryTxt.setText(hubboardCategoryList.get(i).getText());

            }if (i==1){

                Picasso.with(getActivity()).load(hubboardCategoryList.get(i).getImage())
                        .placeholder(R.drawable.hbb_property)
                        .error(R.drawable.hbb_property)
                        .into(vehiclesPartsImage);
                vehiclePartsTxt.setText(hubboardCategoryList.get(i).getText());


            }if (i==2){

                Picasso.with(getActivity()).load(hubboardCategoryList.get(i).getImage())
                        .placeholder(R.drawable.hbb_property)
                        .error(R.drawable.hbb_property)
                        .into(propertyImage);
                propertyTxt.setText(hubboardCategoryList.get(i).getText());

            }if (i==3){

                Picasso.with(getActivity()).load(hubboardCategoryList.get(i).getImage())
                        .placeholder(R.drawable.hbb_job)
                        .error(R.drawable.hbb_job)
                        .into(jobsImage);
                jobsTxt.setText(hubboardCategoryList.get(i).getText());

            }if (i==4){

                Picasso.with(getActivity()).load(hubboardCategoryList.get(i).getImage())
                        .placeholder(R.drawable.hbb_services)
                        .error(R.drawable.hbb_services)
                        .into(servicesImage);
                servicesTxt.setText(hubboardCategoryList.get(i).getText());

            }if (i==5){

                Picasso.with(getActivity()).load(hubboardCategoryList.get(i).getImage())
                        .placeholder(R.drawable.hbb_clasifieds)
                        .error(R.drawable.hbb_clasifieds)
                        .into(classifiedsImage);
                classifiedsTxt.setText(hubboardCategoryList.get(i).getText());

            }
        }
    }
    /**
     * @openSavedAds
     */
    public void openSavedAds(){

        replaceFragment(new SavedAdsFragment());

    }

    /**
     * @openMyAds
     */
    public void openMyAds(){

        replaceFragment(new MyAdsFragment());

    }

    /**
     * @closeDrawer
     */
    public void closeDrawer(){

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Replace current fragment
     */
    public void changeMyFragment(){
        replaceFragment(new HubBoardMainView());
    }

    /**
     *
     * @param fragment fragment name to replace
     */
    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.frame_fragment_containers, fragment, fragment.getClass()
                .getSimpleName());
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();

    }

    /**
     * Open filter dialog for item search
     */
    private void showSearchDialog(){

        final String[] isLow            = {""};
        final String[] categoryId       = {""};


        final Dialog myDialog           = new Dialog(getActivity());
        myDialog.setContentView(R.layout.hbb_search_dialog);

        final EditText keyWord          = (EditText)myDialog.findViewById(R.id.searchEditText);
        final TextView selectedCategory = (TextView)myDialog.findViewById(R.id.sealectCategoryText);
        final ImageView categoryImage   = (ImageView)myDialog.findViewById(R.id.selectCategoryimage);
        final TextView priceAedText     = (TextView)myDialog.findViewById(R.id.selectPriceText);
        final ImageView priceAedImage   = (ImageView)myDialog.findViewById(R.id.selectPriceImage);
        final TextView searchTextView   = (TextView)myDialog.findViewById(R.id.searchTextview);
        final LinearLayout seekBarLayout= (LinearLayout)myDialog.findViewById(R.id.seekBarDialog);
        final ImageView closeImage      = (ImageView)myDialog.findViewById(R.id.searchDialogClose);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        selectedCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryImage.performClick();
            }
        });


        if(rangeSeekBar.getParent()!=null)
            ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar);
        seekBarLayout.addView(rangeSeekBar);


        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String[] array = new String[hubMunchHomeModel.size()];

                for (int i=0;i<hubMunchHomeModel.size();i++){
                    array[i]=hubMunchHomeModel.get(i).getText();
                }
                builder.setTitle(HubboardsConstants.selectCategoryStr)
                        .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                categoryId[0] = hubMunchHomeModel.get(which).getId();
                                selectedCategory.setText(hubMunchHomeModel.get(which).getText());
                                HubboardsDefault.categoryHeaderName = hubMunchHomeModel.get(which).getText();

                                String maxValue = HubboardsDefault.allPriceList.get(which).getMaxprice();
                                double d        = Double.parseDouble(maxValue);
                                int maxPrice    = (int) d;
                                maxValuePrice   = maxPrice;

                                rangeSeekBar.setRangeValues(minValuePrice, maxValuePrice);
                                rangeSeekBar.setSelectedMinValue(minValuePrice);
                                rangeSeekBar.setSelectedMaxValue(maxValuePrice);
                                rangeSeekBar.setSelectedMaxValue(maxValuePrice);

                            }
                        })
                        .setPositiveButton(HubboardsConstants.okStr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                if(rangeSeekBar.getParent()!=null)
                                    ((ViewGroup)rangeSeekBar.getParent()).removeView(rangeSeekBar);
                                seekBarLayout.addView(rangeSeekBar);

                            }
                        })
                        .setNegativeButton(HubboardsConstants.cancelStr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

//        Price button click listener
        priceAedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceAedText.performClick();
            }
        });

//        Price text click listener
        priceAedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final String[] array = {"Price - Low to High","Price - High to Low"};

                builder.setTitle(selectCategoryStr)
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
                        .setPositiveButton(okStr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
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

               String searchText=keyWord.getText().toString();

//                Search add as per the selection
                searchAds(isLow[0], categoryId[0],rangeSeekBar.getSelectedMaxValue()+"",rangeSeekBar.getSelectedMinValue()+"",searchText);
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    /**
     *
     * @param isLow parameter for lowest price
     * @param categoryId selected category id
     * @param highPrice maximum price range
     * @param lowPrice minimum price range
     * @param searchKeyword search keyword
     */
    public void searchAds(String isLow,String categoryId,
                          String highPrice,String lowPrice,String searchKeyword) {

        String highPriceString  = "";
        if (highPrice.equals("0")){
            highPriceString     = "";
        }else {
            highPriceString     = highPrice;
        }
        HubboardsAPI.searchAds(getActivity(),isLow,categoryId,highPriceString,lowPrice, searchKeyword,new HubboardsAPIListener() {
            @Override
            public void onSuccess(String response) {

                if (response == null || response.isEmpty()) {
                } else {

                    HubboardsDefault.searchItems.clear();
                    try {
                        JSONObject mainObj  = new JSONObject(response);
                        boolean isSuccess   = mainObj.has(HubboardsConstants.isSuccessStr)&& mainObj.getBoolean(HubboardsConstants.isSuccessStr);

                        if(isSuccess) {

                            HubboardsDefault.searchItems.clear();

                            JSONArray jsonAdsDetails = mainObj.has(HubboardsConstants.addDetailsStr) ? mainObj.getJSONArray(HubboardsConstants.addDetailsStr) : null;
                            if (jsonAdsDetails != null) {

                                for (int i = 0; i < jsonAdsDetails.length(); i++) {

                                    JSONObject jsonObj  = jsonAdsDetails.getJSONObject(i);
                                    MyAdsItem newItem   = (new Gson()).fromJson(jsonObj.toString(), MyAdsItem.class);
                                    AdDetail newItems   = (new Gson()).fromJson(jsonObj.toString(), AdDetail.class);
                                    String videoString  = jsonObj.has(HubboardsConstants.videoThumbStr) ? jsonObj.getString(HubboardsConstants.videoThumbStr) : null;
                                    newItem.setVideothumb(videoString);

                                    HubboardsDefault.searchItems.add(newItem);
                                    HubboardsDefault.searcHubBoardList.add(newItems);
                                }

//                                Replace current fragment with SearchResult fragment
                                replaceFragment(new SearchResultFragment());

                            }
                        }else{

                            Toast.makeText(getActivity(),"There is no ads",Toast.LENGTH_SHORT).show();
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
     * Call webservice to get saved adds
     */
    public void getSavedAds() {

        HubboardsAPI.getSavedAds(getActivity(), new HubboardsAPIListener() {
            @Override
            public void onSuccess(String response) {

                if (response == null || response.isEmpty()) {
                } else {
                    try {
                        JSONObject mainObj = new JSONObject(response);

                        HubboardsDefault.savedAdsList.clear();

                        JSONArray jsonAdsDetails = mainObj.has(addDetailsStr) ? mainObj.getJSONArray(addDetailsStr) : null;
                        if (jsonAdsDetails != null) {
                            for (int i = 0; i < jsonAdsDetails.length(); i++) {

                                JSONObject jsonObj  = jsonAdsDetails.getJSONObject(i);
                                MyAdsItem newItem   = (new Gson()).fromJson(jsonObj.toString(), MyAdsItem.class);
                                HubboardsDefault.savedAdsList.add(newItem);
                            }
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
     * Call webservice to get adds makes year
     */
    public void getMakenModel() {
        HubboardsAPI.getMakenModelOfCars(getActivity(), new HubboardsAPIListener() {

            @Override
            public void onSuccess(String response) {

                // Parse makesModel json
                parseJson(response);
            }

            @Override
            public void onFailure() {

            }
        });

    }

    /**
     *
     * @param response Hubboard category response from server
     */
    public void parseJson(String response) {

        HubboardsDefault.makenModelItems.clear();
        try {

            JSONArray jObj = new JSONArray(response);
            for (int i = 0; i < jObj.length(); i++) {

                MakenModelItem categoryItem = new MakenModelItem();
                JSONObject actor = jObj.getJSONObject(i);

                String id = actor.getString(HubboardsConstants.idStr);
                String text = actor.getString(HubboardsConstants.makeNameStr);
                JSONArray array = actor.has(HubboardsConstants.modelsStr)?actor.getJSONArray(HubboardsConstants.modelsStr):null;
                categoryItem.setId(id);
                categoryItem.setMakename(text);
                if(array!=null) {
                    categoryItem.setModels(array);
                }
                HubboardsDefault.makenModelItems.add(categoryItem);
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Call webservice to get price range
     */
    public void getPriceRange() {

        String categoryID = "";
        categoryID = HubboardsDefault.classifiedCategoryID;
        HubboardsAPI.getPriceRanges(getActivity(), categoryID, new HubboardsAPIListener() {
            @Override
            public void onSuccess(String response) {


                if (response == null || response.isEmpty()) {
                } else {
                    try {
                        JSONObject mainObj = new JSONObject(response);
                        if (mainObj != null) {

                            JSONArray currencyArray = mainObj.getJSONArray(HubboardsConstants.maximumValueStr);

                            if (currencyArray != null) {
                                for (int i = 0; i < currencyArray.length(); i++) {
                                    JSONObject jsonObj = currencyArray.getJSONObject(i);
                                    PriceListItem item = new PriceListItem();
                                    item.setCatid(jsonObj.getString(HubboardsConstants.catIdStr));
                                    item.setMaxprice(jsonObj.getString(HubboardsConstants.maximumPrice));
                                    HubboardsDefault.allPriceList.add(item);
                                }
                            }
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
     * @Set Drawable image to categories
     */
    private void setBackgroundImage() {

        Picasso.with(getActivity()).load(R.drawable.hbb_allcategory).into(allCategoryImage);
        Picasso.with(getActivity()).load(R.drawable.hbb_vehicleparts).into(vehiclesPartsImage);
        Picasso.with(getActivity()).load(R.drawable.hbb_property).into(propertyImage);
        Picasso.with(getActivity()).load(R.drawable.hbb_job).into(jobsImage);
        Picasso.with(getActivity()).load(R.drawable.hbb_services).into(servicesImage);
        Picasso.with(getActivity()).load(R.drawable.hbb_clasifieds).into(classifiedsImage);

    }
}