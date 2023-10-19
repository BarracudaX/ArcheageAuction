package com.arslan.data.service

import com.arslan.data.entity.Server

interface ServerService {

    fun servers() : List<Server>

}