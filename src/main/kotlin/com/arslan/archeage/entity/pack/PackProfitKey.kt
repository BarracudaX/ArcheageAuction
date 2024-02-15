package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.User
import jakarta.persistence.Embeddable
import jakarta.persistence.ManyToOne
import java.io.Serializable

@Embeddable
class PackProfitKey(
    @ManyToOne
    val pack: Pack,

    @ManyToOne
    var user: User
) : Serializable{

    companion object{ private const val serialVersionUID: Long = 1L }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackProfitKey

        if (pack.id != other.pack.id) return false
        return user.id == other.user.id
    }

    override fun hashCode(): Int {
        var result = pack.id.hashCode()
        result = 31 * result + user.id.hashCode()
        return result
    }


}