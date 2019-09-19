package com.ashomok.ocrme.my_docs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ashomok.ocrme.R;
import com.ashomok.ocrme.ocr.ocr_task.OcrResult;
import com.ashomok.ocrme.utils.GlideApp;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

/**
 * Created by iuliia on 12/26/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = DEV_TAG + RecyclerViewAdapter.class.getSimpleName();
    private final MyDocsActivity.RecyclerViewCallback callback;
    private List<Object> mDataList;
    private List<OcrResult> multiSelectDataList;
    private static final int DOC = 0;
    private static final int NATIVE_AD = 1;
    private Context context;


    RecyclerViewAdapter(List<Object> mDataList,
                        List<OcrResult> multiSelectDataList,
                        MyDocsActivity.RecyclerViewCallback callback) {
        this.mDataList = mDataList;
        this.multiSelectDataList = multiSelectDataList;
        this.callback = callback;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == DOC) {
            View docItem = inflater.inflate(R.layout.my_doc_view, parent, false);
            return new DocViewHolder(docItem);
        } else {
            View nativeAdItem =
                    inflater.inflate(R.layout.mydocs_native_ad, parent, false);
            return new NativeAdViewHolder(nativeAdItem);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        int itemType = getItemViewType(position);
        if (itemType == DOC) {
            DocViewHolder holder = (DocViewHolder) holder1;
            OcrResult item = (OcrResult) mDataList.get(position);

            //check if select mode and update design
            if (multiSelectDataList.size() > 0) {
                //select mode

                holder.checkbox.setVisibility(View.VISIBLE);
                holder.darkHint.setVisibility(View.VISIBLE);
                if (multiSelectDataList.contains(item)) {
                    //current card selected - check checkbox
                    holder.checkbox.setChecked(true);
                } else {
                    holder.checkbox.setChecked(false);
                }
            } else {
                //not select mode
                holder.checkbox.setVisibility(View.GONE);
                holder.darkHint.setVisibility(View.GONE);
            }

            //timestamp
            holder.timeStamp.setText(item.getTimeStamp());

            //source image
            try {
                // Create a reference to a file from a Google Cloud Storage URI
                StorageReference gsReference =
                        FirebaseStorage.getInstance().getReferenceFromUrl(item.getSourceImageUrl());
                // Load the image using Glide
                GlideApp.with(holder.cardView.getContext())
                        .load(gsReference)
                        .centerCrop()
                        .into(holder.sourceImage);
            } catch (Exception e) {
                //ignore
                e.printStackTrace();
            }

            //card menu
            holder.menuBtn.setOnClickListener(view -> showPopupMenu(holder.menuBtn, position));

            //cardview init click callback
            holder.cardView.setOnClickListener(view -> callback.onItemClick(position));
            holder.cardView.setOnLongClickListener(view -> {
                callback.onItemLongClick(position);
                return true;
            });
        } else if (itemType == NATIVE_AD) {
            UnifiedNativeAd item = (UnifiedNativeAd) mDataList.get(position);
            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) holder1;
            populateUnifiedNativeAdView(item, nativeAdViewHolder);
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, NativeAdViewHolder adView) {

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.adView.setNativeAd(nativeAd);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mDataList.get(position);
        if (item instanceof OcrResult) {
            return DOC;
        } else if (item instanceof UnifiedNativeAd) {
            return NATIVE_AD;
        } else {
            return -1;
        }
    }


    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.doc_cardview, popup.getMenu());
        popup.setOnMenuItemClickListener(new DocMenuItemClickListener(position));
        popup.show();
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class DocViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView sourceImage;
        TextView timeStamp;
        ImageButton menuBtn;
        CheckBox checkbox;
        ImageView darkHint;

        DocViewHolder(View v) {
            super(v);
            sourceImage = v.findViewById(R.id.sourceImage);
            timeStamp = v.findViewById(R.id.timeStamp);
            cardView = v.findViewById(R.id.cardView);
            menuBtn = v.findViewById(R.id.card_menu_btn);
            checkbox = v.findViewById(R.id.checkBox);
            darkHint = v.findViewById(R.id.hint);
        }
    }

    private class DocMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int position;

        DocMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    callback.onItemDelete(position);
                    return true;
                case R.id.share_text:
                    callback.onItemShareText(position);
                    return true;
                case R.id.share_pdf:
                    callback.onItemSharePdf(position);
                    return true;
                default:
            }
            return false;
        }
    }

    private static class NativeAdViewHolder extends RecyclerView.ViewHolder {
        UnifiedNativeAdView adView;

        NativeAdViewHolder(View view) {
            super(view);
            adView = view.findViewById(R.id.native_ad);
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        }
    }
}
