package fr.misterassm.kubbit.api.dsl

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import java.util.function.Consumer

class ItemStackBuilder(
    var type: Material,
    var amount: Int = 1,
    var durability: Short = 0,
) {

    var name: String? = null
    var lore: List<String> = emptyList()
    var color: Color? = null
    //var breakable = true
    var enchantments = mutableMapOf<Enchantment, Int>()
    var flags = mutableSetOf<ItemFlag>()

    constructor(itemStack: ItemStack) : this(itemStack.type, itemStack.amount, itemStack.durability) {
        itemStack.itemMeta?.let {
            this.name = it.displayName
            this.lore = it.lore
            this.color = (it as LeatherArmorMeta?)?.color
            //this.breakable = !it.isUnbreakable
            this.enchantments = it.enchants
            this.flags = it.itemFlags
        }
    }

    fun clearLore() {
        lore = emptyList()
    }

    fun enchant(enchantment: Enchantment, level: Int = 1) {
        this.enchantments + (enchantment to level)
    }

    fun clearEnchantments() {
        this.enchantments = mutableMapOf()
    }

    fun flag(vararg flags: ItemFlag) {
        this.flags.plus(flags)
    }

    fun unflag(vararg flags: ItemFlag) {
        this.flags.minus(flags)
    }

    private fun ItemStack.applyItemMeta(consumer: Consumer<ItemMeta>) =
        this.itemMeta?.let { consumer.accept(this.itemMeta) }

    internal fun build() = ItemStack(type, amount, durability).also { itemStack ->
        itemStack.applyItemMeta { itemMeta ->
            name?.let { itemMeta.displayName = it }
            color.takeIf { type == Material.LEATHER_HELMET || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_LEGGINGS || type == Material.LEATHER_BOOTS }
                ?.let { (itemMeta as LeatherArmorMeta).apply { color = it } }

            flags.forEach { itemMeta.addItemFlags(it) }
            enchantments.forEach { (enchantment, level) -> itemMeta.addEnchant(enchantment, level, true) }

            itemMeta.lore = lore
            itemStack.itemMeta = itemMeta

            // TODO: 15/05/2021 BREAKABLE LOGIC
        }
    }

}

fun itemStack(
    type: Material,
    amount: Int = 1,
    data: Short = 0,
    init: ItemStackBuilder.() -> Unit = {},
) = ItemStackBuilder(type, amount, data).apply(init).build()
