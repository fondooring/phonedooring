package com.fonekey.mainpage;
import com.fonekey.R;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;

public class CSliderFermRecyclerAdapter extends RecyclerView.Adapter<CSliderFermRecyclerAdapter.CFermViewHolder>
{
    static byte[] g_tag = new byte[11];

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
        TextView m_fermid;
        SwitchCompat m_swchFermEnable;

        public CFermViewHolder(View itemView) {
            super (itemView);
            m_swchFermEnable = itemView.findViewById(R.id.switchEnableFerm);
            m_data = itemView.findViewById(R.id.txtDateKey);
            m_fermid = itemView.findViewById(R.id.txtFermID);
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

    public void onItemAdd(@NotNull ArrayList<ByteArrayOutputStream> data)
    {
        CFermSlider fermSlider = new CFermSlider();
        fermSlider.m_fermId = data.get(0).toString();
        fermSlider.m_dataBegin = "1.1.2020";
        fermSlider.m_dataEnd = "31.12.2020";
        fermSlider.m_tag = data.get(3).toString();
        fermSlider.m_enable = false;

        m_lstFerm.add(0, fermSlider);
        notifyItemInserted(0);
    }

    @Override
    public void onBindViewHolder(CFermViewHolder viewHolder, final int position) {
        String data = m_lstFerm.get(position).m_dataBegin + " - " + m_lstFerm.get(position).m_dataEnd;
        viewHolder.m_data.setText(data);
        viewHolder.m_fermid.setText(m_lstFerm.get(position).m_tag);
        viewHolder.m_swchFermEnable.setText(m_lstFerm.get(position).m_fermId);
        viewHolder.m_swchFermEnable.setChecked(m_lstFerm.get(position).m_enable);

        viewHolder.m_swchFermEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                for(int i = 0; i < 10; i++)
                    g_tag[i] = m_lstFerm.get(position).m_tag.getBytes()[i];

                if(isChecked)
                    g_tag[10] = 1;
                else
                    g_tag[10] = 0;
            }
        });

    }

    @Override
    public int getItemCount() { return m_lstFerm.size(); }
}