package com.legend.ys8.http;

import com.legend.ys8.model.CharacterImpl;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 * Created by legend on 2017/9/12.
 */

public interface Character {

    @GET("get-character-by-id")
    Call<CharacterImpl> getCharacterList();
}
