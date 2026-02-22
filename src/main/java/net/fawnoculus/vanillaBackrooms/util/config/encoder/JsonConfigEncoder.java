package net.fawnoculus.vanillaBackrooms.util.config.encoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.Strictness;
import com.mojang.serialization.JsonOps;
import net.fawnoculus.vanillaBackrooms.util.config.ConfigEncoding;
import net.fawnoculus.vanillaBackrooms.util.config.ConfigFile;
import net.fawnoculus.vanillaBackrooms.util.config.ConfigOption;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JsonConfigEncoder implements ConfigEncoding {
    public static final Gson GSON = new GsonBuilder()
      .serializeNulls()
      .setStrictness(Strictness.LENIENT)
      .setPrettyPrinting()
      .create();
    private static final JsonConfigEncoder INSTANCE = new JsonConfigEncoder();

    @Contract(pure = true)
    private JsonConfigEncoder() {
    }

    @Contract(pure = true)
    public static JsonConfigEncoder getInstance() {
        return INSTANCE;
    }


    @Override
    public void read(@NotNull Reader reader, @NotNull ConfigFile to) {
        JsonObject json = GSON.fromJson(GSON.newJsonReader(reader), JsonObject.class);
        for (ConfigOption<?> option : to.getOptions()) {
            option.setValueFrom(json.get(option.getName()), JsonOps.INSTANCE);
        }
    }

    @Override
    public void write(@NotNull Writer writer, @NotNull ConfigFile from) {
        JsonObject json = new JsonObject();
        for (ConfigOption<?> option : from.getOptions()) {
            if (option.getComment() != null) {
                json.addProperty("__" + option.getName() + "_comment", option.getComment());
            }
            json.add(option.getName(), option.getEncodedValue(JsonOps.INSTANCE));
        }
        GSON.toJson(json, writer);
        try {
            writer.flush();
            writer.close();
        } catch (IOException ignored) {
        }
    }
}
