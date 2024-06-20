package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class Pagination(

    @field:SerializedName("TotalPage")
    val totalPage: Int,

    @field:SerializedName("RecordPerPage")
    val recordPerPage: Int,

    @field:SerializedName("CurrentPage")
    val currentPage: Int,

    @field:SerializedName("Next")
    val next: Int,

    @field:SerializedName("Previous")
    val previous: Int
)