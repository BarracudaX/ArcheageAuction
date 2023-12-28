package com.arslan.archeage.web

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ArcheageServerContextHolder
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class ArcheageServerHandlerMethodArgumentResolver : HandlerMethodArgumentResolver{

    override fun supportsParameter(parameter: MethodParameter): Boolean = ArcheageServer::class.java.isAssignableFrom(parameter.parameterType)

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        return  ArcheageServerContextHolder.getServerContext()
    }

}