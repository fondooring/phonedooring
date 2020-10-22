package com.fonekey.mainpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fonekey.R;

import java.util.ArrayList;
import java.util.List;

public class CRecyclerAdapter extends RecyclerView.Adapter<CRecyclerAdapter.CFermViewHolder>
{
    CFermViewHolder m_fermViewHolder;

    private List<CFerm> m_lstFerm;
    Context m_context;

    public CRecyclerAdapter(List<CFerm> lstFerm, Context context) {
        this.m_context = context;
        this.m_lstFerm = lstFerm;
    }

    @Override
    public CFermViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ferm, viewGroup, false);
        return m_fermViewHolder = new CFermViewHolder(view);
    }

    public static class CFermViewHolder extends RecyclerView.ViewHolder
    {
        private TextView m_street;
        private TextView m_house;
        private TextView m_rating;
        private TextView m_distance;
        private TextView m_number_rooms;
        private TextView m_price;

        private Button m_btnPay;

        public CFermViewHolder(View itemView)
        {
            super (itemView);

            m_street = (TextView) itemView.findViewById(R.id.txtStreet);
            m_house = (TextView) itemView.findViewById(R.id.txtHouse);
            m_rating = (TextView) itemView.findViewById(R.id.txtRating);
            m_distance = (TextView) itemView.findViewById(R.id.txtDistance);
            m_number_rooms = (TextView) itemView.findViewById(R.id.txtNumberRooms);
            m_price = (TextView) itemView.findViewById(R.id.txtPrice);
            m_btnPay = (Button) itemView.findViewById(R.id.btnPay);
        }
    }

    public void onItemDel(int position)
    {
        if(position != -1 && position < m_lstFerm.size()) {
            m_lstFerm.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void onClear()
    {
        int listSize = m_lstFerm.size();
        for(int i = 0; i < listSize; i++)
            onItemDel(0);
    }

    public void onItemAdd(ArrayList<String> data)
    {
        int position = 0;
        CFerm ferm = new CFerm();
        ferm.m_id = data.get(position++);
        ferm.m_town = "Москва";
        ferm.m_street = data.get(position++);
        ferm.m_house = data.get(position++);
        ferm.m_rating = data.get(position++);
        ferm.m_distance = data.get(position++);
        ferm.m_number_rooms = data.get(position++);
        ferm.m_price = data.get(position++);
        m_lstFerm.add(0, ferm);
        notifyItemInserted(0);

        //CFerm ferm = new CFerm(ferms);
        //ferm.m_cost = street;
        // notifyItemRangeInserted(m_lstFerm.size() + 1, m_lstFerm.size());
    }

    @Override
    public void onBindViewHolder(CFermViewHolder viewHolder, final int position) {
        viewHolder.m_street.setText(m_lstFerm.get(position).m_street);
        viewHolder.m_house.setText(m_lstFerm.get(position).m_house);
        viewHolder.m_rating.setText(m_lstFerm.get(position).m_rating);
        viewHolder.m_distance.setText(m_lstFerm.get(position).m_distance);
        viewHolder.m_number_rooms.setText(m_lstFerm.get(position).m_number_rooms);
        viewHolder.m_price.setText(m_lstFerm.get(position).m_price);

        viewHolder.m_btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = position;


                //Fragment fragment = (Fragment) new CUserCardFragment(); // Фрагмент, которым собираетесь заменить первый фрагмент

                /*FragmentTransaction transaction = ((Activity)m_context).getFragmentManager().beginTransaction(); // Или getSupportFragmentManager(), если используете support.v4
                transaction.replace(R.id.layoutUserCard, new CUserCardFragment()); // Заменяете вторым фрагментом. Т.е. вместо метода `add()`, используете метод `replace()`
                transaction.addToBackStack(null); // Добавляете в backstack, чтобы можно было вернутся обратно
                transaction.commit(); // Коммитете*/

                m_context.startActivity(new Intent(m_context, CRegistrationActivity.class));
                //m_lstFerm.get(position).showBay();
            }
        });

        /*viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context, ActivityToStart.class);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() { return m_lstFerm.size(); }
}