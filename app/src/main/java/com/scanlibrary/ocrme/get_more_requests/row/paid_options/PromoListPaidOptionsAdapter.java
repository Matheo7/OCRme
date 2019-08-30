package com.scanlibrary.ocrme.get_more_requests.row.paid_options;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scanlibrary.ocrme.R;
import com.scanlibrary.ocrme.billing.model.SkuRowData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.scanlibrary.ocrme.utils.LogUtil.DEV_TAG;

public class PromoListPaidOptionsAdapter
        extends RecyclerView.Adapter<PaidOptionRowViewHolder>
        implements PaidOptionRowViewHolder.OnRowClickListener {
    private static final String TAG = DEV_TAG + PromoListPaidOptionsAdapter.class.getSimpleName();

    private List<SkuRowData> dataList = new ArrayList<>();
    private UiDelegatesFactory uiDelegatesFactory;

    @Inject
    public PromoListPaidOptionsAdapter(
            UiDelegatesFactory uiDelegatesFactory) {
        this.uiDelegatesFactory = uiDelegatesFactory;
    }

    public void setDataList(List<SkuRowData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public PaidOptionRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promo_list_paid_option_row, parent, false);

        return new PaidOptionRowViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PaidOptionRowViewHolder holder, int position) {
        SkuRowData data = getItem(position);
        uiDelegatesFactory.onBindViewHolder(data, holder);
    }

    private SkuRowData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onRowClicked(int position) {
        SkuRowData data = getItem(position);
        uiDelegatesFactory.onButtonClicked(data);
    }
}
