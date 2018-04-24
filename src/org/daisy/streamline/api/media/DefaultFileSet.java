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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a default file set.
 * @author Joel Håkansson
 */
public final class DefaultFileSet implements FileSet {
	private static final Logger logger = Logger.getLogger(DefaultFileSet.class.getCanonicalName());
	private final BaseFolder baseFolder;
	private final String manifestPath;
	private final Optional<FormatIdentifier> formatIdentifier;
	private final Map<String, AnnotatedFile> resources;
	
	/**
	 * Provides a builder of file sets.
	 */
	public static class Builder {
		private final BaseFolder baseFolder;
		private final String manifestPath;
		private FormatIdentifier formatIdentifier  = null;
		private final Map<String, AnnotatedFile> resources = new HashMap<>();

		/**
		 * Creates a new builder with the specified base folder and manifest.
		 * @param baseFolder the base folder
		 * @param manifest the manifest
		 * @throws IllegalArgumentException if the manifest isn't a descendant of the base folder
		 */
		public Builder(BaseFolder baseFolder, AnnotatedFile manifest) {
			this(baseFolder, manifest, baseFolder.getPath().relativize(requireDescendant(baseFolder.getPath(), manifest.getFile().toPath())).toString()); 
		}

		/**
		 * Creates a new builder with the specified base folder, manifest and manifest path.
		 * @param baseFolder the base folder
		 * @param manifest the manifest file
		 * @param manifestPath the path to the manifest within the file set
		 */
		public Builder(BaseFolder baseFolder, AnnotatedFile manifest, String manifestPath) {
			this.baseFolder = baseFolder;
			this.manifestPath = manifestPath;
			add(manifest, manifestPath);
		}

		/**
		 * Sets the format identifier for this builder.
		 * @param value the format identifier
		 * @return this builder
		 */
		public Builder formatIdentifier(FormatIdentifier value) {
			this.formatIdentifier = value;
			return this;
		}

		/**
		 * Adds a resource to this builder.
		 * @param f the file
		 * @return this builder
		 */
		public Builder add(File f) {
			add(DefaultAnnotatedFile.with(f).build());
			return this;
		}

		/**
		 * Adds a resource to this builder.
		 * @param f the file
		 * @return this builder
		 */
		public Builder add(AnnotatedFile f) {
			add(f, baseFolder.getPath().relativize(requireDescendant(baseFolder.getPath(), f.getFile().toPath()).normalize()).toString());
			return this;
		}

		/**
		 * Adds a resource to this builder.
		 * @param f the file
		 * @param path the path within the file set
		 * @return this builder
		 */
		public Builder add(File f, String path) {
			add(DefaultAnnotatedFile.with(f).build(), path);
			return this;
		}

		/**
		 * Adds a resource to this builder.
		 * @param f the file
		 * @param path the path within the file set
		 * @return this builder
		 */
		public Builder add(AnnotatedFile f, String path) {
			resources.put(normalizeRelativePath(baseFolder.getPath(), path), f);
			return this;
		}

		/**
		 * Builds the file set.
		 * @return a new file set
		 */
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

	/**
	 * Creates a new builder with the specified base folder and manifest.
	 * @param baseFolder the base folder
	 * @param manifest the manifest
	 * @return returns a new builder
	 * @throws IllegalArgumentException if the manifest isn't a descendant of the base folder
	 */
	public static DefaultFileSet.Builder with(BaseFolder baseFolder, AnnotatedFile manifest) {
		return new Builder(baseFolder, manifest);
	}
	
	/**
	 * Creates a new builder with the specified base folder and manifest.
	 * @param baseFolder the base folder
	 * @param manifest the manifest
	 * @param manifestPath the path to the manifest within the file set
	 * @return returns a new builder
	 * @throws IllegalArgumentException if the manifest isn't a descendant of the base folder
	 */
	public static DefaultFileSet.Builder with(BaseFolder baseFolder, AnnotatedFile manifest, String manifestPath) {
		return new Builder(baseFolder, manifest, manifestPath);
	}
	
	static String normalizeRelativePath(Path base, String path) {
		return base.relativize(base.resolve(path)).normalize().toString();
	}
	
	static Path requireDescendant(Path base, Path path) {
		if (!isDescendant(base, path)) {
			throw new IllegalArgumentException(String.format("'%s' is not a descendant of '%s'", path, base));
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
	
	public String getManifestPath() {
		return manifestPath;
	}
	
	@Override
	public boolean isManifest(String path) {
		return resources.containsKey(normalizeRelativePath(baseFolder.getPath(), path));
	}

	@Override
	public Optional<FormatIdentifier> getFormatIdentifier() {
		return formatIdentifier;
	}

	/**
	 * Gets all registered resources.
	 * @return the resources
	 */
	Map<String, AnnotatedFile> getResources() {
		return Collections.unmodifiableMap(resources);
	}
	
	@Override
	public Set<String> getResourcePaths() {
		return Collections.unmodifiableSet(resources.keySet());
	}
	
	@Override
	public Optional<AnnotatedFile> getResource(String path) {
		return Optional.ofNullable(resources.get(normalizeRelativePath(baseFolder.getPath(), path)));
	}

	/**
	 * Creates a new file set at the specified location. All other properties 
	 * are copied from this file set. Resources are copied to the new file set
	 * to the extent possible, see {@link #internalizeAllCopy()}.
	 * @param copyPath the new location, this must point to an existing directory. It is recommended,
	 * although not strictly required, that the folder is also empty.
	 * @return the created file set
	 */
	public FileSet copyTo(BaseFolder copyPath) {
		// Create a new file set at the specified location
		// All other properties are copied
		DefaultFileSet.Builder builder = new DefaultFileSet.Builder(copyPath, this.getManifest(), this.getManifestPath());
		builder.formatIdentifier(this.getFormatIdentifier().orElse(null));
		// Add all resources from the original file set
		getResourcePaths().stream().forEach(v->{
			builder.add(resources.get(v), v);
		});
		DefaultFileSet ret = builder.build();
		// Internalize all resources by coping them
		//FIXME: all resources should be copied manually, also use findCommonDirectory
		ret.internalizeAllCopy();
		return ret;
	}
	
	Optional<Path> findCommonDirectory() {
		if (resources.isEmpty()) {
			return Optional.empty();
		}
		// Map to a set of normalized paths
		Set<Path> tmp = resources.values().stream()
				.map(v->v.getFile().toPath().normalize())
				.collect(Collectors.toSet());
		if (tmp.size()==1) {
			// Easy, only one resource
			return Optional.ofNullable(tmp.iterator().next().getParent());
		}
		int maxNameCount = tmp.stream()
				.mapToInt(v->v.getNameCount())
				.max()
				.orElse(0);
		// Look for shared ancestors
		int ret = 0;
		for (int i=1;;i++) {
			int ii = i;
			if (i<maxNameCount && tmp.stream()
					// gets the ancestor at the current level
					.map(v->v.subpath(0, Math.min(ii, v.getNameCount()-1)))
					.distinct()
					.count()==1) {
				ret = i;
			} else {
				break;
			}
		}
		// All resources are equal up to the end index, so just use anyone
		return Optional.ofNullable(tmp.iterator().next().subpath(0, ret));
	}
	
	@Override
	public Stream<AnnotatedFile> streamExternal() {
		return resources.values().stream()
			.filter(v->!isDescendant(baseFolder.getPath(), v.getFile().toPath()));
	}

	@Override
	public void internalizeAllCopy() {
		resources.entrySet().forEach(r->{
			internalizeCopy(r.getKey(), r.getValue()).ifPresent(f->r.setValue(f));
		});
	}
	
	@Override
	public void internalizeBelow(Path base) {
		resources.entrySet().forEach(r->{
			if (isDescendant(base, r.getValue().getFile().toPath())) {
				internalize(r.getKey(), r.getValue()).ifPresent(v->r.setValue(v));
			}
		});
	}

	@Override
	public void internalizeCopy(String path) {
		Optional.ofNullable(resources.get(path))
			.ifPresent(
				f->internalizeCopy(path, f).ifPresent(v->resources.put(path, v))
			);
	}
	
	@Override
	public void internalize(String path) {
		Optional.ofNullable(resources.get(path))
			.ifPresent(
				f->internalize(path, f).ifPresent(v->resources.put(path, v))
			);
	}
	
	private Optional<AnnotatedFile> internalizeCopy(String path, AnnotatedFile f) {
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
	
	private Optional<AnnotatedFile> internalize(String path, AnnotatedFile f) {
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
