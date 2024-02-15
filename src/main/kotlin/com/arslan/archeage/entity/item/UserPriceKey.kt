package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.User
import jakarta.persistence.Embeddable
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.io.Serializable

@Embeddable
class UserPriceKey(

    @ManyToOne
    var user: User,

    @JoinColumn(name = "item_id")
    @ManyToOne(optional = false)
    var purchasableItem: PurchasableItem
) : Serializable{
    companion object{ private const val serialVersionUID: Long = 1L }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPriceKey

        if (user.id != other.user.id) return false
        return purchasableItem.id == other.purchasableItem.id
    }

    override fun hashCode(): Int {
        var result = user.id.hashCode()
        result = 31 * result + purchasableItem.id.hashCode()
        return result
    }


}