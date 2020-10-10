package com.fonekey.mainpage;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fonekey.R;

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
        private TextView m_cost;
        private Button m_btnPay;

        public CFermViewHolder(View itemView)
        {
            super (itemView);

            m_cost = (TextView) itemView.findViewById(R.id.txtCost);
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

    public void onItemAdd(String street)
    {
        CFerm ferm = new CFerm();
        ferm.m_cost = street;
        m_lstFerm.add(0, ferm);
        notifyItemInserted(0);
        // notifyItemRangeInserted(m_lstFerm.size() + 1, m_lstFerm.size());
    }

    @Override
    public void onBindViewHolder(CFermViewHolder viewHolder, final int position) {
        viewHolder.m_cost.setText(m_lstFerm.get(position).m_cost);

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