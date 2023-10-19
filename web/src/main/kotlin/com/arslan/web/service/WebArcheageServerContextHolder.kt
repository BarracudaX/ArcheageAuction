package com.arslan.web.service

class WebArcheageServerContextHolder {

    companion object{
        private val serverContext: ThreadLocal<String> = ThreadLocal()

        @JvmStatic
        fun setServerContext(server: String){
            serverContext.set(server)
        }

        @JvmStatic
        fun getServerContext() : String? = serverContext.get()

        @JvmStatic
        fun clear(){
            serverContext.remove()
        }
    }

}