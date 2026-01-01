# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep public class com.example.traveling_app.model.comment.Comment
-keep public class com.example.traveling_app.model.notification.Notification
-keep public class com.example.traveling_app.model.post.Post
-keep public class com.example.traveling_app.model.savedtours.SavedTour
-keep public class com.example.traveling_app.model.tour.Discount
-keep public class com.example.traveling_app.model.tour.HistoryTour
-keep public class com.example.traveling_app.model.tour.Review
-keep public class com.example.traveling_app.model.tour.Tour
# When using android mail, keep those classes used by reflection
-keep class javax.mail.** { *; }
-keep class com.sun.mail.** { *; }
-keep class myjava.awt.datatransfer.** { *; }
-keep class org.apache.harmony.awt.** { *; }
# Facebook SDK
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepnames class com.facebook.FacebookActivity
-keepnames class com.facebook.CustomTabActivity

-keep class com.facebook.login.Login