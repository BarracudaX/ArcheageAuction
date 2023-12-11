package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer

class ArcheageServerContextHolder {


    companion object{
        private val serverContext: ThreadLocal<ArcheageServer> = ThreadLocal()

        @JvmStatic
        fun setServerContext(server: ArcheageServer){
            serverContext.set(server)
        }

        @JvmStatic
        fun getServerContext() : ArcheageServer? = serverContext.get()

        @JvmStatic
        fun clear(){
            serverContext.remove()
        }
    }

}