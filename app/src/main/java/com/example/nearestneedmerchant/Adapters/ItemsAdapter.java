package com.example.nearestneedmerchant.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.nearestneedmerchant.Model.ItemModel;
import com.example.nearestneedmerchant.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ItemsAdapter extends FirebaseRecyclerAdapter<ItemModel, com.example.nearestneedmerchant.Adapters.ItemsAdapter.myviewholder> {


    Context context;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    String userid;
    int i=0;
    String itemkey;

    private DatabaseReference databaseRef,databaseRef4;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseRef5;


    public ItemsAdapter(@NonNull FirebaseRecyclerOptions<ItemModel> options, Context context,String userid) {
        super(options);
        this.context = context;
        this.userid=userid;

    }




    @NonNull
    @Override
    public com.example.nearestneedmerchant.Adapters.ItemsAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdisplaylayout, parent, false);

        return new com.example.nearestneedmerchant.Adapters.ItemsAdapter.myviewholder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ItemModel model) {
        holder.name.setText(" "+model.getItemname());
        holder.price.setText("₹"+model.getSellingprice()+"/-");

        itemkey= getRef(position).getKey();
        Glide.with(context).load(model.getItemimage()).into(holder.image);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Fav").child(itemkey).removeValue();

/*
                Intent intent = new Intent(context, SingleItem.class);
                intent.putExtra("name",model.getItemname());
                intent.putExtra("image",model.getItemimage());
                intent.putExtra("desc",model.getItemdesc());
                intent.putExtra("price",model.getItemprice());
                intent.putExtra("sellingprice",model.getSellingprice());
                intent.putExtra("model",model.getModel());
                intent.putExtra("type",model.getItemtype());
                intent.putExtra("key",itemkey);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/


            }
        });
        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(userid).child(itemkey).removeValue();
                Toast.makeText(context,model.getItemname()+"  is Deleted From Your Favorites",Toast.LENGTH_SHORT).show();

            }
        });


    }

    class myviewholder extends RecyclerView.ViewHolder {

        TextView name, desc,price;

        ImageView addcart,addfav;

        ImageView image;
        LinearLayout layout;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.Specname);
            layout=itemView.findViewById(R.id.layoutclick);
            price = (TextView) itemView.findViewById(R.id.Specprice);
            image=  itemView.findViewById(R.id.imageview);
            addfav=(ImageView) itemView.findViewById(R.id.addfav);
            addcart=(ImageView) itemView.findViewById(R.id.addcartbtton);



        }
    }



}