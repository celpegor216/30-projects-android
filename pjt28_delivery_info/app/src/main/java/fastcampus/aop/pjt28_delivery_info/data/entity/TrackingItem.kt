package fastcampus.aop.pjt28_delivery_info.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["invoice", "code"])
data class TrackingItem(
    val invoice: String,

    // embedded 어노테이션을 사용하면 ShippingCompany의 필드들이 모두 포함됨
    // 따라서 위의 primaryKeys에서 code 또한 사용 가능
    @Embedded val company: ShippingCompany
): Parcelable
