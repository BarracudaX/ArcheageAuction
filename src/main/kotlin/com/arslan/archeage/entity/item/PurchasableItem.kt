package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.Region
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize
import java.util.*

@Table(name = "purchasable_items")
@Entity
open class PurchasableItem(
    name: String,

    description: String,

    region: Region
) : Item(name, description, region)