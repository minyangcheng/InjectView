package com.min.inject.api;


/**
 * Created by minyangcheng on 2016/11/1.
 */
public interface Inject<T> {

    public void bind(T target , Object source , Provider provider);

}
