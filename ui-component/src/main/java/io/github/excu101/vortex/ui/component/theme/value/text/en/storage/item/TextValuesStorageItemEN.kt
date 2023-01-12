package io.github.excu101.vortex.ui.component.theme.value.text.en.storage.item

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.integerSpecifier
import io.github.excu101.vortex.ui.component.theme.key.stringSpecifier
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemCountKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemEmptyKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemInfoSeparatorKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemMimeTypeApplicationKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemMimeTypeAudioKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemMimeTypeImageKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemMimeTypeTextKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemMimeTypeVideoKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemNameKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeEiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeGiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeKiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeMiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizePiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeTiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeYiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemSizeZiBKey
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemsCountKey

fun initStorageItemValuesEN() {
    // Item : MimeType
    Theme[fileListItemMimeTypeApplicationKey] = Text(value = "Application")
    Theme[fileListItemMimeTypeImageKey] = Text(value = "Image")
    Theme[fileListItemMimeTypeVideoKey] = Text(value = "Video")
    Theme[fileListItemMimeTypeAudioKey] = Text(value = "Audio")
    Theme[fileListItemMimeTypeTextKey] = Text(value = "Text")

    // Item : Size
    Theme[fileListItemSizeBKey] = Text(value = "B")
    Theme[fileListItemSizeKiBKey] = Text(value = "KB")
    Theme[fileListItemSizeMiBKey] = Text(value = "MB")
    Theme[fileListItemSizeGiBKey] = Text(value = "GB")
    Theme[fileListItemSizeTiBKey] = Text(value = "TB")
    Theme[fileListItemSizePiBKey] = Text(value = "PB")
    Theme[fileListItemSizeEiBKey] = Text(value = "EB")
    Theme[fileListItemSizeZiBKey] = Text(value = "ZB")
    Theme[fileListItemSizeYiBKey] = Text(value = "YB")

    // Item
    Theme[fileListItemNameKey] = Text(value = stringSpecifier)
    Theme[fileListItemSizeKey] = Text(value = stringSpecifier)
    Theme[fileListItemInfoSeparatorKey] = Text(value = " | ")

    // Item : Directory Content
    Theme[fileListItemsCountKey] = Text(value = "$integerSpecifier items")
    Theme[fileListItemCountKey] = Text(value = "One item")
    Theme[fileListItemEmptyKey] = Text(value = "Empty")
}