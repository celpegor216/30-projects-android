package fastcampus.aop.pjt18_map.response.search

data class Poi(
    val id: String? = null,
    val name: String? = null,
    val telNo: String? = null,
    val frontLat: Float = 0.0f,    // 시설물 입구 위도
    val frontLon: Float = 0.0f,    // 시설물 입구 경도
    val noorLat: Float = 0.0f,    // 중심점 위도
    val noorLon: Float = 0.0f,    // 중심점 경도
    val upperAddrName: String? = null,   //표출 주소 대분류
    val middleAddrName: String? = null,    // 표출 주소 중분류
    val lowerAddrName: String? = null,    // 표출 주소 소분류
    val detailAddrName: String? = null,    // 표출 주소 세분류
    val firstNo: String? = null,    // 본번
    val secondNo: String? = null,    // 부번
    val roadName: String? = null,    // 도로명
    val firstBuildNo: String? = null,    // 건물번호 1
    val secondBuildNo: String? = null,    // 건물번호 2
    val mlClass: String? = null,    // 업종 대분류
    val radius: String? = null,    // 거리(km)
    val bizName: String? = null,    // 업소명
    val upperBizName: String? = null,    // 시설 목적
    val middleBizName: String? = null,    // 시설 분류
    val lowerBizName: String? = null,    // 시설 이름
    val detailBizName: String? = null,    // 시설 상세 이름
    val rpFlag: String? = null,    // 길안내 요청 유무
    val parkFlag: String? = null,    // 주차 가능 유무
    val detailInfoFlag: String? = null,    // 상세 정보 유무
    val desc: String? = null    // 소개 정보
)