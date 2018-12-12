LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := QcRilAm
LOCAL_CERTIFICATE := platform
LOCAL_MODULE_TAGS := optional
LOCAL_PROPRIETARY_MODULE := true

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_JAVA_LIBRARIES :=	android.hidl.manager-V1.0-java
LOCAL_STATIC_JAVA_LIBRARIES := vendor.qti.hardware.radio.am-V1.0-java

LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_PRIVILEGED_MODULE := true

include $(BUILD_PACKAGE)
