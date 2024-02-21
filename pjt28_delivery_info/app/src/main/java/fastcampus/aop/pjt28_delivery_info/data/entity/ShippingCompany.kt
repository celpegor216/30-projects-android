package fastcampus.aop.pjt28_delivery_info.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ShippingCompany(
    @PrimaryKey
    @SerializedName("Code") val code: String,
    @SerializedName("Name") val name: String
): Parcelable
