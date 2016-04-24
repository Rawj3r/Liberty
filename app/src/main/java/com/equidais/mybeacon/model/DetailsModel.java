package com.equidais.mybeacon.model;

/**
 * Created by empirestate on 4/21/16.
 */
public class DetailsModel {

    public String cname, mIntTime, mOutTime;

    public DetailsModel(Builder builder){
        cname = builder.cname;
        mIntTime = builder.mIntTime;
        mOutTime = builder.mOutTime;
    }

    public static class Builder{
        String cname, mIntTime, mOutTime;

        public Builder setCName(String cName){
            this.cname = cName;
            return Builder.this;
        }

        public Builder setMInTime(String mInTime){
            this.mIntTime = mInTime;
            return Builder.this;
        }

        public Builder setMOutTime(String mOutTime){
            this.mOutTime = mOutTime;
            return Builder.this;
        }

        public DetailsModel build(){
            return new DetailsModel(Builder.this);
        }

    }
}
