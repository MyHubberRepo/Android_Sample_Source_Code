package com.myhubber.myhubber.voucher_wallet_directory.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mutualmobile.cardstack.CardStackAdapter;
import com.myhubber.myhubber.ApplicationSingleton;
import com.myhubber.myhubber.R;
import com.myhubber.myhubber.voucher_wallet_directory.interfaces.VoucherRedeemInterface;
import com.myhubber.myhubber.voucher_wallet_directory.model.Voucherdetail;
import com.myhubber.myhubber.voucher_wallet_directory.views.VoucherActivity;
import com.myhubber.myhubber.wallet.Prefs;
import com.myhubber.myhubber.wallet.interfaces.OnRestartRequest;
import com.tramsun.libs.prefcompat.Pref;

import java.util.List;

/**
 * Created by Midhun ek on 1/17/17,
 * Ideaprodigies,
 * Dubai, UAE.
 * Class name  : VoucherAdapter
 * Description : Adapter class to list available voucher on stackLayout
 */

public class VoucherAdapter extends CardStackAdapter implements CompoundButton.OnCheckedChangeListener {

    private static int[]            bgColorIds;
    private final LayoutInflater    mInflater;
    private final Context           mContext;
    private OnRestartRequest        mCallback;
    private Runnable                updateSettingsView;
    private List<Voucherdetail>     voucherDetailsList;
    private VoucherRedeemInterface  voucherRedeemInterface;

    /**
     *
     * @param activity
     * @param voucherDetails
     * @param voucherRedeemInterface
     */
    public VoucherAdapter(VoucherActivity activity, List<Voucherdetail>voucherDetails, VoucherRedeemInterface voucherRedeemInterface) {
        super(activity);
        this.mContext               = activity;
        this.mInflater              = LayoutInflater.from(activity);
        this.voucherRedeemInterface = voucherRedeemInterface;
        this.voucherDetailsList     = voucherDetails;

//        Background color array
        bgColorIds = new int[]{
                R.color.card1_bg,
                R.color.card2_bg,
                R.color.card3_bg,
                R.color.card4_bg,
                R.color.card5_bg,
                R.color.card6_bg,
                R.color.card7_bg,
                R.color.card1_bg,
                R.color.card2_bg,
                R.color.card3_bg,
                R.color.card4_bg,
                R.color.card5_bg,
                R.color.card6_bg,
                R.color.card7_bg,
                R.color.card8_bg,
                R.color.card9_bg,
                R.color.card10_bg
        };

    }
    /**
     *
     * @return voucherDetailsList
     */
    @Override
    public int getCount() {
        return voucherDetailsList.size();
    }
    /**
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.parallax_enabled:
                Pref.putBoolean(Prefs.PARALLAX_ENABLED, isChecked);
                break;
            case R.id.reverse_click_animation:
                Prefs.setReverseClickAnimationEnabled(isChecked);
                break;
            case R.id.show_init_animation:
                Pref.putBoolean(Prefs.SHOW_INIT_ANIMATION, isChecked);
                break;
        }
        updateSettingsView.run();
    }

    /**
     *
     * @param position
     * @param container
     * @return
     */
    @Override
    public View createView(final int position, ViewGroup container) {

        final Voucherdetail voucherDetails  = voucherDetailsList.get(position);
        final CardView root                 = (CardView) mInflater.inflate(R.layout.card, container, false);
        root.setCardBackgroundColor(ContextCompat.getColor(mContext, bgColorIds[position]));

        final TextView cardTitle        = (TextView) root.findViewById(R.id.card_title);
        final TextView orgName          = (TextView) root.findViewById(R.id.company_name);
        final TextView cardDetail       = (TextView) root.findViewById(R.id.card_detail);
        final TextView timeLeft         = (TextView) root.findViewById(R.id.time_left);
        final ImageView nextImage       = (ImageView)root.findViewById(R.id.nextImage);
        final ImageView merchantLogo    = (ImageView)root.findViewById(R.id.imageView);

        cardTitle.setText(voucherDetails.getVoucherName());
        orgName.setText(voucherDetails.getMerchantTradename());
        cardDetail.setText(Html.fromHtml(voucherDetails.getVoucherDescription()));


        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voucherRedeemInterface.voucherRedeemClick(voucherDetails);
            }
        });

        Glide.with(mContext).load(ApplicationSingleton.MERCHANT_IMAGE_URL+voucherDetails.getMerchantLogo())
                .asBitmap().centerCrop()
                .error(R.drawable.default_image)
                .into(new BitmapImageViewTarget(merchantLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        merchantLogo.setImageDrawable(circularBitmapDrawable);
                    }
                });
        timeLeft.setText(voucherDetails.getDaysleft());
        return root;
    }
    /**
     *
     * @param view
     * @param currentCardPosition
     * @param selectedCardPosition
     * @return Animator
     */
    @Override
    protected Animator getAnimatorForView(View view, int currentCardPosition, int selectedCardPosition) {
        if (Prefs.isReverseClickAnimationEnabled()) {

            int offsetTop = getScrollOffset();
            if (currentCardPosition > selectedCardPosition) {
                return ObjectAnimator.ofFloat(view, View.Y, view.getY(), offsetTop + getCardFinalY(currentCardPosition));
            } else {
                return ObjectAnimator.ofFloat(view, View.Y, view.getY(), offsetTop + getCardOriginalY(0) + (currentCardPosition * getCardGapBottom()));
            }
        } else {
            return super.getAnimatorForView(view, currentCardPosition, selectedCardPosition);
        }
    }
}

