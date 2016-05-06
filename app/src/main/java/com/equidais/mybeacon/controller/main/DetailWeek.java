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
import com.equidais.mybeacon.controller.common.Controller;
import com.equidais.mybeacon.model.DetailsModel;

import java.util.ArrayList;
import java.util.List;

public class DetailWeek extends Fragment implements Controller.HomeCallBackListener{

    private RecyclerView recyclerView;
    private List<DetailsModel> modelList = new ArrayList<>();
    private DetailWeekDapter weekDapter;
    private Controller controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new Controller(DetailWeek.this);
        controller.startFetching();


    }

    public DetailWeek() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail_week, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        weekDapter = new DetailWeekDapter(modelList);
        recyclerView.setAdapter(weekDapter);

        return view;
    }

    @Override
    public void onFetchStart() {

    }

    @Override
    public void onFetchProgress(DetailsModel detailsModel) {
        weekDapter.addList(detailsModel);

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

    ///////////////////////////////////////////////////////// Detail Adapter ///////////////////////////////////////////////////////////////////////////////////////////////

    public class DetailWeekDapter extends RecyclerView.Adapter<DetailWeekDapter.Holder>{

        public  String TAG = DetailWeekDapter.class.getSimpleName();
        public List<DetailsModel> detailsModels;
        public DetailWeek detailWeek;
        public DetailsModel currData;

        public void addList(DetailsModel detailsModel){
            detailsModels.add(detailsModel);
            notifyDataSetChanged();
        }


        public DetailWeekDapter(List<DetailsModel> detailsModelList){
            this.detailsModels = detailsModelList;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_list, parent, false);

            return new Holder(row);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            currData = detailsModels.get(position);
            holder.datetimeview.setText("Time in: " + currData.mIntTime + " - " + "Time Out: " + currData.mOutTime);
            holder.duration.setText(GlobalFunc.getDatesDifference(currData.mIntTime, currData.mOutTime));
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