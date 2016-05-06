package com.equidais.mybeacon.controller.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.model.DetailsModel;

import java.util.ArrayList;
import java.util.List;

public class DetailedTotal extends Fragment implements MonthController.MonthCallbackListener{

    private MonthController controller;
    private List<DetailsModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_total, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);
        controller.gettotaldata();

        return  view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new MonthController(DetailedTotal.this);

    }

    @Override
    public void onFetchStart() {

    }

    @Override
    public void onFetchProgress(DetailsModel detailsModel) {
        adapter.addList(detailsModel);

    }

    @Override
    public void onFetchProgress(List<DetailsModel> detailsModelList) {

    }

    @Override
    public void onFetchComplete() {

    }

    @Override
    public void onFetchFailed() {

    }


    //////////////////////////////////////// ADAPTER /////////////////////////////////////

    public class Adapter extends RecyclerView.Adapter<Adapter.Holder>{

        public List<DetailsModel> modelList;
        public DetailsModel data;

        public void addList(DetailsModel detailsModel){
            modelList.add(detailsModel);
            notifyDataSetChanged();
        }

        public Adapter(List<DetailsModel> modelList) {
            this.modelList = modelList;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_list_details, parent, false);

            return new Holder(row);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            data = modelList.get(position);
            holder.datetimeview.setText("Time in: " + data.mIntTime + " - " + "Time Out: " + data.mOutTime);
            holder.duration.setText(GlobalFunc.getDatesDifference( data.mIntTime, data.mOutTime));
        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        public class Holder extends RecyclerView.ViewHolder{

            public View parentView;
            public TextView datetimeview, duration;


            public Holder(View itemView) {
                super(itemView);

                this.parentView = itemView;
                datetimeview = (TextView)itemView.findViewById(R.id.txt_date);
                duration = (TextView)itemView.findViewById(R.id.txt_duration);

            }

        }
    }
}