package com.arslan.data.runners

import com.arslan.data.entity.Continent
import com.arslan.data.entity.Location
import com.arslan.data.repository.LocationRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("dev")
@Order(1)
class LocationsInitializer(
    private val locationRepository: LocationRepository,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        LocaleContextHolder.setLocale(Locale.ENGLISH)

        if(locationRepository.count() != 0L) return

        locationRepository.save(Location("solis headlands",Continent.EAST,true))
        locationRepository.save(Location("mahadevi",Continent.EAST))
        locationRepository.save(Location("villanelle",Continent.EAST,true))
        locationRepository.save(Location("silent forest",Continent.EAST))
        locationRepository.save(Location("ynystere",Continent.EAST,true))
        locationRepository.save(Location("rokhala mountains",Continent.EAST))
        locationRepository.save(Location("rookborne basin",Continent.EAST))
        locationRepository.save(Location("falcorth plains",Continent.EAST))
        locationRepository.save(Location("tigerspine mountains",Continent.EAST))
        locationRepository.save(Location("arcum iris",Continent.EAST))
        locationRepository.save(Location("sunbite wilds",Continent.EAST))
        locationRepository.save(Location("windscour savannah",Continent.EAST))
        locationRepository.save(Location("perinoor ruins",Continent.EAST))
        locationRepository.save(Location("hasla",Continent.EAST))

        LocaleContextHolder.setLocale(Locale("ru","RU"))

        locationRepository.save(Location("багровый каньон",Continent.EAST))
        locationRepository.save(Location("долина талых снегов",Continent.EAST))
        locationRepository.save(Location("древний лес",Continent.EAST))
        locationRepository.save(Location("инистра",Continent.EAST,true))
        locationRepository.save(Location("махадеби",Continent.EAST))
        locationRepository.save(Location("плато соколиной охоты",Continent.EAST))
        locationRepository.save(Location("полуостров рассвета",Continent.EAST,true))
        locationRepository.save(Location("поющая земля",Continent.EAST,true))
        locationRepository.save(Location("радужные пески",Continent.EAST))
        locationRepository.save(Location("рокочущие перевалы",Continent.EAST))
        locationRepository.save(Location("руины харихараллы",Continent.EAST))
        locationRepository.save(Location("саванна",Continent.EAST))
        locationRepository.save(Location("тигриный хребет",Continent.EAST))
        locationRepository.save(Location("хазира",Continent.EAST))

        LocaleContextHolder.setLocale(Locale.ENGLISH)
    }

}