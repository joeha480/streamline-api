package org.daisy.streamline.api.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class DefaultFileSet implements FileSet {
	private static final Logger logger = Logger.getLogger(DefaultFileSet.class.getCanonicalName());
	private final BaseFolder baseFolder;
	private final String manifestPath;
	private final Optional<FormatIdentifier> formatIdentifier;
	private final Map<String, AnnotatedFile> resources;
	
	public static class Builder {
		private final BaseFolder baseFolder;
		private final String manifestPath;
		private FormatIdentifier formatIdentifier  = null;
		private final Map<String, AnnotatedFile> resources = new HashMap<>();

		public Builder(BaseFolder baseFolder, AnnotatedFile manifest) {
			this(baseFolder, manifest, baseFolder.getPath().relativize(manifest.getFile().toPath()).toString()); 
		}

		public Builder(BaseFolder baseFolder, AnnotatedFile manifest, String manifestPath) {
			this.baseFolder = baseFolder;
			this.manifestPath = manifestPath;
			add(manifest, manifestPath);
		}

		public Builder formatIdentifier(FormatIdentifier value) {
			this.formatIdentifier = value;
			return this;
		}

		public Builder add(File f) {
			add(DefaultAnnotatedFile.with(f).build());
			return this;
		}

		public Builder add(AnnotatedFile f) {
			add(f, requireDescendant(baseFolder.getPath(), baseFolder.getPath().relativize(f.getFile().toPath()).normalize()).toString());
			return this;
		}

		public Builder add(File f, String path) {
			add(DefaultAnnotatedFile.with(f).build(), path);
			return this;
		}

		public Builder add(AnnotatedFile f, String path) {
			resources.put(normalizeRelativePath(baseFolder, path), f);
			return this;
		}

		public DefaultFileSet build() {
			return new DefaultFileSet(this);
		}
	}

	private DefaultFileSet(Builder builder) {
		this.baseFolder = builder.baseFolder;
		this.manifestPath = builder.manifestPath;
		this.formatIdentifier = Optional.ofNullable(builder.formatIdentifier);
		this.resources = new HashMap<>(builder.resources);
	}

	public static DefaultFileSet.Builder with(BaseFolder baseFolder, AnnotatedFile manifest) {
		return new Builder(baseFolder, manifest);
	}
	
	static String normalizeRelativePath(BaseFolder base, String path) {
		return base.getPath().relativize(base.getPath().resolve(path)).normalize().toString();
	}
	
	static Path requireDescendant(Path base, Path path) {
		if (!isDescendant(base, path)) {
			throw new IllegalArgumentException(String.format("'%1' is not a descendant of '%2'", path, base));
		}
		return path;
	}
	
	static boolean isDescendant(Path base, Path descendant) {
		return descendant.normalize().startsWith(base.normalize());
	}

	@Override
	public BaseFolder getBaseFolder() {
		return baseFolder;
	}

	@Override
	public AnnotatedFile getManifest() {
		return Objects.requireNonNull(resources.get(manifestPath));
	}
	
	@Override
	public boolean isManifest(String path) {
		return resources.containsKey(normalizeRelativePath(baseFolder, path));
	}

	@Override
	public Optional<FormatIdentifier> getFormatIdentifier() {
		return formatIdentifier;
	}

	@Override
	public Map<String, AnnotatedFile> getResources() {
		return Collections.unmodifiableMap(resources);
	}
	
	@Override
	public Set<String> getResourcePaths() {
		return Collections.unmodifiableSet(resources.keySet());
	}
	
	@Override
	public Optional<AnnotatedFile> getResource(String path) {
		return Optional.ofNullable(resources.get(normalizeRelativePath(baseFolder, path)));
	}

	/**
	 * Creates a new file set at the specified location. All other properties 
	 * are copied from this file set. Resources are copied to the new file set
	 * to the extent possible, see {@link #copyExternal()}.
	 * @param copyPath the new location, this must point to an existing directory. It is recommended,
	 * although not strictly required, that the folder is also empty.
	 * @return the created file set
	 */
	public FileSet copyTo(BaseFolder copyPath) {
		// Create a new file set at the specified location
		// All other properties are copied
		DefaultFileSet.Builder builder = new DefaultFileSet.Builder(copyPath, this.getManifest(), this.manifestPath);
		builder.formatIdentifier(this.getFormatIdentifier().orElse(null));
		// Add all resources from the original file set
		getResourcePaths().stream().forEach(v->{
			builder.add(resources.get(v), v);
		});
		DefaultFileSet ret = builder.build();
		// Internalize all resources by coping them
		ret.copyExternal();
		return ret;
	}
	
	@Override
	public Stream<AnnotatedFile> streamExternal() {
		return resources.values().stream()
			.filter(v->!isDescendant(baseFolder.getPath(), v.getFile().toPath()));
	}

	@Override
	public void copyExternal() {
		resources.entrySet().forEach(r->{
			copyExternal(r.getKey(), r.getValue()).ifPresent(f->r.setValue(f));
		});
	}
	
	@Override
	public void moveExternal(Path base) {
		resources.entrySet().forEach(r->{
			if (isDescendant(base, r.getValue().getFile().toPath())) {
				moveExternal(r.getKey(), r.getValue()).ifPresent(v->r.setValue(v));
			}
		});
	}

	@Override
	public void internalizeCopy(String path) {
		Optional.ofNullable(resources.get(path))
			.ifPresent(
				f->copyExternal(path, f).ifPresent(v->resources.put(path, v))
			);
	}
	
	@Override
	public void internalize(String path) {
		Optional.ofNullable(resources.get(path))
			.ifPresent(
				f->moveExternal(path, f).ifPresent(v->resources.put(path, v))
			);
	}
	
	private Optional<AnnotatedFile> copyExternal(String path, AnnotatedFile f) {
		if (!isDescendant(baseFolder.getPath(), f.getFile().toPath())) {
			Path newLocation = baseFolder.getPath().resolve(path);
			if (isDescendant(baseFolder.getPath(), newLocation)) {
				try {
					Files.copy(f.getFile().toPath(), newLocation, StandardCopyOption.REPLACE_EXISTING);
					return Optional.of(DefaultAnnotatedFile.with(f).file(newLocation.toFile()).build());
				} catch (IOException e) {
					logger.log(Level.WARNING, "Could not copy file: " + f.getFile(), e);
				}				
			}
		}
		return Optional.empty();
	}
	
	private Optional<AnnotatedFile> moveExternal(String path, AnnotatedFile f) {
		if (!isDescendant(baseFolder.getPath(), f.getFile().toPath())) {
			Path newLocation = baseFolder.getPath().resolve(path);
			if (isDescendant(baseFolder.getPath(), newLocation)) {
				try {
					Files.move(f.getFile().toPath(), newLocation, StandardCopyOption.REPLACE_EXISTING);
					return Optional.of(DefaultAnnotatedFile.with(f).file(newLocation.toFile()).build());
				} catch (IOException e) {
					logger.log(Level.WARNING, "Could not move file: " + f.getFile(), e);
				}
			}
		}
		return Optional.empty();
	}

}
