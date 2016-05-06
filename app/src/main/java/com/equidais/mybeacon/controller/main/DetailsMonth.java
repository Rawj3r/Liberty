package com.equidais.mybeacon.controller.main;


import android.os.Bundle;
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


public class DetailsMonth extends Fragment implements MonthController.MonthCallbackListener{

    private MonthController monthController;
    private List<DetailsModel> detailsModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private DetailMonthAdapter detailMonthAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        monthController = new MonthController(DetailsMonth.this);
        monthController.getmonthdata();
    }

    public DetailsMonth() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_month, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        detailMonthAdapter = new DetailMonthAdapter(detailsModels);
        recyclerView.setAdapter(detailMonthAdapter);


        return view;
    }


    @Override
    public void onFetchStart() {

    }

    @Override
    public void onFetchProgress(DetailsModel detailsModel) {
        detailMonthAdapter.addList(detailsModel);
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

    ////////////////////////////// ADAPTER ///////////////////////////

    public class DetailMonthAdapter extends RecyclerView.Adapter<DetailMonthAdapter.Holder>{

        public String TAG = DetailMonthAdapter.class.getSimpleName();
        public List<DetailsModel> detailsModels;
        public DetailsMonth detailsMonth;
        public DetailsModel data;

        public void addList(DetailsModel detailsModel){
            detailsModels.add(detailsModel);
            notifyDataSetChanged();
        }

        public DetailMonthAdapter(List<DetailsModel> detailsModels) {
            this.detailsModels = detailsModels;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_list_details, parent, false);

            return new Holder(row);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            data = detailsModels.get(position);
            holder.datetimeview.setText("Time in: " + data.mIntTime + " - " + "Time Out: " + data.mOutTime);
            holder.duration.setText(GlobalFunc.getDatesDifference( data.mIntTime, data.mOutTime));

        }

        @Override
        public int getItemCount() {
            return detailsModels.size();
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
