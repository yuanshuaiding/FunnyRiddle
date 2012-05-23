LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := myparsexml
LOCAL_SRC_FILES := myparsexml.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
