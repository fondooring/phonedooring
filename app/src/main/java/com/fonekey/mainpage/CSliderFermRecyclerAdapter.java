package com.fonekey.mainpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fonekey.R;

import java.util.List;

public class CSliderFermRecyclerAdapter extends RecyclerView.Adapter<CSliderFermRecyclerAdapter.CFermViewHolder>
{
    private List<CFermSlider> m_lstFerm;
    CFermViewHolder m_fermViewHolder;
    SwitchCompat m_swchFermEnable;

    public CSliderFermRecyclerAdapter(List<CFermSlider> lstFerm) {
        m_lstFerm = lstFerm;
    }

    @Override
    public CFermViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ferm_slider, viewGroup, false);
        m_swchFermEnable = (SwitchCompat) view.findViewById(R.id.switchEnableFerm);

        return m_fermViewHolder = new CFermViewHolder(view);
    }

    public static class CFermViewHolder extends RecyclerView.ViewHolder
    {
        SwitchCompat m_swchFermEnable;

        public CFermViewHolder(View itemView) {
            super (itemView);

            m_swchFermEnable = (SwitchCompat) itemView.findViewById(R.id.switchEnableFerm);
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
        CFermSlider fermSlider = new CFermSlider();
        fermSlider.m_street = street;
        fermSlider.m_enable = false;
        m_lstFerm.add(0, fermSlider);
        notifyItemInserted(0);
        // notifyItemRangeInserted(m_lstFerm.size() + 1, m_lstFerm.size());
    }

    @Override
    public void onBindViewHolder(CFermViewHolder viewHolder, final int position) {
        viewHolder.m_swchFermEnable.setText(m_lstFerm.get(position).m_street);
        viewHolder.m_swchFermEnable.setChecked(m_lstFerm.get(position).m_enable);

        m_swchFermEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = position;
            }
        });
    }

    @Override
    public int getItemCount() { return m_lstFerm.size(); }
}