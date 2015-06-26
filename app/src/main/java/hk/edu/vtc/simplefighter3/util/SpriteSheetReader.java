package hk.edu.vtc.simplefighter3.util;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import hk.edu.vtc.simplefighter3.model.Frame;
import hk.edu.vtc.simplefighter3.model.SpriteSheet;


public class SpriteSheetReader {
    public static Map<String, Frame> getFrames(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        Map<String, Frame> frames = new HashMap<String, Frame>();

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "frames":
                        readFrames(reader, frames);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }

        return frames;
    }

    private static void readFrames(JsonReader jsonReader, Map<String, Frame> frames) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            Frame frame = readFrame(jsonReader);
            frames.put(frame.getFilename(), frame);
        }
        jsonReader.endArray();
    }

    private static Frame readFrame(JsonReader reader) throws IOException {
        Frame frame = new Frame();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "filename":
                    frame.setFilename(reader.nextString());
                    break;
                case "frame":
                    readLocation(reader, frame);
                    break;
                case "rotated":
                    frame.setIsRotated(reader.nextBoolean());
                    break;
                case "pivot":
                    readPivot(reader, frame);
                    break;
                default:
                    reader.skipValue();
                    break;
            }


        }
        reader.endObject();
        return frame;
    }

    private static void readLocation(JsonReader reader, Frame frame) throws IOException {
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "x":
                    frame.setX(reader.nextInt());
                    break;
                case "y":
                    frame.setY(reader.nextInt());
                    break;
                case "w":
                    frame.setWeight(reader.nextInt());
                    break;
                case "h":
                    frame.setHeight(reader.nextInt());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
    }

    private static void readPivot(JsonReader reader, Frame frame) throws IOException {
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "x":
                    frame.setPivotX(reader.nextDouble());
                    break;
                case "y":
                    frame.setPivotY(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
    }


}
