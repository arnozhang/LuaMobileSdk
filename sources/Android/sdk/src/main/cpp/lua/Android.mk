LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

SRC_LIST    := $(wildcard $(LOCAL_PATH)/src/*.c)

LOCAL_MODULE    := lua
LOCAL_SRC_FILES := $(SRC_LIST:$(LOCAL_PATH)/%=%)

include $(BUILD_STATIC_LIBRARY)
