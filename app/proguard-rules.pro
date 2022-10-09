-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keep class io.github.excu101.filesystem.unix.** { *; }
-keep class io.github.excu101.filesystem.unix.attr.UnixStatusStructure { *;}
-keep class io.github.excu101.filesystem.unix.attr.UnixDirentStructure { *;}
-keep class io.github.excu101.filesystem.unix.error.UnixException { *;}
-keep class java.io.FileDescriptor { *; }