/*
 * myparsexml.c
 * 
 * Author: Frank Ableson
 * Contact Info: fableson@navitend.com
 */

#include <jni.h>
#include <android/log.h>

#define  LOG_TAG    "my xml parser"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


/*
parse xml
*/
JNIEXPORT jstring JNICALL Java_com_eric_riddle_activities_RiddleListActivity_parseXml( JNIEnv* env,jobject thiz ,jstring str)
{
    return str;//(*env)->NewStringUTF(env, str)
}

