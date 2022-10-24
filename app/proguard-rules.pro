-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keep class io.github.excu101.filesystem.unix.** { *; }
-keep class java.io.FileDescriptor { *; }