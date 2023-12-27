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
}