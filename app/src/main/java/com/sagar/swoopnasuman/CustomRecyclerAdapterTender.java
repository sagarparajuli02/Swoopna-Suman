package com.sagar.swoopnasuman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CustomRecyclerAdapterTender extends RecyclerView.Adapter<CustomRecyclerAdapterTender.ViewHolder> {

    private Context context;
    private List<TenderUtils> tenderUtils;

    public CustomRecyclerAdapterTender(Context context, List personUtils) {
        this.context = context;
        this.tenderUtils = personUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item_tender, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(tenderUtils.get(position));

        TenderUtils pu = tenderUtils.get(position);

        holder.pName.setText(pu.getPersonFirstName());

    }

    @Override
    public int getItemCount() {
        return tenderUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pName;


        public ViewHolder(View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.pNametxt);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    TenderUtils cpu = (TenderUtils) view.getTag();

                    //    Toast.makeText(view.getContext(), cpu.getPersonFirstName()+" "+cpu.getPersonLastName()+" is "+ cpu.getJobProfile(), Toast.LENGTH_SHORT).show();
                    //   new DownloadTask(CustomRecyclerAdapterTender.this,cpu.getJobProfile());


                    Intent intent = new Intent(context,LyricsDetails.class);
                    intent.putExtra("title",cpu.getPersonFirstName());
                    intent.putExtra("description",cpu.getJobProfile());

                    context.startActivity(intent);


                    //     Intent intent = new Intent(Intent.ACTION_VIEW);

                    //  intent.setDataAndType(Uri.parse( cpu.getJobProfile()), "text/html");

                    //  intent.setDataAndType(Uri.parse( " https://drive.google.com/open?id=15YGVE2oZOrXyOojWMJgavARGwSYx5saz"), "text/html");
                    //   context.startActivity(intent);



                }
            });

        }
    }

}