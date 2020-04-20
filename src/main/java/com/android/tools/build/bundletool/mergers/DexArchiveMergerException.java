package com.android.tools.build.bundletool.mergers;

/**
 * Created by YangJing on 2020/04/20 .
 * Email: yangjing.yeoh@bytedance.com
 */
public class DexArchiveMergerException extends RuntimeException {
  public DexArchiveMergerException(Throwable cause) {
    super(cause);
  }

  public DexArchiveMergerException(String message) {
    super(message);
  }

  public DexArchiveMergerException(String message, Throwable cause) {
    super(message, cause);
  }
}
