package io.github.excu101.filesystem.fs.operation.option

import io.github.excu101.filesystem.fs.operation.FileOperation

interface OpenOption : FileOperation.Option

interface WriteOpenOption : OpenOption
interface ReadOpenOption : OpenOption
interface CreateOpenOption : OpenOption
interface CreateNewOpenOption : OpenOption
interface AppendOpenOption : OpenOption