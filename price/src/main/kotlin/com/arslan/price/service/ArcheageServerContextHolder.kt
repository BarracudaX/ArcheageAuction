package com.arslan.price.service

class ArcheageServerContextHolder {

    companion object{
        private val serverContext: ThreadLocal<String> = ThreadLocal()

        fun setServerContext(server: String){
            serverContext.set(server)
        }

        fun getServerContext() : String = serverContext.get()

        fun clear(){
            serverContext.remove()
        }
    }

}