package io.github.excu101.filesystem.fs.operation.option

// Options for open file operation
object Options {

    const val Empty = 0

    object Open {
        const val Write = 1
        const val Read = 2
        const val Create = 4
        const val CreateNew = 8
        const val Append = 16
        const val Async = 32
        const val CloseOnExit = 64

        const val All = Write or Read or Create or CreateNew or Append or Async or CloseOnExit
    }

    object Copy {
        const val NoFollowLinks = 32
        const val ReplaceExists = 64

        const val All = NoFollowLinks or ReplaceExists
    }
}