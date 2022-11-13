package io.github.excu101.filesystem.fs.operation.option

object Options {
    // Options for open file operation
    object Open {
        val Write = object : WriteOpenOption {}
        val Read = object : ReadOpenOption {}
        val Create = object : CreateOpenOption {}
        val CreateNew = object : CreateNewOpenOption {}
        val Append = object : AppendOpenOption {}
    }
}