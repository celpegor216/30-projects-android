package fastcampus.aop.pjt27_subway_info.data.api.response


import com.google.gson.annotations.SerializedName

data class RealtimeStationArrivals(
    @SerializedName("errorMessage")
    val errorMessage: ErrorMessage?,
    @SerializedName("realtimeArrivalList")
    val realtimeArrivalList: List<RealtimeArrival>?
)