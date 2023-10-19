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
        itemRepository.save(Item("нашинковонные овощи",""))
        itemRepository.save(Item("банан",""))
        itemRepository.save(Item("лекарственый порошок",""))
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

        LocaleContextHolder.setLocale(Locale.ENGLISH)
    }


}