package fastcampus.aop.pjt27_subway_info.data.api.response


import com.google.gson.annotations.SerializedName

data class RealtimeArrival(
    @SerializedName("arvlCd")
    val arvlCd: String?,
    @SerializedName("arvlMsg2")
    val arvlMsg2: String?,
    @SerializedName("arvlMsg3")
    val arvlMsg3: String?,
    @SerializedName("barvlDt")
    val barvlDt: String?,
    @SerializedName("beginRow")
    val beginRow: Any?,
    @SerializedName("bstatnId")
    val bstatnId: String?,
    @SerializedName("bstatnNm")
    val bstatnNm: String?,
    @SerializedName("btrainNo")
    val btrainNo: String?,
    @SerializedName("btrainSttus")
    val btrainSttus: String?,
    @SerializedName("curPage")
    val curPage: Any?,
    @SerializedName("endRow")
    val endRow: Any?,
    @SerializedName("ordkey")
    val ordkey: String?,
    @SerializedName("pageRow")
    val pageRow: Any?,
    @SerializedName("recptnDt")
    val recptnDt: String?,
    @SerializedName("rowNum")
    val rowNum: Int?,
    @SerializedName("selectedCount")
    val selectedCount: Int?,
    @SerializedName("statnFid")
    val statnFid: String?,
    @SerializedName("statnId")
    val statnId: String?,
    @SerializedName("statnList")
    val statnList: String?,
    @SerializedName("statnNm")
    val statnNm: String?,
    @SerializedName("statnTid")
    val statnTid: String?,
    @SerializedName("subwayHeading")
    val subwayHeading: Any?,
    @SerializedName("subwayId")
    val subwayId: Int,
    @SerializedName("subwayList")
    val subwayList: String?,
    @SerializedName("subwayNm")
    val subwayNm: Any?,
    @SerializedName("totalCount")
    val totalCount: Int?,
    @SerializedName("trainCo")
    val trainCo: Any?,
    @SerializedName("trainLineNm")
    val trainLineNm: String?,
    @SerializedName("trnsitCo")
    val trnsitCo: String?,
    @SerializedName("updnLine")
    val updnLine: String?
)