package com.adolsai.asrabbit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.model.Partition;
import com.ht.baselib.base.BaseAdapter;
import com.ht.baselib.helper.SparseViewHelper;

import java.util.List;

/**
 * <p>PartitionAdapter类 </p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/3 18:06)<br/>
 */
public class PartitionAdapter extends BaseAdapter<Partition.BoardListBean> {

    public PartitionAdapter(Context context, List<Partition.BoardListBean> lists) {
        super(context, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_partition, parent, false);
        }

        TextView tvPartitionNumber = SparseViewHelper.getView(convertView, R.id.tv_partition_number);
        TextView tvPartitionTitle = SparseViewHelper.getView(convertView, R.id.tv_partition_title);
        View cutLine = SparseViewHelper.getView(convertView, R.id.cut_line);
        View cutLine1 = SparseViewHelper.getView(convertView, R.id.cut_line1);
        Partition.BoardListBean curr = getItem(position);
        if (curr != null) {
            tvPartitionNumber.setText(curr.getId());
            tvPartitionTitle.setText(curr.getName());
            cutLine.setVisibility(View.VISIBLE);
            cutLine1.setVisibility(View.GONE);
        }
        if (position == getCount() - 1) {
            cutLine.setVisibility(View.GONE);
            cutLine1.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
