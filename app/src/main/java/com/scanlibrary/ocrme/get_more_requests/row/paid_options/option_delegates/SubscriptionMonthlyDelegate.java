package com.scanlibrary.ocrme.get_more_requests.row.paid_options.option_delegates;

import android.content.Context;

import com.scanlibrary.ocrme.R;
import com.scanlibrary.ocrme.billing.BillingProviderImpl;
import com.scanlibrary.ocrme.billing.model.SkuRowData;
import com.scanlibrary.ocrme.get_more_requests.row.paid_options.PaidOptionRowViewHolder;
import com.scanlibrary.ocrme.get_more_requests.row.paid_options.UiPaidOptionManagingDelegate;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.scanlibrary.ocrme.billing.BillingProviderImpl.PREMIUM_YEARLY_SKU_ID;
import static com.scanlibrary.ocrme.utils.LogUtil.DEV_TAG;

public class SubscriptionMonthlyDelegate extends UiPaidOptionManagingDelegate {
    public static final String TAG = DEV_TAG + SubscriptionMonthlyDelegate.class.getSimpleName();

    @Inject
    public SubscriptionMonthlyDelegate(BillingProviderImpl billingProvider, Context context) {
        super(billingProvider, context);
    }

    @Override
    public void onRowClicked(SkuRowData data) {
        if (data != null) {
            if (getBillingProvider().isPremiumYearlySubscribed()) {
                // If we already subscribed to yearly premium, launch replace flow
                ArrayList<String> currentSubscriptionSku = new ArrayList<>();
                currentSubscriptionSku.add(PREMIUM_YEARLY_SKU_ID);
                getBillingProvider().getBillingManager().initiatePurchaseFlow(data.getSku(),
                        currentSubscriptionSku, data.getSkuType());
            } else {
                super.onRowClicked(data);
            }
        }
    }

    @Override
    public void onBindViewHolder(SkuRowData data, PaidOptionRowViewHolder holder) {
        super.onBindViewHolder(data, holder);

        holder.getTitle().setText(getContext().getResources().getString(R.string.one_month_premium));
        holder.getSubtitleTop().setText(getContext().getResources().getString(R.string.unlimited_requests));
    }
}
