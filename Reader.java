import java.io.IOException;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Reader {
    public static Stream<String> read(String path) throws IOException {
        return Files.lines(Paths.get(path), StandardCharsets.UTF_8);
    }
}
