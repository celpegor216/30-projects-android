package fastcampus.aop.pjt30_food_delivery.data.url

object Url {
    const val TMAP_URL = "https://apis.openapi.sk.com"
    const val GET_TMAP_POIS = "/tmap/pois"
    const val GET_TMAP_POIS_AROUND = "/tmap/pois/search/around"
    const val GET_TMAP_REVERSE_GEO_CODE = "/tmap/geo/reversegeocoding"

    const val FOOD_URL = "https://65decaf5ff5e305f32a076aa.mockapi.io"
    const val GET_RESTAURANT_FOOD = "/restaurants/{restaurantId}/foods"
}