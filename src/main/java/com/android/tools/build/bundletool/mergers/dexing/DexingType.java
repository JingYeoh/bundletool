package com.android.tools.build.bundletool.mergers.dexing;

/**
 * TODO: please input the description for this class
 * Created by YangJing on 2020/04/20 .
 * Email: yangjing.yeoh@bytedance.com
 */
public enum DexingType {

  MONO_DEX(
      false,
      true,
      false
  ),
  LEGACY_MULTIDEX(
      true,
      false,
      true
  ),
  NATIVE_MULTIDEX(
      true,
      true,
      false
  );
  /** If this mode allows multiple DEX files. */
  boolean multiDex;
  /** If we should pre-dex in this dexing mode. */
  boolean preDex;
  /** If a main dex list is required for this dexing mode. */
  boolean needsMainDexList;

  DexingType(boolean multiDex, boolean preDex, boolean needsMainDexList) {
    this.multiDex = multiDex;
    this.preDex = preDex;
    this.needsMainDexList = needsMainDexList;
  }

  public boolean isPreDex() {
    return preDex;
  }

  public boolean isMultiDex() {
    return multiDex;
  }
}