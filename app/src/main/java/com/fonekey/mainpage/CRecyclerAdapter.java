package com.fonekey.mainpage;
import com.fonekey.R;
import com.fonekey.searchpage.CListFragment;
import com.fonekey.searchpage.CSearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.content.Intent;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import ru.yandex.money.android.sdk.Amount;

import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.PaymentMethodType;
import ru.yandex.money.android.sdk.PaymentParameters;
import ru.yandex.money.android.sdk.SavePaymentMethod;

public class CRecyclerAdapter extends RecyclerView.Adapter<CRecyclerAdapter.CFermViewHolder>
{
    private static boolean m_owner; // true - owner ferms, false - all list ferms
    private final List<CFerm> m_lstFerm;
    private final Context m_context;
    private final Fragment m_fragment;

    public CRecyclerAdapter(boolean owner, Context context, Fragment fragment) {
        m_owner = owner;
        m_context = context;
        m_fragment = fragment;
        m_lstFerm = new ArrayList<>();
    }

    @NotNull
    @Override
    public CFermViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ferm, viewGroup, false);
        return new CFermViewHolder(view);
    }

    public static class CFermViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView m_street;
        private final TextView m_house;
        private final TextView m_rating;
        private final TextView m_distance;
        private final TextView m_number_rooms;
        private final TextView m_price;

        private final ImageView m_imgFerm;

        private final Button m_btnPayDel;

        private final LinearLayout m_layotFerm;

        public CFermViewHolder(View itemView)
        {
            super (itemView);

            m_layotFerm = itemView.findViewById(R.id.layoutFerm);

            m_street = itemView.findViewById(R.id.txtStreet);
            m_house = itemView.findViewById(R.id.txtHouse);
            m_rating = itemView.findViewById(R.id.txtRating);
            m_distance = itemView.findViewById(R.id.txtDistance);
            m_number_rooms = itemView.findViewById(R.id.txtNumberRooms);
            m_price = itemView.findViewById(R.id.txtPrice);

            m_imgFerm = itemView.findViewById(R.id.imgFerm);

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

    public void onItemArray(@NotNull ArrayList<ByteArrayOutputStream> data) {
        int position = 0;
        CFerm ferm = new CFerm();
        ferm.m_id = data.get(position++).toString();

        if(data.get(position++).toString().equals("111"))
            ferm.m_town = "Москва";

        ferm.m_street = data.get(position++).toString();
        ferm.m_house = data.get(position++).toString();
        ferm.m_rating = data.get(position++).toString();
        ferm.m_distance = data.get(position++).toString();
        ferm.m_number_rooms = data.get(position++).toString();
        ferm.m_price = data.get(position++).toString();
        ferm.m_foto = data.get(position++).toByteArray();

        m_lstFerm.add(0, ferm);
        notifyItemInserted(0);
        // notifyItemRangeInserted(m_lstFerm.size() + 1, m_lstFerm.size());
    }

    @Override
    public int getItemCount() { return m_lstFerm.size(); }

    @Override
    public void onBindViewHolder(@NotNull CFermViewHolder viewHolder, final int position) {
        viewHolder.m_street.setText(m_lstFerm.get(position).m_street);
        viewHolder.m_house.setText(m_lstFerm.get(position).m_house);
        viewHolder.m_rating.setText(m_lstFerm.get(position).m_rating);
        viewHolder.m_distance.setText(m_lstFerm.get(position).m_distance);
        viewHolder.m_number_rooms.setText(m_lstFerm.get(position).m_number_rooms);
        viewHolder.m_price.setText(m_lstFerm.get(position).m_price);
        viewHolder.m_imgFerm.setImageBitmap(BitmapFactory.decodeByteArray(m_lstFerm.get(position).m_foto, 0, m_lstFerm.get(position).m_foto.length));

        // Оплата покупки
        viewHolder.m_btnPayDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CMainActivity.m_userId.equals("0000000000")) {

                        Set<PaymentMethodType> paymentMethodTypes = new HashSet<>();
                        paymentMethodTypes.add(PaymentMethodType.BANK_CARD);
                        paymentMethodTypes.add(PaymentMethodType.SBERBANK);
                        paymentMethodTypes.add(PaymentMethodType.GOOGLE_PAY);

                        PaymentParameters paymentParameters = new PaymentParameters(
                                new Amount(BigDecimal.TEN, Currency.getInstance("RUB")),
                                "Название товара",
                                "Описание товара",
                                "test_NzU3NTY0CGtWr6U1ep6M5GCSjKm6n10pXdoKnqFmZ4E",
                                "757564",
                                SavePaymentMethod.OFF,
                                paymentMethodTypes
                        );

                        Intent intentToken = Checkout.createTokenizeIntent(m_context, paymentParameters);
                        m_fragment.startActivityForResult(intentToken, CListFragment.REQUEST_CODE_TOKENIZE);

                    /*Intent intentSend = new Intent(m_context, CListFragment.class);
                    intentSend.putExtra("town", "111");
                    // data.add(CSearch.town.getBytes());
                    intentSend.putExtra("town", "111".getBytes());
                    intentSend.putExtra("ferm_id", m_lstFerm.get(position).m_id.getBytes());
                    intentSend.putExtra("time_begin", "20-10-2020 12:00:00".getBytes());
                    intentSend.putExtra("time_end", "20-10-2020 12:00:01".getBytes());
                    m_fragment.startActivityForResult(intentSend, CListFragment.REQUEST_CODE_SEND);*/

                    Intent intentSend = ((Activity) m_context).getIntent();
                    intentSend.putExtra("town", "111".getBytes());
                    intentSend.putExtra("ferm_id", m_lstFerm.get(position).m_id.getBytes());
                    intentSend.putExtra("time_begin", "20-10-2020 12:00:00".getBytes());
                    intentSend.putExtra("time_end", "20-10-2020 12:00:01".getBytes());

                    ((Activity) m_context).setResult(Activity.RESULT_OK, intentSend);
                    //((Activity) m_context).finish();

                } else {
                    m_context.startActivity(new Intent(m_context, CRegistrationActivity.class));
                }
            }
        });

        // Чтение квартиры
        viewHolder.m_layotFerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<byte[]> data = new ArrayList<>();
                data.add("111".getBytes());
                data.add(m_lstFerm.get(position).m_id.getBytes());

                ByteArrayOutputStream message = CClient.CreateMessage(data, (byte)0x52); // "R"
                ArrayList<ArrayList<ByteArrayOutputStream>> answer = CClient.DataExchange(message.toByteArray(), (byte)0x52);
                if(answer != null) {
                    try {
                        if(answer.size() == 2) {
                            ArrayList<ByteArrayOutputStream> item = answer.get(0);
                            if(item.get(0).toByteArray()[0] != '0')
                                throw new CException("Ошибка сервера");

                            item = answer.get(1);
                            int sizeArray = item.size();
                            int posArray = 0;

                            Intent intent = new Intent(m_context, CViewFermActivity.class);
                            intent.putExtra("town", m_lstFerm.get(position).m_town);
                            intent.putExtra("street", m_lstFerm.get(position).m_street);
                            intent.putExtra("house", m_lstFerm.get(position).m_house);
                            if (posArray >= sizeArray)
                                throw new CException("Ошибка парсера");
                            intent.putExtra("owner", item.get(posArray++).toString());
                            intent.putExtra("rating", m_lstFerm.get(position).m_rating);
                            intent.putExtra("distance", m_lstFerm.get(position).m_distance);
                            intent.putExtra("number_rooms", m_lstFerm.get(position).m_number_rooms);
                            if(posArray >= sizeArray)
                                throw new CException("Ошибка парсера");
                            intent.putExtra("geo", item.get(posArray++).toString());
                            if(posArray >= sizeArray)
                                throw new CException("Ошибка парсера");
                            intent.putExtra("description", item.get(posArray++).toString());
                            intent.putExtra("foto_0", m_lstFerm.get(position).m_foto);

                            if(posArray < sizeArray) {
                                int number_photo = Integer.parseInt(item.get(posArray++).toString());
                                intent.putExtra("number_photo", number_photo);
                                for(int i = 1; i < number_photo; i++) {
                                    if(posArray < sizeArray)
                                        intent.putExtra("foto_" + i, item.get(posArray++).toByteArray());
                                    else
                                        throw new CException("Ошибка парсера");
                                }
                            } else
                                throw new CException("Ошибка парсера");

                            if(posArray < sizeArray) {
                                int number_comments = Integer.parseInt(item.get(posArray++).toString());
                                intent.putExtra("number_comments", number_comments);
                                for (int i = 0; i < number_comments; i++) {
                                    if (posArray < sizeArray)
                                        intent.putExtra("comments_" + i, item.get(posArray++).toByteArray());
                                    else
                                        throw new CException("Ошибка парсера");
                                }
                            } else
                                throw new CException("Ошибка парсера");

                            if(posArray != sizeArray)
                                throw new CException("Ошибка парсера");

                            m_context.startActivity(intent);
                        } else
                            throw new Error();
                    } catch (CException error) {
                        Toast.makeText(m_context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(m_context, R.string.not_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }
}