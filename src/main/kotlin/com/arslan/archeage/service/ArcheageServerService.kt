package com.arslan.archeage.service

import com.arslan.archeage.entity.Region
import com.arslan.archeage.entity.ArcheageServer

interface ArcheageServerService {

    fun servers() : Map<Region,List<ArcheageServer>>

}