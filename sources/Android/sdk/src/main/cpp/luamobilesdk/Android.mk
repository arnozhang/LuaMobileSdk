LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CPPFLAGS := --std=c++11
# LOCAL_ALLOW_UNDEFINED_SYMBOLS := true

SRC_LIST    := $(wildcard $(LOCAL_PATH)/src/*.cc)

LOCAL_C_INCLUDES += $(LOCAL_PATH)/../lua/src

LOCAL_MODULE     := luamobilesdk
LOCAL_SRC_FILES  := $(SRC_LIST:$(LOCAL_PATH)/%=%)


LOCAL_STATIC_LIBRARIES := lua
include $(BUILD_SHARED_LIBRARY)
