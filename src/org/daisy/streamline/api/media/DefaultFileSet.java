package org.daisy.streamline.api.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
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
public final class DefaultFileSet implements ModifiableFileSet {
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
			this(baseFolder, manifest, baseFolder.getPath().relativize(requireDescendant(baseFolder.getPath(), manifest.getPath())).toString()); 
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
		 * Creates a new builder with the specified resources.
		 * @param manifest the manifest
		 * @param resources the resources
		 * @throws IllegalArgumentException if the resources do not share the same file root
		 */
		public Builder(AnnotatedFile manifest, AnnotatedFile ... resources) {
			this(manifest, Arrays.asList(resources));
		}

		/**
		 * Creates a new builder with the specified resources.
		 * @param manifest the manifest
		 * @param resources the resources
		 * @throws IllegalArgumentException if the resources do not share the same file root
		 */
		public Builder(AnnotatedFile manifest, Collection<AnnotatedFile> resources) {
			this(BaseFolder.with(findCommonAncestor(
					Stream.concat(resources.stream().map(v->v.getPath()), Stream.of(manifest.getPath()))
				).orElseThrow(IllegalArgumentException::new)), manifest);
			resources.forEach(v->add(v));
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
		public Builder add(Path f) {
			add(DefaultAnnotatedFile.with(f).build());
			return this;
		}

		/**
		 * Adds a resource to this builder.
		 * @param f the file
		 * @return this builder
		 */
		public Builder add(AnnotatedFile f) {
			add(f, baseFolder.getPath().relativize(requireDescendant(baseFolder.getPath(), f.getPath()).normalize()).toString());
			return this;
		}

		/**
		 * Adds a resource to this builder.
		 * @param f the file
		 * @param path the path within the file set
		 * @return this builder
		 */
		public Builder add(Path f, String path) {
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
		return descendant.toAbsolutePath().normalize().startsWith(base.toAbsolutePath().normalize());
	}
	
	boolean isDescendant(Path descendant) {
		return isDescendant(getBaseFolder().getPath(), descendant);
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
	
	@Override
	public Set<String> getResourcePaths() {
		return Collections.unmodifiableSet(resources.keySet());
	}
	
	@Override
	public Optional<AnnotatedFile> getResourceForKey(String path) {
		return Optional.ofNullable(resources.get(path));
	}
	
	@Override
	public Optional<AnnotatedFile> getResource(String path) {
		return Optional.ofNullable(resources.get(normalizeRelativePath(baseFolder.getPath(), path)));
	}
	
	@Override
	public Optional<AnnotatedFile> getResource(Path path) {
		return getResource(getBaseFolder().getPath().relativize(path).toString());
	}

	/**
	 * Creates a new file set at the specified location. All other properties 
	 * are copied from this file set. Resources are copied to the new file set
	 * to the extent possible, see {@link #internalizeAllCopy()}.
	 * @param source the original file set
	 * @param target the new location, this must point to an existing directory. It is recommended,
	 * although not strictly required, that the folder is also empty.
	 * @return the created file set
	 * @throws IOException if an I/O error occurs
	 */
	public static DefaultFileSet copy(FileSet source, BaseFolder target) throws IOException {
		Files.createDirectories(target.getPath());
		// Create a new file set at the specified location
		// All other properties are copied
		DefaultFileSet.Builder builder = new DefaultFileSet.Builder(target, source.getManifest(), source.getManifestPath());
		builder.formatIdentifier(source.getFormatIdentifier().orElse(null));
		// Add all resources from the original file set
		source.getResourcePaths().stream().forEach(v->{
			source.getResourceForKey(v).ifPresent(key->builder.add(key, v));
		});
		DefaultFileSet ret = builder.build();
		// Internalize all resources by coping them
		ret.internalizeAllCopy();
		return ret;
	}

	static Optional<Path> findCommonAncestor(Collection<Path> resources) {
		if (resources.isEmpty()) {
			return Optional.empty();
		}
		return findCommonAncestor(resources.stream());
	}

	private static Optional<Path> findCommonAncestor(Stream<Path> resources) {
		// Map to a set of normalized paths
		Set<Path> tmp = resources
				.map(v->v.normalize())
				.collect(Collectors.toSet());
		if (tmp.size()==1) {
			// Easy, only one resource
			return Optional.ofNullable(tmp.iterator().next().getParent());
		}
		if (tmp.stream().map(v->v.getRoot()).distinct().count()>1) {
			return Optional.empty();
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
		if (ret==0) {
			return Optional.empty();
		} else {
			// All resources are equal up to the end index, so just use any
			Path p = tmp.iterator().next();
			Path sub = p.subpath(0, ret);
			// There's no reason for sub to be null here, but it doesn't hurt to check it
			if (sub==null) {
				return Optional.empty();
			} else {
				Path root = p.getRoot();
				return root==null ? Optional.of(sub) : Optional.of(root.resolve(sub));
			}
		}
	}
	
	@Override
	public Stream<AnnotatedFile> streamExternal() {
		return resources.values().stream()
			.filter(v->!isDescendant(baseFolder.getPath(), v.getPath()));
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
			if (isDescendant(base, r.getValue().getPath())) {
				internalize(r.getKey(), r.getValue()).ifPresent(v->r.setValue(v));
			}
		});
	}

	@Override
	public boolean internalizeCopy(String path) {
		Optional<AnnotatedFile> res = Optional.ofNullable(resources.get(path)).flatMap(f->internalizeCopy(path, f));
		if (res.isPresent()) {
			resources.put(path, res.get());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean internalize(String path) {
		Optional<AnnotatedFile> res = Optional.ofNullable(resources.get(path)).flatMap(v->internalize(path, v));
		if (res.isPresent()) {
			resources.put(path, res.get());
			return true;
		}
		return false;
	}
	
	private Optional<AnnotatedFile> internalizeCopy(String path, AnnotatedFile f) {
		if (!isDescendant(baseFolder.getPath(), f.getPath())) {
			Path newLocation = baseFolder.getPath().resolve(path);
			if (isDescendant(baseFolder.getPath(), newLocation)) {
				try {
					Files.createDirectories(newLocation.getParent());
					Files.copy(f.getPath(), newLocation, StandardCopyOption.REPLACE_EXISTING);
					return Optional.of(DefaultAnnotatedFile.with(f).file(newLocation.toFile()).build());
				} catch (IOException e) {
					logger.log(Level.WARNING, "Could not copy file: " + f.getPath(), e);
				}				
			}
		}
		return Optional.empty();
	}
	
	private Optional<AnnotatedFile> internalize(String path, AnnotatedFile f) {
		if (!isDescendant(baseFolder.getPath(), f.getPath())) {
			Path newLocation = baseFolder.getPath().resolve(path);
			if (isDescendant(baseFolder.getPath(), newLocation)) {
				try {
					Files.createDirectories(newLocation.getParent());
					Files.move(f.getPath(), newLocation, StandardCopyOption.REPLACE_EXISTING);
					return Optional.of(DefaultAnnotatedFile.with(f).file(newLocation.toFile()).build());
				} catch (IOException e) {
					logger.log(Level.WARNING, "Could not move file: " + f.getPath(), e);
				}
			}
		}
		return Optional.empty();
	}

}
