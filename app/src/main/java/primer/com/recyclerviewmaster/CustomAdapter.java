package primer.com.recyclerviewmaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CustomAdapter  extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private OnCardClickListener onCardClickListener;
    private  OnTextClickListener onTextClickListener;
    private Context context;
    private List<Pojo1> pojo1List;

    public CustomAdapter(Context context, List<Pojo1> pojo1List) {
        this.context = context;
        this.pojo1List = pojo1List;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item1,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,  int position) {

        final Pojo1 pojo1 = pojo1List.get(position);
        holder.name.setText(pojo1.getTitle());
        holder.price.setText(pojo1.getPrice());
        holder.thumbnail.setImageUrl(pojo1.getImage(),pojo1.getImageLoader());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClickListener.OnCardClicked(view,holder.getAdapterPosition());
            }
        });
        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTextClickListener.OnCardClicked(view,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pojo1List.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,price;
        public NetworkImageView thumbnail;
        public CardView cardView;
        public MyViewHolder(View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }



    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;

    }
    public void setOnTextClickListener(OnTextClickListener onTextClickListener){
        this.onTextClickListener = onTextClickListener;
    }
    public interface OnCardClickListener {
        void OnCardClicked(View view, int position);

    }

    public interface OnTextClickListener{
        void OnCardClicked(View view,int position);
    }
}
