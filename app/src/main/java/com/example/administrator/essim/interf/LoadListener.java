package com.example.administrator.essim.interf;

public interface LoadListener<T> {

    void onSuccess(T t);

    void onFailed();
}
