# keeping line number info during debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# for firebase auth
-keepattributes Signature
-keepattributes *Annotation*

# for model class serializing
-keep class me.arblitroshani.dentalclinic.model.** { *; }