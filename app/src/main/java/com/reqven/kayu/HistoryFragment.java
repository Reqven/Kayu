package com.reqven.kayu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private FragmentHistoryListener listener;
    private ArrayList<Product> products;
    private ArrayList<String> codes;
    private HistoryViewAdapter adapter;

    public interface FragmentHistoryListener {
        void onInputHistorySent(Product product);
    }

    public HistoryFragment() {
        products = new ArrayList<>();
        //products.add(new Product("3564700019313"));
        //products.add(new Product("3478820023184"));
        //products.add(new Product("3017620424403"));/*
        //products.add(new Product("568986451684651"))
        //products.add(new Product("3329770061866"));
        //products.add(new Product("7613031333394"));*/

        adapter = new HistoryViewAdapter(products, new HistoryViewAdapter.OnListItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent intent = new Intent(getContext(), ProductActivity.class);
                intent.putExtra("code", products.get(position).getBarcode());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        final Context context = view.getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(context, null));
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentHistoryListener) {
            listener = (FragmentHistoryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentHistoryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public void addItem(Product product) {
        products.add(product);
        adapter.notifyItemInserted(products.size() - 1);
    }
}
