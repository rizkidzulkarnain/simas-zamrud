package id.co.zamrud.emaszamrud.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.DataChatMessages;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*public class ChatMessagesAdapter<T extends BaseInterface> extends RecyclerView.Adapter<ChatMessagesAdapter.MyViewHolder> {
    private Context mContext;
    private List<DataChatMessages> mDataChatMessagesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        public ImageView image;

        @BindView(R.id.tvTitle)
        public TextView tvTitle;

        @BindView(R.id.tvLastUpdate)
        public TextView tvLastUpdate;

        @BindView(R.id.tvDesc)
        public TextView tvDesc;

        @BindView(R.id.rating)
        public RatingBar rating;

        @BindView(R.id.ic_download)
        public ImageView ic_download;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ChatMessagesAdapter(Context mContext, List<DataChatMessages> dataChatMessagesList) {
        this.mContext = mContext;
        this.mDataChatMessagesList = dataChatMessagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DataChatMessages dataChatMessages = mDataChatMessagesList.get(position);

        Glide.with(mContext).load(dataChatMessages.getImageURL()).into(holder.image);
        holder.tvTitle.setText(dataChatMessages.getTitle());
        holder.tvLastUpdate.setText(dataChatMessages.getLastUpdate());
        holder.tvDesc.setText(dataChatMessages.getDesc());
        holder.rating.setRating(dataChatMessages.getRating());
        holder.image.setOnClickListener(new View.OnClickListener( ) {
            public void onClick(View v) {
                downloadData(holder);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return mDataChatMessagesList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mDataChatMessagesList.size();
    }

    public void downloadData(MyViewHolder holder){
        try {
            holder.image.setImageResource(R.drawable.user);
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.downloadData();
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    if (response.body() != null) {
                        holder.image.setVisibility(View.INVISIBLE);
                    }else{
                        holder.image.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<PostExecuteDatabase> call, Throwable t) {
                    holder.image.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/

/*interface BaseInterface{
    long getId();
}*/
