package fastcampus.aop.pjt21_particle_pollution.data.models.tmcoordinates


import com.google.gson.annotations.SerializedName

data class TmCoordinatesResponse(
    @SerializedName("documents")
    val documents: List<Document?>?,
    @SerializedName("meta")
    val meta: Meta?
)