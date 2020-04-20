package com.android.tools.build.bundletool.mergers;

import com.android.dx.command.dexer.DxContext;
import com.android.tools.build.bundletool.mergers.dexing.DexArchiveMerger;
import com.android.tools.build.bundletool.mergers.dexing.DexingType;
import com.android.tools.build.bundletool.model.exceptions.CommandExecutionException;
import com.android.tools.build.bundletool.model.utils.ThrowableUtils;
import com.android.tools.build.bundletool.model.utils.files.FilePreconditions;
import com.android.tools.r8.CompilationFailedException;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import static com.android.tools.build.bundletool.model.utils.files.FilePreconditions.checkDirectoryExistsAndEmpty;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Created by YangJing on 2020/04/20 .
 * Email: yangjing.yeoh@bytedance.com
 */
public class DxDexMerger implements DexMerger {

  public static final String DX_UNEXPECTED_EXCEPTION = "UNEXPECTED TOP-LEVEL EXCEPTION:";
  private static final String DEX_OVERFLOW_MSG =
      "Cannot fit requested classes in a single dex file";
  private final ForkJoinPool forkJoinPool = new ForkJoinPool();

  private static void validateInput(ImmutableList<Path> dexFiles, Path outputDir) {
    checkDirectoryExistsAndEmpty(outputDir);
    dexFiles.forEach(FilePreconditions::checkFileExistsAndReadable);
  }

  private static CommandExecutionException translateD8Exception(CompilationFailedException d8Exception) {
    // DexOverflowException is thrown when the merged result doesn't fit into a single dex file and
    // `mainDexClasses` is empty. Detection of the exception in the stacktrace is non-trivial and at
    // the time of writing this code it is suppressed exception of the root cause.
    if (ThrowableUtils.anyInCausalChainOrSuppressedMatches(
        d8Exception, t -> t.getMessage() != null && t.getMessage().contains(DEX_OVERFLOW_MSG))) {
      return new CommandExecutionException(
          "Dex merging failed because the result does not fit into a single dex file and"
              + " multidex is not supported by the input.",
          d8Exception);
    } else {
      return new CommandExecutionException("Dex merging failed.", d8Exception);
    }
  }

  @Override
  public ImmutableList<Path> merge(ImmutableList<Path> dexFiles, Path outputDir, Optional<Path> mainDexListFile, boolean isDebuggable, int minSdkVersion) {
    validateInput(dexFiles, outputDir);
    DxContext dxContext = new DxContext();
    DexArchiveMerger merger = DexArchiveMerger.createDxDexMerger(dxContext, forkJoinPool);
    try {
      Path mainDexListPath = null;
      if (mainDexListFile.isPresent()) {
        mainDexListPath = mainDexListFile.get();
      }
//      List<Path> inputs = new ArrayList<>();
//      for (Path dexFile : dexFiles) {
//        Path dir = dexFile;
//        if (dexFile.toFile().isFile()) {
//          dir = dexFile.getParent();
//        }
//        if (!inputs.contains(dir)) {
//          inputs.add(dir);
//        }
//      }
      merger.mergeDexArchives(dexFiles, outputDir, mainDexListPath, DexingType.LEGACY_MULTIDEX);

      File[] mergedFiles = outputDir.toFile().listFiles();
      return Arrays.stream(mergedFiles).map(File::toPath).collect(toImmutableList());

    } catch (com.android.tools.build.bundletool.mergers.dexing.DexArchiveMergerException e) {
      e.printStackTrace();
      dxContext.err.println(DX_UNEXPECTED_EXCEPTION);
      dxContext.err.println(Throwables.getRootCause(e));
      dxContext.err.print(Throwables.getStackTraceAsString(e));
    }

//    try {
//      List<Dex> dexList = new ArrayList<>();
//      for (Path dexPath : dexFiles) {
//        dexList.add(new Dex(dexPath.toFile()));
//      }
//      com.android.dx.merge.DexMerger dexMerger =
//          new com.android.dx.merge.DexMerger(
//              dexList.toArray(new Dex[0]),
//              CollisionPolicy.FAIL,
//              dxContext);
//      Dex output = dexMerger.merge();
//      // copy to output dir
//      output.writeTo(outputDir.toFile());
//
//    } catch (IOException e) {
//      e.printStackTrace();
//      dxContext.err.println(DX_UNEXPECTED_EXCEPTION);
//      dxContext.err.println(Throwables.getRootCause(e));
//      dxContext.err.print(Throwables.getStackTraceAsString(e));
//
//      throw new DexArchiveMergerException("Unable to merge dex", e);
//    }
    return null;
  }
}
