package com.ashomok.ocrme.get_more_requests.row.free_options;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashomok.ocrme.R;

import java.util.List;

import javax.inject.Inject;

import com.ashomok.ocrme.utils.LogHelper;

/**
 * Created by iuliia on 3/2/18.
 */

public class PromoListFreeOptionsAdapter extends RecyclerView.Adapter<FreeOptionRowViewHolder>
        implements FreeOptionRowViewHolder.OnButtonClickListener {
    private static final String TAG = LogHelper.makeLogTag(PromoListFreeOptionsAdapter.class);
    private final List<PromoRowFreeOptionData> dataList;

    @Inject
    public UiDelegatesFactory uiDelegatesFactory;

    @Inject
    public PromoListFreeOptionsAdapter(List<PromoRowFreeOptionData> rowData) {
        this.dataList = rowData;
    }

    @NonNull
    @Override
    public FreeOptionRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promo_list_free_option_row, parent, false);

        return new FreeOptionRowViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeOptionRowViewHolder holder, int position) {
        PromoRowFreeOptionData data = getItem(position);
        uiDelegatesFactory.onBindViewHolder(data, holder);
    }

    private PromoRowFreeOptionData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onButtonClicked(int position) {
        PromoRowFreeOptionData data = getItem(position);
        uiDelegatesFactory.onButtonClicked(data);
    }
}
