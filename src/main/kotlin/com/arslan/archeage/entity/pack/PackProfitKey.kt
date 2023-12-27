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

}