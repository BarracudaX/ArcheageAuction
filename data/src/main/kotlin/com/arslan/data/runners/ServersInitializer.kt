package com.arslan.data.runners

import com.arslan.data.entity.Server
import com.arslan.data.repository.ServerRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import java.util.Locale

@Component
@Profile("dev")
class ServersInitializer(private val serverRepository: ServerRepository) : ApplicationRunner{

    override fun run(args: ApplicationArguments?) {
        LocaleContextHolder.setLocale(Locale.ENGLISH)
        if(serverRepository.count() != 0L) return

        serverRepository.save(Server("ardeniya"))
        serverRepository.save(Server("nehiliya"))

        LocaleContextHolder.setLocale(Locale("ru","RU"))

        serverRepository.save(Server("луций"))
        serverRepository.save(Server("корвус"))
        serverRepository.save(Server("фанем"))
        serverRepository.save(Server("шаеда"))
        serverRepository.save(Server("ифнир"))
        serverRepository.save(Server("ксанатос"))
        serverRepository.save(Server("тарон"))
        serverRepository.save(Server("рейвен"))
        serverRepository.save(Server("нагашар"))

        LocaleContextHolder.setLocale(Locale.ENGLISH)
    }


}