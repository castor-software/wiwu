package se.kth.castor.wiwu;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.dutra.tools.maven.deptree.core.Node;

import java.io.IOException;

public class NodeAdapter extends TypeAdapter<Node> {

    @Override
    public void write(JsonWriter jsonWriter, Node node) throws IOException {
        jsonWriter.beginObject()
                .name("coordinates")
                .jsonValue("\"" + node.getArtifactCanonicalForm() + "\"")
                .name("children")
                .beginArray();
        for (Node c : node.getChildNodes()) {
            this.write(jsonWriter, c);
        }
        jsonWriter.endArray()
                .endObject();
    }

    @Override
    public Node read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
