package com.arslan.price.entity

import jakarta.persistence.Embeddable


@Embeddable
data class Price(val gold: Int,val silver: Int, val copper: Int)