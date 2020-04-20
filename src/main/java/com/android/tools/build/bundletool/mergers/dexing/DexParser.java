package com.android.tools.build.bundletool.mergers.dexing;

import java.util.regex.Pattern;

/**
 * Created by YangJing on 2020/04/20 .
 * Email: yangjing.yeoh@bytedance.com
 */
class DexParser {
  public static final String DX_UNEXPECTED_EXCEPTION = "UNEXPECTED TOP-LEVEL EXCEPTION:";
  public static final String ERROR_INVOKE_DYNAMIC = "invalid opcode ba - invokedynamic requires --min-sdk-version >= 26";
  public static final String ENABLE_DESUGARING = "The dependency contains Java 8 bytecode. Please enable desugaring by adding the following to build.gradle\nandroid {\n    compileOptions {\n        sourceCompatibility 1.8\n        targetCompatibility 1.8\n    }\n}\nSee https://developer.android.com/studio/write/java8-support.html for details. Alternatively, increase the minSdkVersion to 26 or above.\n";
  public static final String DEX_LIMIT_EXCEEDED_ERROR = "The number of method references in a .dex file cannot exceed 64K.\nLearn how to resolve this issue at https://developer.android.com/tools/building/multidex.html";
  static final String DEX_TOOL_NAME = "Dex";
  static final String COULD_NOT_CONVERT_BYTECODE_TO_DEX = "Error converting bytecode to dex:\nCause: %s";
  static final String INVALID_BYTE_CODE_VERSION = "Dex cannot parse version %1$d byte code.\nThis is caused by library dependencies that have been compiled using Java 8 or above.\nIf you are using the 'java' gradle plugin in a library submodule add \ntargetCompatibility = '1.7'\nsourceCompatibility = '1.7'\nto that submodule's build.gradle file.";
  private static final Pattern INVALID_BYTE_CODE_VERSION_EXCEPTION_PATTERN = Pattern.compile("com.android.dx.cf.iface.ParseException: bad class file magic \\(cafebabe\\) or version \\((\\d+)\\.\\d+\\).*");
  private static final Pattern UNSUPPORTED_CLASS_FILE_VERSION_PATTERN = Pattern.compile("unsupported class file version (\\d+)\\.\\d+");

}
