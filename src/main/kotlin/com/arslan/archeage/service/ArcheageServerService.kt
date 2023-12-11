package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer

interface ArcheageServerService {

    fun servers() : List<ArcheageServer>

}