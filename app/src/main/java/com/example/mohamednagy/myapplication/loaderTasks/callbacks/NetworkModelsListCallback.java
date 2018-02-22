package com.example.mohamednagy.myapplication.loaderTasks.callbacks;

import com.example.mohamednagy.myapplication.model.Review;

import java.util.List;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public interface NetworkModelsListCallback<T> extends NetworkConnetionsCallback<T>{
    void updateUi(T reviewList);
}
