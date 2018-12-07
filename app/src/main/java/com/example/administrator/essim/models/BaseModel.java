package com.example.administrator.essim.models;

import com.example.administrator.essim.interf.LoadListener;

public interface BaseModel<DataResponse> {

    void getData(LoadListener<DataResponse> listener);

}
