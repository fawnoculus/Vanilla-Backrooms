package net.fawnoculus.vanilla_backrooms.util.config;

import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
