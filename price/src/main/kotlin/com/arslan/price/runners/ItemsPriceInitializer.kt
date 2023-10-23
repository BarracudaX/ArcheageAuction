package com.arslan.price.runners

import com.arslan.price.entity.ItemPrice
import com.arslan.price.entity.PackPrice
import com.arslan.price.entity.Price
import com.arslan.price.repository.ItemPriceRepository
import com.arslan.price.service.PriceArcheageServerContextHolder
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class ItemsPriceInitializer(private val itemPriceRepository: ItemPriceRepository) : ApplicationRunner{
    override fun run(args: ApplicationArguments?) {
        PriceArcheageServerContextHolder.setServerContext("nagashar")
        if(itemPriceRepository.count() != 0L) return

        itemPriceRepository.save(ItemPrice("яйцо",Price(0,3,11)))
        itemPriceRepository.save(ItemPrice("молотые специи",Price(0,3,5)))
        itemPriceRepository.save(ItemPrice("нашинкованные овощи",Price(0,3,35)))
        itemPriceRepository.save(ItemPrice("банан",Price(0,11,84)))
        itemPriceRepository.save(ItemPrice("лекарственный порошок",Price(0,3,18)))
        itemPriceRepository.save(ItemPrice("гусиное перо",Price(0,4,57)))
        itemPriceRepository.save(ItemPrice("измельченные злаки",Price(0,3,16)))
        itemPriceRepository.save(ItemPrice("молоко",Price(0,10,12)))
        itemPriceRepository.save(ItemPrice("виноград",Price(0,3,95)))
        itemPriceRepository.save(ItemPrice("свежевыжатый сок",Price(0,3,16)))
        itemPriceRepository.save(ItemPrice("инжир",Price(0,8,48)))
        itemPriceRepository.save(ItemPrice("шерсть ятты",Price(0,11,73)))
        itemPriceRepository.save(ItemPrice("финик",Price(0,14,27)))
        itemPriceRepository.save(ItemPrice("измельченные лепестки цветов",Price(0,3,21)))
        itemPriceRepository.save(ItemPrice("овечья шерсть",Price(0,5,20)))
        itemPriceRepository.save(ItemPrice("вишня",Price(0,53,23)))
        itemPriceRepository.save(ItemPrice("гранат",Price(0,53,12)))
        itemPriceRepository.save(ItemPrice("апельсин",Price(0,62,91)))
        itemPriceRepository.save(ItemPrice("оливки",Price(0,16,56)))
        itemPriceRepository.save(ItemPrice("картофель",Price(0,2,85)))
        itemPriceRepository.save(ItemPrice("лилия",Price(0,4,2)))
        itemPriceRepository.save(ItemPrice("лаванда",Price(0,3,2)))
        itemPriceRepository.save(ItemPrice("азалия",Price(0,3,85)))
        itemPriceRepository.save(ItemPrice("подсолнух",Price(0,8,33)))
        itemPriceRepository.save(ItemPrice("морковь",Price(0,4,99)))
        itemPriceRepository.save(ItemPrice("помидор",Price(0,4,1)))
        itemPriceRepository.save(ItemPrice("огурец",Price(0,3,78)))
        itemPriceRepository.save(ItemPrice("шафран",Price(0,29,37)))
        itemPriceRepository.save(ItemPrice("рис",Price(0,2,57)))
        itemPriceRepository.save(ItemPrice("чеснок",Price(0,5,25)))
        itemPriceRepository.save(ItemPrice("роза",Price(0,4,36)))
        itemPriceRepository.save(ItemPrice("просо",Price(0,2,9)))
        itemPriceRepository.save(ItemPrice("алоэ",Price(0,10,2)))
        itemPriceRepository.save(ItemPrice("арахис",Price(0,33,73)))
        itemPriceRepository.save(ItemPrice("василек",Price(0,15,50)))
        itemPriceRepository.save(ItemPrice("мята",Price(0,20,18)))
        itemPriceRepository.save(ItemPrice("батат",Price(0,12,52)))
        itemPriceRepository.save(ItemPrice("ремесленный молоточек",Price(1,0,0)))
        itemPriceRepository.save(ItemPrice("густое шлифовальное масло",Price(16,45,72)))
        itemPriceRepository.save(ItemPrice("густой лакировочный состав",Price(16,65,46)))
        itemPriceRepository.save(ItemPrice("густое пропиточное масло",Price(16,56,54)))
        itemPriceRepository.save(ItemPrice("сыромятная кожа",Price(0,37,77)))
        itemPriceRepository.save(ItemPrice("упаковка строительной древесины",Price(0,26,29)))
        itemPriceRepository.save(ItemPrice("каменный блок",Price(0,22,9)))
        itemPriceRepository.save(ItemPrice("слиток железа",Price(0,56,28)))
        itemPriceRepository.save(ItemPrice("яблоко",Price(0,10,90)))
        itemPriceRepository.save(ItemPrice("лимон",Price(0,17,53)))
        itemPriceRepository.save(ItemPrice("утиное перо",Price(0,6,10)))
        itemPriceRepository.save(ItemPrice("авокадо",Price(0,15,24)))
        itemPriceRepository.save(ItemPrice("плод моринги",Price(0,59,92)))
        itemPriceRepository.save(ItemPrice("мясной фарш",Price(0,3,32)))
        itemPriceRepository.save(PackPrice("маринованые яйца","полуостров рассвета",Price(25,31,13)))
        itemPriceRepository.save(PackPrice("маринованые яйца","поющая земля",Price(25,30,79)))
        itemPriceRepository.save(PackPrice("маринованые яйца","инистра",Price(29,14,22)))

        itemPriceRepository.save(PackPrice("банановое повидло","полуостров рассвета",Price(22,32,77)))
        itemPriceRepository.save(PackPrice("банановое повидло","поющая земля",Price(22,32,44)))
        itemPriceRepository.save(PackPrice("банановое повидло","инистра",Price(25,82,53)))

        itemPriceRepository.save(PackPrice("набивка для тюфяка","полуостров рассвета",Price(23,73,78)))
        itemPriceRepository.save(PackPrice("набивка для тюфяка","поющая земля",Price(22,35,22)))
        itemPriceRepository.save(PackPrice("набивка для тюфяка","инистра",Price(25,85,31)))

        itemPriceRepository.save(PackPrice("яблочные оладьи","полуостров рассвета",Price(11,28,97)))
        itemPriceRepository.save(PackPrice("яблочные оладьи","поющая земля",Price(9,26,0)))
        itemPriceRepository.save(PackPrice("яблочные оладьи","инистра",Price(12,2,91)))

        itemPriceRepository.save(PackPrice("хрустящие лепешки","полуостров рассвета",Price(23,2,37)))
        itemPriceRepository.save(PackPrice("хрустящие лепешки","поющая земля",Price(23,2,7)))
        itemPriceRepository.save(PackPrice("хрустящие лепешки","инистра",Price(26,52,17)))

        itemPriceRepository.save(PackPrice("виноградный джем","полуостров рассвета",Price(10,26,23)))
        itemPriceRepository.save(PackPrice("виноградный джем","поющая земля",Price(10,26,2)))
        itemPriceRepository.save(PackPrice("виноградный джем","инистра",Price(12,29,54)))

        itemPriceRepository.save(PackPrice("ароматная древесина махадеби","полуостров рассвета",Price(9,24,29)))
        itemPriceRepository.save(PackPrice("ароматная древесина махадеби","поющая земля",Price(9,24,8)))
        itemPriceRepository.save(PackPrice("ароматная древесина махадеби","инистра",Price(12,1,95)))

        itemPriceRepository.save(PackPrice("пуховик для лица","поющая земля",Price(20,39,55)))
        itemPriceRepository.save(PackPrice("пуховик для лица","инистра",Price(23,49,1)))

        itemPriceRepository.save(PackPrice("притирания для кожи","поющая земля",Price(9,9,63)))
        itemPriceRepository.save(PackPrice("притирания для кожи","инистра",Price(10,88,66)))

        itemPriceRepository.save(PackPrice("ароматические подушечки","полуостров рассвета",Price(20,39,31)))
        itemPriceRepository.save(PackPrice("ароматические подушечки","инистра",Price(22,20,37)))

        itemPriceRepository.save(PackPrice("терпкое вишневое вино","полуостров рассвета",Price(9,9,63)))
        itemPriceRepository.save(PackPrice("терпкое вишневое вино","инистра",Price(10,37,97)))

        itemPriceRepository.save(PackPrice("фруктовое молоко","полуостров рассвета",Price(26,7,40)))
        itemPriceRepository.save(PackPrice("фруктовое молоко","поющая земля",Price(24,48,11)))
        itemPriceRepository.save(PackPrice("фруктовое молоко","инистра",Price(26,66,52)))

        itemPriceRepository.save(PackPrice("смесь специй для вина","полуостров рассвета",Price(12,42,15)))
        itemPriceRepository.save(PackPrice("смесь специй для вина","поющая земля",Price(10,14,19)))
        itemPriceRepository.save(PackPrice("смесь специй для вина","инистра",Price(12,52,75)))

        itemPriceRepository.save(PackPrice("апельсиновый джем","полуостров рассвета",Price(29,8,33)))
        itemPriceRepository.save(PackPrice("апельсиновый джем","поющая земля",Price(27,49,3)))

        itemPriceRepository.save(PackPrice("оливкое масло","полуостров рассвета",Price(13,47,87)))
        itemPriceRepository.save(PackPrice("оливкое масло","поющая земля",Price(12,85,10)))

        itemPriceRepository.save(PackPrice("фруктовое печень","полуостров рассвета",Price(29,63,36)))
        itemPriceRepository.save(PackPrice("фруктовое печень","поющая земля",Price(28,0,98)))
        itemPriceRepository.save(PackPrice("фруктовое печень","инистра",Price(30,23,63)))

        itemPriceRepository.save(PackPrice("сушеные яблочные дольки","полуостров рассвета",Price(13,73,55)))
        itemPriceRepository.save(PackPrice("сушеные яблочные дольки","поющая земля",Price(13,9,56)))
        itemPriceRepository.save(PackPrice("сушеные яблочные дольки","инистра",Price(13,98,39)))

        itemPriceRepository.save(PackPrice("вяленые припасаы саванны","полуостров рассвета",Price(28,89,38)))
        itemPriceRepository.save(PackPrice("вяленые припасаы саванны","поющая земля",Price(28,89,0)))
        itemPriceRepository.save(PackPrice("вяленые припасаы саванны","инистра",Price(31,10,82)))

        itemPriceRepository.save(PackPrice("мятные леденцы","полуостров рассвета",Price(13,44,48)))
        itemPriceRepository.save(PackPrice("мятные леденцы","поющая земля",Price(13,43,89)))
        itemPriceRepository.save(PackPrice("мятные леденцы","инистра",Price(14,32,46)))

        itemPriceRepository.save(PackPrice("пряности харихараллы","полуостров рассвета",Price(31,87,83)))
        itemPriceRepository.save(PackPrice("пряности харихараллы","поющая земля",Price(31,88,15)))
        itemPriceRepository.save(PackPrice("пряности харихараллы","инистра",Price(34,9,28)))

        itemPriceRepository.save(PackPrice("сладкий картофель","полуостров рассвета",Price(14,62,99)))
        itemPriceRepository.save(PackPrice("сладкий картофель","поющая земля",Price(14,62,89)))
        itemPriceRepository.save(PackPrice("сладкий картофель","инистра",Price(15,51,42)))

        itemPriceRepository.save(PackPrice("хазирские лечебные перины","полуостров рассвета",Price(34,96,29)))
        itemPriceRepository.save(PackPrice("хазирские лечебные перины","поющая земля",Price(31,89,75)))
        itemPriceRepository.save(PackPrice("хазирские лечебные перины","инистра",Price(34,12,42)))

        itemPriceRepository.save(PackPrice("вяленые припасы хазиры","полуостров рассвета",Price(15,85,36)))
        itemPriceRepository.save(PackPrice("вяленые припасы хазиры","поющая земля",Price(14,63,68)))
        itemPriceRepository.save(PackPrice("вяленые припасы хазиры","инистра",Price(15,52,48)))

        itemPriceRepository.save(PackPrice("лекарственный сироп","полуостров рассвета",Price(26,5,47)))
        itemPriceRepository.save(PackPrice("лекарственный сироп","поющая земля",Price(26,5,11)))
        itemPriceRepository.save(PackPrice("лекарственный сироп","инистра",Price(29,88,56)))

        itemPriceRepository.save(PackPrice("лавандовый чай","полуостров рассвета",Price(12,41,28)))
        itemPriceRepository.save(PackPrice("лавандовый чай","поющая земля",Price(12,40,60)))
        itemPriceRepository.save(PackPrice("лавандовый чай","инистра",Price(13,79,44)))

        itemPriceRepository.save(PackPrice("сушоные яблоки","полуостров рассвета",Price(30,44,34)))
        itemPriceRepository.save(PackPrice("сушоные яблоки","поющая земля",Price(28,81,96)))
        itemPriceRepository.save(PackPrice("сушоные яблоки","инистра",Price(31,4,66)))

        itemPriceRepository.save(PackPrice("душистый лосьон","полуостров рассвета",Price(14,5,50)))
        itemPriceRepository.save(PackPrice("душистый лосьон","поющая земля",Price(13,41,48)))
        itemPriceRepository.save(PackPrice("душистый лосьон","инистра",Price(14,30,32)))

        itemPriceRepository.save(PackPrice("груз компоста из радужных песков","полуостров рассвета",Price(12,58,72)))
        itemPriceRepository.save(PackPrice("груз компоста из радужных песков","поющая земля",Price(12,58,46)))
        itemPriceRepository.save(PackPrice("груз компоста из радужных песков","инистра",Price(15,98,54)))

        itemPriceRepository.save(PackPrice("груз компоста с плато соколиной охоты","полуостров рассвета",Price(12,92,8)))
        itemPriceRepository.save(PackPrice("груз компоста с плато соколиной охоты","поющая земля",Price(9,95,36)))
        itemPriceRepository.save(PackPrice("груз компоста с плато соколиной охоты","инистра",Price(14,24,49)))

        itemPriceRepository.save(PackPrice("груз компоста с тигриного хребета","полуостров рассвета",Price(11,42,86)))
        itemPriceRepository.save(PackPrice("груз компоста с тигриного хребета","поющая земля",Price(11,42,62)))
        itemPriceRepository.save(PackPrice("груз компоста с тигриного хребета","инистра",Price(14,56,4)))

        itemPriceRepository.save(PackPrice("груз компоста с махадеби","полуостров рассвета",Price(9,93,57)))
        itemPriceRepository.save(PackPrice("груз компоста с махадеби","поющая земля",Price(9,93,31)))
        itemPriceRepository.save(PackPrice("груз компоста с махадеби","инистра",Price(14,23,38)))

        itemPriceRepository.save(PackPrice("груз компоста с полуострова рассвета","поющая земля",Price(10,12,9)))
        itemPriceRepository.save(PackPrice("груз компоста с полуострова рассвета","инистра",Price(12,89,2)))

        itemPriceRepository.save(PackPrice("груз компоста из поющей земли","полуостров рассвета",Price(10,12,9)))
        itemPriceRepository.save(PackPrice("груз компоста из поющей земли","инистра",Price(12,29,17)))

        itemPriceRepository.save(PackPrice("груз компоста из древнего леса","полуостров рассвета",Price(14,22,24)))
        itemPriceRepository.save(PackPrice("груз компоста из древнего леса","поющая земля",Price(10,90,17)))
        itemPriceRepository.save(PackPrice("груз компоста из древнего леса","инистра",Price(14,83,52)))

        itemPriceRepository.save(PackPrice("груз компоста из инистры","полуостров рассвета",Price(15,96,23)))
        itemPriceRepository.save(PackPrice("груз компоста из инистры","поющая земля",Price(15,21,84)))

        itemPriceRepository.save(PackPrice("груз компоста из рокочущих перевалов","полуостров рассвета",Price(16,26,58)))
        itemPriceRepository.save(PackPrice("груз компоста из рокочущих перевалов","поющая земля",Price(15,50,95)))
        itemPriceRepository.save(PackPrice("груз компоста из рокочущих перевалов","инистра",Price(16,40,87)))

        itemPriceRepository.save(PackPrice("груз компоста из саванны","полуостров рассвета",Price(15,92,15)))
        itemPriceRepository.save(PackPrice("груз компоста из саванны","поющая земля",Price(15,91,47)))
        itemPriceRepository.save(PackPrice("груз компоста из саванны","инистра",Price(16,81,19)))

        itemPriceRepository.save(PackPrice("груз компоста из руин харихараллы","полуостров рассвета",Price(17,17,4)))
        itemPriceRepository.save(PackPrice("груз компоста из руин харихараллы","поющая земля",Price(17,17,22)))
        itemPriceRepository.save(PackPrice("груз компоста из руин харихараллы","инистра",Price(18,6,96)))

        itemPriceRepository.save(PackPrice("груз компоста из хазиры","полуостров рассвета",Price(18,47,16)))
        itemPriceRepository.save(PackPrice("груз компоста из хазиры","поющая земля",Price(17,18,13)))
        itemPriceRepository.save(PackPrice("груз компоста из хазиры","инистра",Price(18,8,23)))

        itemPriceRepository.save(PackPrice("груз компоста из багрового каньона","полуостров рассвета",Price(14,20,85)))
        itemPriceRepository.save(PackPrice("груз компоста из багрового каньона","поющая земля",Price(14,20,59)))
        itemPriceRepository.save(PackPrice("груз компоста из багрового каньона","инистра",Price(16,33,58)))

        itemPriceRepository.save(PackPrice("груз компоста из долиных талых снегов","полуостров рассвета",Price(16,64,36)))
        itemPriceRepository.save(PackPrice("груз компоста из долиных талых снегов","поющая земля",Price(15,88,62)))
        itemPriceRepository.save(PackPrice("груз компоста из долиных талых снегов","инистра",Price(16,78,69)))

        itemPriceRepository.save(PackPrice("сушеные семечки","полуостров рассвета",Price(11,99,8)))
        itemPriceRepository.save(PackPrice("сушеные семечки","поющая земля",Price(11,98,84)))
        itemPriceRepository.save(PackPrice("сушеные семечки","инистра",Price(14,84,88)))

        itemPriceRepository.save(PackPrice("компост","полуостров рассвета",Price(12,39,94)))
        itemPriceRepository.save(PackPrice("компост","поющая земля",Price(9,18,38)))
        itemPriceRepository.save(PackPrice("компост","инистра",Price(13,23,21)))

        itemPriceRepository.save(PackPrice("фаршированные помидоры","полуостров рассвета",Price(10,88,75)))
        itemPriceRepository.save(PackPrice("фаршированные помидоры","поющая земля",Price(10,89,0)))
        itemPriceRepository.save(PackPrice("фаршированные помидоры","инистра",Price(13,52,51)))

        itemPriceRepository.save(PackPrice("соленые огурчики","полуостров рассвета",Price(9,38,35)))
        itemPriceRepository.save(PackPrice("соленые огурчики","поющая земля",Price(9,38,49)))
        itemPriceRepository.save(PackPrice("соленые огурчики","инистра",Price(13,22,15)))

        itemPriceRepository.save(PackPrice("толченый шафран","поющая земля",Price(9,64,93)))
        itemPriceRepository.save(PackPrice("толченый шафран","инистра",Price(11,97,51)))

        itemPriceRepository.save(PackPrice("рисовая лапша","полуостров рассвета",Price(9,64,93)))
        itemPriceRepository.save(PackPrice("рисовая лапша","инистра",Price(11,41,76)))

        itemPriceRepository.save(PackPrice("чесночная приправа","полуостров рассвета",Price(13,63,85)))
        itemPriceRepository.save(PackPrice("чесночная приправа","поющая земля",Price(10,29,60)))
        itemPriceRepository.save(PackPrice("чесночная приправа","инистра",Price(13,78,3)))

        itemPriceRepository.save(PackPrice("букеты роз","полуостров рассвета",Price(14,82,65)))
        itemPriceRepository.save(PackPrice("букеты роз","поющая земля",Price(14,13,63)))

        itemPriceRepository.save(PackPrice("просяная мука","полуостров рассвета",Price(15,11,47)))
        itemPriceRepository.save(PackPrice("просяная мука","поющая земля",Price(14,40,53)))
        itemPriceRepository.save(PackPrice("просяная мука","инистра",Price(15,38,23)))

        itemPriceRepository.save(PackPrice("настойка алоэ","полуостров рассвета",Price(14,78,27)))
        itemPriceRepository.save(PackPrice("настойка алоэ","поющая земля",Price(14,78,27)))
        itemPriceRepository.save(PackPrice("настойка алоэ","инистра",Price(15,75,69)))

        itemPriceRepository.save(PackPrice("вяленые припаса харихараллы","полуостров рассвета",Price(16,9,30)))
        itemPriceRepository.save(PackPrice("вяленые припаса харихараллы","поющая земля",Price(16,9,16)))
        itemPriceRepository.save(PackPrice("вяленые припаса харихараллы","инистра",Price(17,6,57)))

        itemPriceRepository.save(PackPrice("ароматный чай","полуостров рассвета",Price(17,43,92)))
        itemPriceRepository.save(PackPrice("ароматный чай","поющая земля",Price(16,10,3)))
        itemPriceRepository.save(PackPrice("ароматный чай","инистра",Price(17,7,73)))

        itemPriceRepository.save(PackPrice("мятные рогалики","полуостров рассвета",Price(13,62,45)))
        itemPriceRepository.save(PackPrice("мятные рогалики","поющая земля",Price(13,62,19)))
        itemPriceRepository.save(PackPrice("мятные рогалики","инистра",Price(15,17,39)))

        itemPriceRepository.save(PackPrice("оладьи из батата","полуостров рассвета",Price(15,46,4)))
        itemPriceRepository.save(PackPrice("оладьи из батата","поющая земля",Price(14,75,63)))
        itemPriceRepository.save(PackPrice("оладьи из батата","инистра",Price(15,73,37)))

        itemPriceRepository.save(PackPrice("крем с экстрактом бамбука","полуостров рассвета",Price(12,12,9)))
        itemPriceRepository.save(PackPrice("крем с экстрактом бамбука","поющая земля",Price(12,14,8)))
        itemPriceRepository.save(PackPrice("крем с экстрактом бамбука","инистра",Price(12,6,13)))

        itemPriceRepository.save(PackPrice("пальмовое масло","полуостров рассвета",Price(10,88,74)))
        itemPriceRepository.save(PackPrice("пальмовое масло","поющая земля",Price(10,83,60)))
        itemPriceRepository.save(PackPrice("пальмовое масло","инистра",Price(10,67,96)))

        itemPriceRepository.save(PackPrice("лакированные фигурки слонов","полуостров рассвета",Price(61,13,92)))
        itemPriceRepository.save(PackPrice("лакированные фигурки слонов","поющая земля",Price(60,89,92)))
        itemPriceRepository.save(PackPrice("лакированные фигурки слонов","инистра",Price(68,54,65)))

        itemPriceRepository.save(PackPrice("глянцевые сферы с живыми светлячками","полуостров рассвета",Price(64,76,36)))
        itemPriceRepository.save(PackPrice("глянцевые сферы с живыми светлячками","поющая земля",Price(62,56,20)))
        itemPriceRepository.save(PackPrice("глянцевые сферы с живыми светлячками","инистра",Price(65,59,2)))

        itemPriceRepository.save(PackPrice("расписные фигурки львов","полуостров рассвета",Price(64,66,26)))
        itemPriceRepository.save(PackPrice("расписные фигурки львов","поющая земля",Price(62,53,79)))
        itemPriceRepository.save(PackPrice("расписные фигурки львов","инистра",Price(68,61,56)))

        itemPriceRepository.save(PackPrice("чашки из белого глазурованного фарфора","полуостров рассвета",Price(70,40,37)))
        itemPriceRepository.save(PackPrice("чашки из белого глазурованного фарфора","поющая земля",Price(66,32,56)))

        itemPriceRepository.save(PackPrice("глянцевитые подвески-арованы","полуостров рассвета",Price(73,62,17)))
        itemPriceRepository.save(PackPrice("глянцевитые подвески-арованы","поющая земля",Price(73,58,6)))
        itemPriceRepository.save(PackPrice("глянцевитые подвески-арованы","инистра",Price(76,96,14)))

        itemPriceRepository.save(PackPrice("груз мерцающие фонари","полуостров рассвета",Price(28,97,65)))
        itemPriceRepository.save(PackPrice("груз мерцающие фонари","поющая земля",Price(26,23,5)))

        itemPriceRepository.save(PackPrice("груз белоснежного меха","полуостров рассвета",Price(37,62,74)))
        itemPriceRepository.save(PackPrice("груз белоснежного меха","поющая земля",Price(19,95,25)))
        itemPriceRepository.save(PackPrice("груз белоснежного меха","инистра",Price(19,17,55)))

        itemPriceRepository.save(PackPrice("груз шаманских амулетов","полуостров рассвета",Price(32,16,4)))
        itemPriceRepository.save(PackPrice("груз шаманских амулетов","поющая земля",Price(31,98,9)))
        itemPriceRepository.save(PackPrice("груз шаманских амулетов","инистра",Price(40,6,70)))

        itemPriceRepository.save(PackPrice("груз резных оберегов","полуостров рассвета",Price(37,69,75)))
        itemPriceRepository.save(PackPrice("груз резных оберегов","поющая земля",Price(37,52,24)))
        itemPriceRepository.save(PackPrice("груз резных оберегов","инистра",Price(36,36,70)))

        itemPriceRepository.save(PackPrice("груз декоративного оружия","полуостров рассвета",Price(47,72,15)))
        itemPriceRepository.save(PackPrice("груз декоративного оружия","поющая земля",Price(47,34,35)))
        itemPriceRepository.save(PackPrice("груз декоративного оружия","инистра",Price(46,56,30)))

        PriceArcheageServerContextHolder.clear()
    }


}