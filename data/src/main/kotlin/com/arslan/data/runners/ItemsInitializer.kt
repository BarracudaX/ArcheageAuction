package com.arslan.data.runners

import com.arslan.data.entity.Item
import com.arslan.data.repository.ItemRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("dev")
@Order(2)
class ItemsInitializer(private val itemRepository: ItemRepository) : ApplicationRunner{
    override fun run(args: ApplicationArguments?) {
        LocaleContextHolder.setLocale(Locale.ENGLISH)

        if(itemRepository.count() != 0L) return

        itemRepository.save(Item("ground spices",""))
        itemRepository.save(Item("egg",""))
        itemRepository.save(Item("gilda star",""))
        itemRepository.save(Item("chopped produce",""))
        itemRepository.save(Item("banana",""))
        itemRepository.save(Item("medicinal powder",""))
        itemRepository.save(Item("goose down",""))
        itemRepository.save(Item("milk",""))
        itemRepository.save(Item("grape",""))
        itemRepository.save(Item("fig",""))
        itemRepository.save(Item("wool",""))
        itemRepository.save(Item("cherry",""))
        itemRepository.save(Item("pomegranate",""))
        itemRepository.save(Item("orange",""))
        itemRepository.save(Item("olive",""))

        LocaleContextHolder.setLocale(Locale("ru","RU"))

        itemRepository.save(Item("молотые специи",""))
        itemRepository.save(Item("яйцо",""))
        itemRepository.save(Item("дельфийская звезда",""))
        itemRepository.save(Item("нашинкованные овощи",""))
        itemRepository.save(Item("банан",""))
        itemRepository.save(Item("лекарственный порошок",""))
        itemRepository.save(Item("яблоко",""))
        itemRepository.save(Item("гусиное перо",""))
        itemRepository.save(Item("измельченные злаки",""))
        itemRepository.save(Item("молоко",""))
        itemRepository.save(Item("виноград",""))
        itemRepository.save(Item("свежевыжатый сок",""))
        itemRepository.save(Item("инжир",""))
        itemRepository.save(Item("шерсть ятты",""))
        itemRepository.save(Item("финик",""))
        itemRepository.save(Item("измельченные лепестки цветов",""))
        itemRepository.save(Item("овечья шерсть",""))
        itemRepository.save(Item("вишня",""))
        itemRepository.save(Item("гранат",""))
        itemRepository.save(Item("апельсин",""))
        itemRepository.save(Item("оливки",""))
        itemRepository.save(Item("картофель",""))
        itemRepository.save(Item("лилия",""))
        itemRepository.save(Item("лаванда",""))
        itemRepository.save(Item("азалия",""))
        itemRepository.save(Item("подсолнух",""))
        itemRepository.save(Item("морковь",""))
        itemRepository.save(Item("помидор",""))
        itemRepository.save(Item("огурец",""))
        itemRepository.save(Item("шафран",""))
        itemRepository.save(Item("рис",""))
        itemRepository.save(Item("чеснок",""))
        itemRepository.save(Item("роза",""))
        itemRepository.save(Item("просо",""))
        itemRepository.save(Item("алоэ",""))
        itemRepository.save(Item("арахис",""))
        itemRepository.save(Item("василек",""))
        itemRepository.save(Item("мята",""))
        itemRepository.save(Item("батат",""))
        itemRepository.save(Item("ремесленный молоточек",""))
        itemRepository.save(Item("густое шлифовальное масло",""))
        itemRepository.save(Item("густой лакировочный состав",""))
        itemRepository.save(Item("густое пропиточное масло",""))
        itemRepository.save(Item("сыромятная кожа",""))
        itemRepository.save(Item("упаковка строительной древесины",""))
        itemRepository.save(Item("каменный блок",""))
        itemRepository.save(Item("слиток железа",""))
        itemRepository.save(Item("утиное перо",""))
        itemRepository.save(Item("авокадо",""))
        itemRepository.save(Item("плод моринги",""))
        itemRepository.save(Item("мясной фарш",""))
        itemRepository.save(Item("лимон",""))
        itemRepository.save(Item("клубок лиловых молний",""))
        itemRepository.save(Item("вексель консорциума синей соли",""))
        itemRepository.save(Item("шкура снежного великана",""))
        itemRepository.save(Item("клык огненной кобры",""))
        itemRepository.save(Item("проклятая душа",""))
        itemRepository.save(Item("обломок древней секиры",""))
        itemRepository.save(Item("дельфийская пыль",""))

        LocaleContextHolder.setLocale(Locale.ENGLISH)
    }


}