#include "../../jni/jni_classes.cpp"
#include "sys/inotify.h"
#include <cerrno>
#include <cstdlib>
#include <string>
#include "sys/poll.h"

static void clearErrno() {
    errno = 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixObserverCalls_initServiceImpl(
        JNIEnv *env,
        jobject thiz,
        jint flags
) {
    clearErrno();
    int descriptor = inotify_init1(flags);

    if (errno != 0) {

    }

    return descriptor;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixObserverCalls_addObservableImpl(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jbyteArray path,
        jint mask
) {
    clearErrno();
    char *cPath = fromByteArrayToPath(env, path);

    if (errno != 0) {

    }

    // Returns observable
    return inotify_add_watch(descriptor, cPath, (uint32_t) mask);
}


extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixObserverCalls_removeObservableImpl(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jint observable
) {
    clearErrno();
    inotify_rm_watch(descriptor, (uint32_t) observable);

    if (errno != 0) {

    }
}

static jobject createUnixPathObservableEvent(JNIEnv *env, const struct inotify_event *event) {
    static jmethodID constructor = nullptr;
    if (constructor == nullptr) {
        constructor = findUnixPathObservableEventInitMethod(env);
    }

    jint observable = event->wd;
    jint mask = event->mask;
    jint cookie = event->cookie;
    jbyteArray name = createByteArray(env, (char *) event->name);

    return env->NewObject(
            findUnixPathObservableEventClass(env),
            constructor,
            observable,
            mask,
            cookie,
            name
    );
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixObserverCalls_getEventsForObservableImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray buffer,
        jint offset,
        jint length
) {
    void *cBuffer = env->GetByteArrayElements(buffer, nullptr);
    size_t cOffset = offset;
    size_t cLength = length;
    char *bufferStart = (char *) buffer + cOffset;
    char *bufferEnd = bufferStart + cLength;
    jsize javaEventsLength = 0;
    for (char *eventStart = bufferStart; eventStart < bufferEnd;) {
        auto *event = (struct inotify_event *) eventStart;
        ++javaEventsLength;
        eventStart += sizeof(struct inotify_event) + event->len;
    }

    jobjectArray javaEvents = env->NewObjectArray(
            javaEventsLength,
            findUnixPathObservableEventClass(env),
            NULL
    );

    if (!javaEvents) {
        env->ReleaseByteArrayElements(buffer, (jbyte *) cBuffer, JNI_ABORT);
        return nullptr;
    }
    jsize javaIndex = 0;
    for (char *eventStart = bufferStart; eventStart < bufferEnd;) {
        auto *event = (struct inotify_event *) eventStart;
        jobject javaEvent = createUnixPathObservableEvent(env, event);
        if (!javaEvent) {
            env->DeleteLocalRef(javaEvents);
            env->ReleaseByteArrayElements(buffer, (jbyte *) cBuffer, JNI_ABORT);
            return nullptr;
        }
        env->SetObjectArrayElement(javaEvents, javaIndex, javaEvent);
        env->DeleteLocalRef(javaEvent);
        ++javaIndex;
        eventStart += sizeof(struct inotify_event) + event->len;
    }
    env->ReleaseByteArrayElements(buffer, (jbyte *) cBuffer, JNI_ABORT);
    return javaEvents;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixObserverCalls_waitEventsForObservableImpl(
        JNIEnv *env, jobject thiz,
        jobjectArray descriptors,
        jint timeout
) {
    static jfieldID descriptorId = env->GetFieldID(
            findClass(
                    env,
                    "io/github/excu101/filesystem/unix/structure/UnixPollFileDescriptorStructure"
            ),
            "descriptor",
            "Ljava/io/FileDescriptor;"
    );

    static jfieldID eventId = env->GetFieldID(
            findClass(
                    env,
                    "io/github/excu101/filesystem/unix/structure/UnixPollFileDescriptorStructure"
            ),
            "events",
            "S"
    );

    static jfieldID reventsId = env->GetFieldID(
            findClass(
                    env,
                    "io/github/excu101/filesystem/unix/structure/UnixPollFileDescriptorStructure"
            ),
            "revents",
            "S"
    );
    size_t len = env->GetArrayLength(descriptors);
    std::unique_ptr<struct pollfd[]> fds(new struct pollfd[len]);
    memset(fds.get(), 0, sizeof(struct pollfd) * len);
    size_t count = 0;
    for (int i = 0; i < len; ++i) {
//        Thanks Kotlin for Null-safety
        jobject element = env->GetObjectArrayElement(descriptors, i);
//        if (element == nullptr) break;
        jobject elementDescriptor = env->GetObjectField(element, descriptorId);
//        if (elementDescriptor == nullptr) break;
        fds[count].fd = getIndexFromFileDescriptor(env, elementDescriptor);
        fds[count].events = env->GetShortField(element, eventId);
        ++count;
    }
    int result;
    for (;;) {
        timespec before{};
        clock_gettime(CLOCK_MONOTONIC, &before);
        result = poll(fds.get(), count, timeout);
        if (result >= 0 || errno != EINTR) {
            break;
        }
        // We got EINTR. Work out how much of the original timeout is still left.
        if (timeout > 0) {
            timespec now{};
            clock_gettime(CLOCK_MONOTONIC, &now);
            timespec diff{};
            diff.tv_sec = now.tv_sec - before.tv_sec;
            diff.tv_nsec = now.tv_nsec - before.tv_nsec;
            if (diff.tv_nsec < 0) {
                --diff.tv_sec;
                diff.tv_nsec += 1000000000;
            }
            jint diffMs = diff.tv_sec * 1000 + diff.tv_nsec / 1000000;
            if (diffMs >= timeout) {
                result = 0;
                break;
            }
            timeout -= diffMs;
        }
    }

    if (result == -1) {
        throwUnixException(env, errno, "poll");
        return -1;
    }


    for (size_t i = 0; i < count; ++i) {
        jobject element = env->GetObjectArrayElement(descriptors, i);
        env->SetShortField(element, reventsId, fds[i].revents);
    }

    return result;
}