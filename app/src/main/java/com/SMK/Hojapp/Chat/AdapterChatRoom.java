package com.SMK.Hojapp.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.SMK.Hojapp.R;

import java.util.List;

/**
 * Created by KPlo on 2018. 10. 28..
 */

public class AdapterChatRoom extends RecyclerView.Adapter<AdapterChatRoom.MyViewHolder> {
    private List<ChatRoomData> mDataset;
    private String myNickName;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TextView_members;
        public TextView TextView_last_msg;
        public TextView TextView_update_time;
        public View rootView;
        public MyViewHolder(View v) {
            super(v);
            TextView_members = v.findViewById(R.id.TextView_members);
            TextView_last_msg = v.findViewById(R.id.TextView_last_msg);
            TextView_update_time = v.findViewById(R.id.TextView_update_time);
            rootView = v;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterChatRoom(List<ChatRoomData> myDataset, Context context, String myNickName) {
        //{"1","2"}
        mDataset = myDataset;
        this.myNickName = myNickName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterChatRoom.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat_room, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatRoomData chatRoom = mDataset.get(position);

        holder.TextView_members.setText(chatRoom.getMembers());
        holder.TextView_last_msg.setText(chatRoom.getLastMsg());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        //삼항 연산자
        return mDataset == null ? 0 :  mDataset.size();
    }

    public ChatRoomData getChatRoom(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatRoomData chatRoom) {
        mDataset.add(chatRoom);
        notifyItemInserted(mDataset.size()-1); //갱신
    }

}