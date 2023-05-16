package io.github.excu101.filesystem.fs.utils

import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import kotlin.reflect.KProperty

operator fun String.getValue(source: Any?, property: KProperty<*>): MimeType {
    return MimeType.from(extension = this)
}

inline val MimeType.isApplicationType
    get() = type == MimeType.TYPE_APP

inline val MimeType.isTextType
    get() = type == MimeType.TYPE_TEXT

inline val MimeType.isVideoType
    get() = type == MimeType.TYPE_VIDEO

inline val MimeType.isAudioType
    get() = type == MimeType.TYPE_AUDIO

inline val MimeType.isImageType
    get() = type == MimeType.TYPE_IMAGE

val EmptyMimeType = object : MimeType {
    override val extension: String
        get() = ""
    override val internetMediaType: String
        get() = ""
    override val isNotStandard: Boolean
        get() = false
    override val isVendor: Boolean
        get() = false
    override val type: Int
        get() = MimeType.TYPE_EMPTY

    override fun toString(): String = ""
}

val videoMimeTypes = mapOf(
    "3gp" to "video/3gpp",
    "3gp2" to "video/3gpp2",
    "jpm" to "video/jpm",
    "mp4" to "video/mp4",
    "mp4v" to "video/mp4",
    "mpg4" to "video/mp4",
)

val imageMimeTypes = mapOf(
    "svg" to "image/svg+xml",
    "jpg" to "image/jpeg",
    "jpeg" to "image/jpeg",
    "pjpeg" to "image/pjpeg",
)

val applicationMimeTypes = mapOf(
    "x3d" to "application/vnd.hzn-3d-crossword",
    "mseq" to "application/vnd.mseq",
    "pwn" to "application/vnd.3m.post-it-notes",
    "plb" to "application/vnd.3gpp.pic-bw-large",
    "psb" to "application/vnd.3gpp.pic-bw-small",
    "pvb" to "application/vnd.3gpp.pic-bw-var",
    "tcap" to "application/vnd.3gpp2.tcap",
    "7z" to "application/x-7z-compressed",
    "abw" to "application/x-abiword",
    "ace" to "application/x-ace-compressed",
    "acc" to "application/vnd.americandynamics.acc",
    "acu" to "application/vnd.acucobol",
    "atc" to "application/vnd.acucorp",
    "aab" to "application/x-authorware-bin",
    "aam" to "application/x-authorware-map",
    "aas" to "application/x-authorware-seg",
    "air" to "application/vnd.adobe.air-application-installer-package+zip",
    "swf" to "application/x-shockwave-flash",
    "fxp" to "application/vnd.adobe.fxp",
    "pdf" to "application/pdf",
    "ppd" to "application/vnd.cups-ppd",
    "dir" to "application/x-director",
    "xdp" to "application/vnd.adobe.xdp+xml",
    "xfdf" to "application/vnd.adobe.xfdf",
    "ahead" to "application/vnd.ahead.space",
    "azf" to "application/vnd.airzip.filesecure.azf",
    "azs" to "application/vnd.airzip.filesecure.azs",
    "azw" to "application/vnd.amazon.ebook",
    "ami" to "application/vnd.amiga.ami",
    "apk" to "application/vnd.android.package-archive",
    "cii" to "application/vnd.anser-web-certificate-issue-initiation",
    "fti" to "application/vnd.anser-web-funds-transfer-initiation",
    "atx" to "application/vnd.antix.game-component",
    "dmg" to "application/x-apple-diskimage",
    "mpkg" to "application/vnd.apple.installer+xml",
    "aw" to "application/applixware",
    "les" to "application/vnd.hhe.lesson-player",
    "swi" to "application/vnd.aristanetworks.swi",
    "atomcat" to "application/atomcat+xml",
    "atomsvc" to "application/atomsvc+xml",
    "rss" to "application/rss+xml",
    "shf" to "application/shf+xml",
    "st" to "application/vnd.sailingtracker.track",
    "sus" to "application/vnd.sus-calendar",
    "sru" to "application/sru+xml",
    "setpay" to "application/set-payment-initiation",
    "setreg" to "application/set-registration-initiation",
    "sema" to "application/vnd.sema",
    "semd" to "application/vnd.semd",
    "semf" to "application/vnd.semf",
    "see" to "application/vnd.seemail",
    "snf" to "application/x-font-snf",
    "spq" to "application/scvp-vp-request",
    "spp" to "application/scvp-vp-response",
    "mpg4s" to "video/mp4",
)

val audioMimeTypes = mapOf(
    "adp" to "audio/adpcm",
    "aac" to "audio/x-aac",
    "mp3" to "audio/mp3"
)

val textMimeType = mapOf(
    "s" to "text/x-asm",
    "java" to "text/x-java-source",
    "txt" to "text/plain"
)