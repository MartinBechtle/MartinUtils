package martinutils.health;

import martinutils.health.HealthVerifier;
import martinutils.io.FileUtil;
import martinutils.runtime.Assert;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

public final class HealthTrace extends Thread {

	private final String ctx;
	private List<String> classPathDirs;
	private Map<Path, Long> pjFiles;
	private static HealthTrace instance;
	private boolean stop = false;
	private static HashSet<String> supportedExtensions = new HashSet<>(
			Arrays.asList("xml", "java", "class", "html", "js", "css"));

	public static synchronized void checkHealth(String context, String root) {
		Assert.notEmpty(context, "context");
		if (instance != null) {
			HealthTrace.stopCheck();
		}
		instance = new HealthTrace(context, root);
		instance.start();
	}

	private HealthTrace(String ctx, String root) {

		classPathDirs = new ArrayList<>();
		if (root != null && Files.exists(Paths.get(root))) {
			classPathDirs.add(root);
		}
		else {
			String classpath = System.getProperty("java.class.path");
			String[] classpathEntries = classpath.split(File.pathSeparator);
			for (String str : classpathEntries) {
				if (!str.endsWith(".jar")) {
					classPathDirs.add(str);
				}
			}
		}
		this.ctx = ctx;
	}

	private static synchronized void stopCheck() {
		instance.stop = true;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				if (stop) {
					return;
				}
				updateFiles();
				searchModFiles();
				flushFiles();
			} catch (Exception e) { }
		}
	}

	long nextFileMod = 0;

	private void searchModFiles() throws IOException {

		long now = System.currentTimeMillis();
		if (now < nextFileMod) {
			return;
		}
		nextFileMod = now + 30 * 1000;

		List<Path> toRemove = new ArrayList<>();
		for (Map.Entry<Path, Long> entry : pjFiles.entrySet()) {

			Path file = entry.getKey();
			if (!Files.exists(file)) {
				toRemove.add(file);
				continue;
			}
			BasicFileAttributes basicFileAttributes = Files.getFileAttributeView(file, BasicFileAttributeView.class).readAttributes();
			FileTime time = basicFileAttributes.lastModifiedTime();
			Long lastMod = time.toMillis();

			if (lastMod > entry.getValue()) {
				pushFile(file.getFileName().toString());
				entry.setValue(lastMod);
			}
		}
		for (Path file : toRemove) {
			pjFiles.remove(file);
		}
	}

	long nextFileUpdate = 0;

	private void updateFiles() throws IOException {

		long now = System.currentTimeMillis();
		if (now < nextFileUpdate) {
			return;
		}
		nextFileUpdate = now + 60 * 1000;

		pjFiles = new HashMap<>();

		for (String str : classPathDirs) {

			Path classpathEntry = Paths.get(str);
			
			Files.walk(classpathEntry)
					.filter(entry -> {
						String fileName = entry.getFileName().toString();
						String extension = FileUtil.getFileExt(fileName);
						return supportedExtensions.contains(extension);
					})
					.forEach(path -> pjFiles.put(path, now));
		}
	}

	private List<String> filesToPush = new ArrayList<>();
	long nextFileFlush = 0;

	private void pushFile(String fileName) {
		filesToPush.add(fileName);
	}

	private void flushFiles() {

		long now = System.currentTimeMillis();
		if (now < nextFileFlush) {
			return;
		}
		nextFileFlush = now + 60 * 1000;

		if (filesToPush.size() > 0) {
			if (filesToPush.size() > 5) {
				filesToPush = filesToPush.subList(0, 3);
			}
			String desc = StringUtils.join(filesToPush, ", ");
			HealthVerifier.verify(ctx, desc);
			filesToPush.clear();
		}
	}
}