package com.alex.greenroute.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alex on 12/14/2015.
 */
public class ApiResponse {
    @SerializedName("user_type")
    public Integer type;

    @SerializedName("json_messages")
    public List<LogResponse> logs;

    @SerializedName("cards")
    public List<CardResponse> cards;
}
