package pa1pal.picscramble.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pa1pal.picscramble.R;
import pa1pal.picscramble.data.model.Item;

/**
 * Created by pa1pal on 10/4/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    Context context;
    ImageLoad imageLoad;
    private int gameStatus;
    private List<Item> itemList = new ArrayList<>();

    public MainAdapter() {
        this.gameStatus = 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        switch (gameStatus) {
            case 1:
            case 2:
                Picasso.with(context)
                        .load(itemList.get(position).getMedia().getM())
                        .fit()
                        .into(holder.randomImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (context instanceof ImageLoad) {
                                    if (position == 8)
                                        ((ImageLoad) context).isLoaded();
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
                break;
            case 3:
                if (itemList.get(position).isFound()) {
                    Picasso.with(context).load(itemList.get(position).getMedia().getM())
                            .fit()
                            .into(holder.randomImage);
                } else {
                    Picasso.with(context).load(R.drawable.ic_image)
                            .placeholder(R.drawable.ic_image)
                            .fit()
                            .into(holder.randomImage);
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void gameStatus(int status) {
        this.gameStatus = status;
        notifyDataSetChanged();
    }

    public void itemFound(int position) {
        this.itemList.get(position).setFound(true);
        notifyDataSetChanged();
    }

    public void setImages(List<Item> p) {
        itemList = p;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.random_image)
        ImageView randomImage;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
