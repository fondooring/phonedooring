package com.fonekey.mainpage;
import com.fonekey.R;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;
import java.util.ArrayList;

public class CSliderFermRecyclerAdapter extends RecyclerView.Adapter<CSliderFermRecyclerAdapter.CFermViewHolder>
{
    private List<CFermSlider> m_lstFerm;
    private Context m_context;

    public CSliderFermRecyclerAdapter(Context context) {
        m_context = context;
        m_lstFerm = new ArrayList<>();
    }

    @Override
    public CFermViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ferm_slider, viewGroup, false);
        return new CFermViewHolder(view);
    }

    public static class CFermViewHolder extends RecyclerView.ViewHolder
    {
        TextView m_data;
        SwitchCompat m_swchFermEnable;

        public CFermViewHolder(View itemView) {
            super (itemView);
            m_swchFermEnable = itemView.findViewById(R.id.switchEnableFerm);
            m_data = itemView.findViewById(R.id.txtDateKey);
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

    public void onItemAdd(List<String> data)
    {
        int position = 0;
        CFermSlider fermSlider = new CFermSlider();
        fermSlider.m_fermId = data.get(position++);
        fermSlider.m_dataBegin = data.get(position++);
        fermSlider.m_dataEnd = data.get(position++);
        fermSlider.m_enable = false;

        m_lstFerm.add(0, fermSlider);
        notifyItemInserted(0);
    }

    @Override
    public void onBindViewHolder(CFermViewHolder viewHolder, final int position) {
        String data = m_lstFerm.get(position).m_dataBegin + " - " + m_lstFerm.get(position).m_dataEnd;
        viewHolder.m_data.setText(data);
        viewHolder.m_swchFermEnable.setText(m_lstFerm.get(position).m_fermId);
        viewHolder.m_swchFermEnable.setChecked(m_lstFerm.get(position).m_enable);

        viewHolder.m_swchFermEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = position;
            }
        });
    }

    @Override
    public int getItemCount() { return m_lstFerm.size(); }
}