package com.adolsai.asrabbit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.model.Post;
import com.ht.baselib.base.BaseAdapter;
import com.ht.baselib.helper.SparseViewHelper;

import java.util.List;

/**
 * <p>PartitionAdapter类 </p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/3 18:06)<br/>
 */
public class PostAdapter extends BaseAdapter<Post> {

    public PostAdapter(Context context, List<Post> lists) {
        super(context, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_post, parent, false);
        }

        TextView postPlate = SparseViewHelper.getView(convertView, R.id.post_plate);
        TextView tvPostReadTime = SparseViewHelper.getView(convertView, R.id.tv_post_read_time);
        TextView tvPostReadPosition = SparseViewHelper.getView(convertView, R.id.tv_post_read_position);
        TextView tvPostType = SparseViewHelper.getView(convertView, R.id.tv_post_type);
        TextView tvPostContent = SparseViewHelper.getView(convertView, R.id.tv_post_content);


        Post post = getItem(position);
        if (post != null) {
            postPlate.setText(post.getPlate());
            tvPostReadTime.setText(post.getReadTime());
            tvPostReadPosition.setText(post.getReadPosition());
            tvPostType.setText(post.getInvitationType());
            tvPostContent.setText(post.getContent());
        }
        return convertView;
    }
}
