package com.myhubber.myhubber.voucher_wallet_directory.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mutualmobile.cardstack.CardStackLayout;
import com.mutualmobile.cardstack.utils.Units;
import com.myhubber.myhubber.ApplicationSingleton;
import com.myhubber.myhubber.BaseActivity;
import com.myhubber.myhubber.R;
import com.myhubber.myhubber.voucher_wallet_directory.adapter.VoucherAdapter;
import com.myhubber.myhubber.voucher_wallet_directory.interfaces.VoucherRedeemInterface;
import com.myhubber.myhubber.voucher_wallet_directory.model.Merchant;
import com.myhubber.myhubber.voucher_wallet_directory.model.Voucherdetail;
import com.myhubber.myhubber.nearby.services.GPSTracker;
import com.myhubber.myhubber.nearbyLatest.CalculateDistance;
import com.myhubber.myhubber.tabbedActivity.HomeTabLayout;
import com.myhubber.myhubber.util.StringFactory;
import com.myhubber.myhubber.wallet.Prefs;
import com.myhubber.myhubber.wallet.models.VoucherRedeem;
import com.myhubber.myhubber.webservices.ClientConfig;
import com.myhubber.myhubber.webservices.ClientInterface;
import com.squareup.picasso.Picasso;
import com.tramsun.libs.prefcompat.Pref;

import java.util.ArrayList;
import java.util.List;

import me.philio.pinentry.PinEntryView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Midhun ek on 1/17/17,
 * Ideaprodigies,
 * Dubai, UAE.
 * Class name  : VoucherActivity
 * Description : Activity to show available vouchers and Redeem Voucher
 */

public class VoucherActivity extends BaseActivity implements VoucherRedeemInterface {

    private Voucherdetail voucherDetailObject;
    private Voucherdetail voucherDetailObjectSingle;

    private ImageView merchantBanner;
    private ImageView backImage;
    private ImageView emptyState;

    private TextView merchantName;
    private TextView merchnatLocation;
    private TextView merchantDistance;

    private String typeString   ="";
    private String privatePin   ="";
    private String branchId     ="";

    private List<Voucherdetail> voucherdetails  = new ArrayList<>();
    private ClientInterface     clientInterface;


    private CardStackLayout         mCardStackLayout;
    private VoucherAdapter walletAdapter;
    private GPSTracker              gpsTracker;

    /**
     *
     * @param myValue Call refresh
     */
    @Override
    public void callRefresh(String myValue) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResourceId    = R.layout.latest_vouchers_activity;
        super.onCreate(savedInstanceState);

//        Global deceleration for view inside xml
        merchantBanner      = (ImageView)findViewById(R.id.merchantImageBanner);
        merchantName        = (TextView)findViewById(R.id.merchantNameHuge);
        merchnatLocation    = (TextView)findViewById(R.id.merchantLocation);
        merchantDistance    = (TextView)findViewById(R.id.merchantDistance);
        backImage           = (ImageView)findViewById(R.id.backImage);
        mCardStackLayout    = (CardStackLayout) findViewById(R.id.cardStack);
        emptyState          = (ImageView)findViewById(R.id.empty_screen_img_vouvher);

//        Back image click listener
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Finish the activity
                finish();
            }
        });

//        Initialize card stack layout
        Pref.putInt(Prefs.PARALLAX_SCALE, 60);
        Pref.putInt(Prefs.CARD_GAP, 60);
        Pref.putInt(Prefs.CARD_GAP_BOTTOM, 30);

        emptyState.setVisibility(View.VISIBLE);
        mCardStackLayout.setVisibility(View.INVISIBLE);

//        Get bundle extra use by intent
        try {
            voucherDetailObject = (Voucherdetail) getIntent().getSerializableExtra("MyClass");
            branchId            = getIntent().getStringExtra("branchId");

        }catch (Exception e){
            e.printStackTrace();
        }

//        Initialize GPSTracker , Background sevice class to fetch location
        gpsTracker              = new GPSTracker(getApplicationContext());

//        Populate selected voucher details & Current user location
        if (voucherDetailObject != null){

            Picasso.with(getApplicationContext()).load(ApplicationSingleton.MERCHANT_IMAGE_URL + voucherDetailObject.getMerchantLogo())
                    .resize(650, 650)
                    .centerInside()
                    .placeholder(R.drawable.voucher_placeholder)
                    .error(R.drawable.voucher_placeholder)
                    .into(merchantBanner);

            merchantName.setText(voucherDetailObject.getMerchantTradename());
            merchnatLocation.setText(voucherDetailObject.getBranchName());

            branchId    = voucherDetailObject.getBranchId();
            typeString  = voucherDetailObject.getType();


            try {
                Double froLat;
                Double fromLong;
                if (gpsTracker   != null) {

                    froLat      = gpsTracker.getLatitude();
                    fromLong    = gpsTracker.getLongitude();
                }else {

                    froLat      = Double.parseDouble(ApplicationSingleton.userLatitude);
                    fromLong    = Double.parseDouble(ApplicationSingleton.userLongitude);
                }
                Double toLat    = Double.parseDouble(voucherDetailObject.getBranchLat());
                Double toLong   = Double.parseDouble(voucherDetailObject.getBranchLng());

                double distanceTotal = CalculateDistance.distance(froLat, fromLong, toLat, toLong);
                merchantDistance.setText(distanceTotal+" Km");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

//        Call webservice to fetch merchant vouchers
        requestVouchers();
    }

    /**
     * @getbranchvoucher webservice method name
     * @userid logged user id
     * @branchid selected merchant id
     * @type selected merchant type
     */
    private void requestVouchers() {

        String  usersId = "";
        if (ApplicationSingleton.LOGGEDIN_USER  != null && !StringFactory.isStringNullOrEmpty(ApplicationSingleton.LOGGEDIN_USER.getUserID()))
            usersId     = ApplicationSingleton.LOGGEDIN_USER.getUserID();
        clientInterface = ClientConfig.getRestAdapter().create(ClientInterface.class);
        clientInterface.getMerchantVouchers("1",usersId,branchId,typeString, new Callback<Merchant>(){
            @Override
            public void success(Merchant achievements, Response response) {

                if (Boolean.valueOf(achievements.getIsSuccess())){
                    if (achievements.getVoucherdetails()!=null){
                        emptyState.setVisibility(View.INVISIBLE);
                        mCardStackLayout.setVisibility(View.VISIBLE);

                        try {
                            voucherdetails = achievements.getVoucherdetails();

//                            Populate vouchers on cardStackLayout
                            setupAdapters();

                        }catch (Exception e){
                            emptyState.setVisibility(View.VISIBLE);
                            mCardStackLayout.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Initialize GPSTracker again if it is null
        if (gpsTracker!=null) {
            gpsTracker = new GPSTracker(getApplicationContext());
        }
    }

    /**
     * Populate voucher List on stack layout
     */
    private void setupAdapters(){

        walletAdapter   = new VoucherAdapter(this, voucherdetails,VoucherActivity.this);
        mCardStackLayout.setShowInitAnimation(Prefs.isShowInitAnimationEnabled());
        mCardStackLayout.setParallaxEnabled(Prefs.isParallaxEnabled());
        mCardStackLayout.setParallaxScale(Prefs.getParallaxScale(this));
        mCardStackLayout.setCardGap(Units.dpToPx(this, Prefs.getCardGap(this)));
        mCardStackLayout.setCardGapBottom(Units.dpToPx(this, Prefs.getCardGapBottom(this)));
        mCardStackLayout.setAdapter(walletAdapter);
    }

    /**
     * Method to show pin dialog and submit the pin
     */
    private void showPinDialog() {

        final Dialog dialog     = new Dialog(this);
        dialog.setContentView(R.layout.pinentrydialog);
        final ImageView LOGO    = (ImageView)dialog.findViewById(R.id.merchantLogo);
        final Button submit     = (Button)dialog.findViewById(R.id.submitPin);
        final PinEntryView pinEntryView    =(PinEntryView)dialog.findViewById(R.id.pin_entry_border);

        pinEntryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                privatePin      = charSequence.toString();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                privatePin      = charSequence.toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Glide.with(this).load(ApplicationSingleton.MERCHANT_IMAGE_URL+voucherDetailObject.getMerchantLogo())
                .asBitmap().centerCrop()
                .error(R.drawable.default_image)
                .into(new BitmapImageViewTarget(LOGO) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        LOGO.setImageDrawable(circularBitmapDrawable);
                    }
                });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (privatePin.length() ==4){
                    if (privatePin.equals(voucherDetailObjectSingle.getBranchPin())){

//                        Method to redeem voucher
                        redeemVoucher();
                    }else {
                        Toast.makeText(getApplicationContext(), "Wrong Pin", Toast.LENGTH_SHORT).show();
                    }
                    dialog.cancel();
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid Pin", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
    }


    /**
     * @voucheridredim webservice method name
     * @voucherid selected voucher id
     * @userid logged user id
     */
    private void redeemVoucher() {

        String  usersId = "";

        if (ApplicationSingleton.LOGGEDIN_USER != null && !StringFactory.isStringNullOrEmpty(ApplicationSingleton.LOGGEDIN_USER.getUserID()))
            usersId = ApplicationSingleton.LOGGEDIN_USER.getUserID();


        clientInterface.redeemVoucher("1", voucherDetailObjectSingle.getVoucherId(), usersId, new Callback<VoucherRedeem>() {
            @Override
            public void success(VoucherRedeem voucherRedeem, Response response) {
                if (voucherRedeem.getIsSuccess().contains("true")){

//                    Show success dialog
                    successDialog();
                }else {
                    Toast.makeText(getApplicationContext(), "Voucher cannot be redeemed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Voucher cannot be redeemed.", Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     *
     * @param voucherdetail selected voucher id from listview
     */
    @Override
    public void voucherRedeemClick(Voucherdetail voucherdetail) {

        voucherDetailObjectSingle   = voucherdetail;
        showPinDialog();

    }

    /**
     * Show voucher redeemed success dialog
     */
    private void successDialog() {

        final Dialog dialog     = new Dialog(this);
        dialog.setContentView(R.layout.achievements_unblockdialog);

        final ImageView logo    = (ImageView)dialog.findViewById(R.id.ach_dialog_image);
        final TextView submit   = (TextView) dialog.findViewById(R.id.ViewBadge);
        final TextView youHaveText    = (TextView) dialog.findViewById(R.id.youhave);
        final TextView viewYourText    = (TextView) dialog.findViewById(R.id.viewyour);
        final TextView achievementsText    = (TextView) dialog.findViewById(R.id.achievements);

        youHaveText.setText(getResources().getString(R.string.yourpointsare));
        viewYourText.setText(getResources().getString(R.string.stayput));
        achievementsText.setText(getResources().getString(R.string.youshould));
        submit.setText(getResources().getString(R.string.gotit));

        Glide.with(this).load(ApplicationSingleton.MERCHANT_IMAGE_URL+voucherDetailObjectSingle.getMerchantLogo())
                .asBitmap().centerCrop()
                .error(R.drawable.default_image)
                .into(new BitmapImageViewTarget(logo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        logo.setImageDrawable(circularBitmapDrawable);
                    }
                });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Move to home screen of the app
                Intent  moveToHome=new Intent(getApplicationContext(), HomeTabLayout.class);
                startActivity(moveToHome);
            }
        });
        dialog.show();
    }
}

