package com.fonekey.mainpage;
import com.fonekey.R;
import com.fonekey.searchpage.CSearch;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CRecyclerAdapter extends RecyclerView.Adapter<CRecyclerAdapter.CFermViewHolder>
{
    private static boolean m_owner; // true - owner ferms, false - all list ferms
    private List<CFerm> m_lstFerm;
    private Context m_context;

    public CRecyclerAdapter(boolean owner, Context context) {
        m_owner = owner;
        m_context = context;
        m_lstFerm = new ArrayList<>();
    }

    @Override
    public CFermViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ferm, viewGroup, false);
        return new CFermViewHolder(view);
    }

    public static class CFermViewHolder extends RecyclerView.ViewHolder
    {
        private TextView m_street;
        private TextView m_house;
        private TextView m_rating;
        private TextView m_distance;
        private TextView m_number_rooms;
        private TextView m_price;

        private Button m_btnPayDel;

        public CFermViewHolder(View itemView)
        {
            super (itemView);

            m_street = itemView.findViewById(R.id.txtStreet);
            m_house = itemView.findViewById(R.id.txtHouse);
            m_rating = itemView.findViewById(R.id.txtRating);
            m_distance = itemView.findViewById(R.id.txtDistance);
            m_number_rooms = itemView.findViewById(R.id.txtNumberRooms);
            m_price = itemView.findViewById(R.id.txtPrice);

            m_btnPayDel = itemView.findViewById(R.id.btnPayDel);
            ImageButton m_btnLike = itemView.findViewById(R.id.btnLike);

            if(!m_owner) {
                m_btnPayDel.setText("Купить");
                m_btnLike.setVisibility(View.VISIBLE);
            } else {
                m_btnPayDel.setText("Удалить");
                m_btnLike.setVisibility(View.INVISIBLE);
            }
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

        viewHolder.m_btnPayDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CMainActivity.m_userId.equals("")) {
                    if (!m_owner)
                        m_context.startActivity(new Intent(m_context, CRegistrationActivity.class));
                } else {
                    String answer = "";
                    ArrayList<String> array = new ArrayList<>();
                    ArrayList<String> message = new ArrayList<>();
                    message.add("P");
                    message.add(CSearch.town);
                    message.add("#");
                    message.add(m_lstFerm.get(position).m_id);
                    message.add("33");
                    message.add("44");

                    String str = message.toString();
                    int result = CClient.SendData(str.substring(1, str.length() - 1).replace(", ", "|"));
                    if(result == 0) {
                        result = CClient.ReadData();
                        if (result == 0) {
                            answer = CClient.GetBuffer();
                            if (!answer.isEmpty()) {
                                StringTokenizer list = new StringTokenizer(answer, "#");
                                while (list.hasMoreTokens()) {
                                    StringTokenizer item = new StringTokenizer(list.nextToken(), "|");
                                    while (item.hasMoreTokens()) {
                                        str = item.nextToken();
                                        if (str.equals("P"))
                                            continue;
                                        array.add(str);
                                    }
                                    if (!array.isEmpty())
                                        array.clear();
                                        if(str == "1") {
                                            //
                                        } else {
                                            //
                                        }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() { return m_lstFerm.size(); }
}