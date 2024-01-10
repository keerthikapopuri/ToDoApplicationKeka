package keerthika.com.mx.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import keerthika.com.mx.todoapp.R;
import keerthika.com.mx.todoapp.model.Nota;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {


    private List<Nota> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;
    private Context context;


    public NoteAdapter(Context context, List<Nota> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Nota nota = mData.get(position);

        holder.name.setText(nota.getName());
        holder.description.setText(nota.getDescription());
        holder.statusText.setText(nota.getStatus());
        //holder.priorityText.setText(nota.getPriority());
        holder.categoryText.setText(nota.getCategory());
        holder.dateText.setText(nota.getDate());

        if(nota.getPriority().toLowerCase().equals("low")){
            holder.priorityText.setText("LOW");
            holder.priorityText.setBackgroundColor(ContextCompat.getColor(context, R.color.md_green_900));
            holder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.md_green_900));
        }

        if(nota.getPriority().toLowerCase().equals("medium")){
            holder.priorityText.setText("MEDIUM");
            holder.priorityText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
        holder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorOrange));

        }
        if(nota.getPriority().toLowerCase().equals("high")){
            holder.priorityText.setText("HIGH");
            holder.priorityText.setBackgroundColor(ContextCompat.getColor(context, R.color.md_red_900));
            holder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.md_red_900));

        }



        if (nota.getStatus().equals("Progress")){
            holder.statusText.setText("PROGRESS");
            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
            holder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
            holder.iconStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.orange_circle));
        }
        if (nota.getStatus().equals("New")){
            holder.statusText.setText("NEW");
            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
            holder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray));
            holder.iconStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_circle));
        }
        if (nota.getStatus().equals("Completed")){
            holder.statusText.setText("COMPLETED");
            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.iconStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.gree_circle));
        }




    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        @BindView(R.id.statusView)
        View statusView;

        @BindView(R.id.iconStatus)
        View iconStatus;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.statusText)
        TextView statusText;

        @BindView(R.id.priorityText)
        TextView priorityText;

        @BindView(R.id.categoryText)
                TextView categoryText;
        @BindView(R.id.dateText)
                TextView dateText;



        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) mLongClickListener.onItemLongClick(v, getAdapterPosition());
            return false;
        }
    }

    public Nota getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }



}
