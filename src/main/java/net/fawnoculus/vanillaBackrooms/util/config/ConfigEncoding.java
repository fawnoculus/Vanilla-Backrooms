package net.fawnoculus.vanillaBackrooms.util.config;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;

public interface ConfigEncoding {
    default void readPath(@NotNull Path path, @NotNull ConfigFile to) throws IOException {
        this.read(new FileReader(path.toFile()), to);
    }

    default void writePath(@NotNull Path path, @NotNull ConfigFile to) throws IOException {
        this.write(new FileWriter(path.toFile()), to);
    }

    void read(@NotNull Reader reader, @NotNull ConfigFile to);

    void write(@NotNull Writer writer, @NotNull ConfigFile from);
}
